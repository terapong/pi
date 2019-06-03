package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.primefaces.model.chart.MeterGaugeChartModel;

import toto.pi3.ejb.entity.*;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "managehousebean")
@ViewScoped
public class ManageHouseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private MeterGaugeChartModel tempMeterIn;
	private MeterGaugeChartModel humMeterIn;
	private MeterGaugeChartModel co2MeterIn;
	private MeterGaugeChartModel tempMeterOut;
	private MeterGaugeChartModel humMeterOut;
//	private MeterGaugeChartModel co2MeterOut;
	private List<House> houses;
	private House house;
	private long selectedHouseId;
	private List<Topic> topics;
	
	private MqttClient myClient;
	private MqttConnectOptions connOpt;
	private int qos = 0;
	
	private String on = "images/led_green.png";
	private String off = "images/led_off.png";
	
	private String imgNAOn = on;
	private String imgAlert;
	private String imgSW;
	private String imgFrel;
	private String imgFin;
	private String imgFout;
	private String imgPout;
	private String imgPin;
	
	private String temIn;
	private String humIn;
	private String temOut;
	private String humOut;
	
	static final String BROKER_URL = "tcp://localhost:1883";
	//static final String BROKER_URL = "tcp://192.168.1.3:1883";
	static final String M2MIO_THING = "terapong_web";
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		houses = session.queryAllHouse();
		if(!houses.isEmpty()) {
			house = houses.get(0);
			selectedHouseId = house.getId();
			topics = house.getNodeMCU().getTopics();
			if(house.isSw1()) {
				sendSW("SW_1_ON");
				imgSW = on;
			} else {
				sendSW("SW_1_OFF");
				imgSW = off;
			}
			
			if(house.isOpenAlert()) {
				sendSW("ALERT");
				imgAlert = on;
			} else {
				sendSW("ALERT_OFF");
				imgAlert = off;
			}
			refreshMeter();
		}
		
	}

	@PreDestroy
	private void destroy() {
		
	}
	
	private Message getMessageValue(String subTopic) {
		for(Topic t : topics) {
			if(t.getName().indexOf("sub") == -1 && t.getName().indexOf("co2") == -1) {
				Message m = session.queryMessageByTopicId(t.getId());
				if(m != null) {
					if(m.getTopic().getName().indexOf(subTopic) != -1) {
						return m;
					}
				}
			}
		}
    	return null;
    }
	
	public void refreshMeter() {
		house = session.queryHouseById(selectedHouseId);
		topics = house.getNodeMCU().getTopics();
		createMeterGaugeModels();
		setLED(house);
	}
	
	private void createMeterGaugeModels() {
		tempMeterIn = initChartMeterModel("/temp/in", "Temperature Meter ภายใน (" + house.getMushRoom().getTemperatureMin() + " - " + house.getMushRoom().getTemperatureMax() + ")  °C");
		tempMeterOut = initChartMeterModel("/temp/out", "Temperature Meter ภายนอก (" + house.getMushRoom().getTemperatureOutMin() + " - " + house.getMushRoom().getTemperatureOutMax() + ")  °C"); 
		humMeterIn = initChartMeterModel("/hum/in", "Humidity Meter ภายใน (" + house.getMushRoom().getHumidityMin() + " - " + house.getMushRoom().getHumidityMax() + ")  rH");
		humMeterOut = initChartMeterModel("/hum/out", "Humidity Meter ภายนอก (" + house.getMushRoom().getHumidityOutMin() + " - " + house.getMushRoom().getHumidityOutMax() + ")  rH");
//		co2MeterIn = initChartMeterModel("/co2/in", "Co2 Meter ภายใน (" + house.getMushRoom().getCo2Min() + " - " + house.getMushRoom().getCo2Max() + ")  ppm");
//		co2MeterIn = initChartMeterModel("/co2/out", "Co2 Meter ภายนอก (" + house.getMushRoom().getCo2  Min() + " - " + house.getMushRoom().getCo2  Max() + ")  ppm");
	}
	
	private MeterGaugeChartModel initChartMeterModel(String io, String title) {
		Message m = getMessageValue(io);
		MeterGaugeChartModel model = null;
        List<Number> intervals = new ArrayList<Number>();
        if(m != null) {
        	if(io.equals("/temp/in")) {
            	intervals.add(house.getMushRoom().getTemperatureMin());
            	intervals.add(house.getMushRoom().getTemperatureMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(m.getMessage(), intervals);
            	model.setGaugeLabel(m.getMessage() + " °C");
            	temIn = "อุณหภูมิ ภายใน : " + m.getMessage() + " °C";
            } else if(io.equals("/temp/out")) {
            	intervals.add(house.getMushRoom().getTemperatureOutMin());
            	intervals.add(house.getMushRoom().getTemperatureOutMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(m.getMessage(), intervals);
            	model.setGaugeLabel(m.getMessage() + " °C");
            	temOut = "อุณหภูมิ ภายนอก : " + m.getMessage() + " °C";
            } else if(io.equals("/hum/in")) {
            	intervals.add(house.getMushRoom().getHumidityMin());
            	intervals.add(house.getMushRoom().getHumidityMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(m.getMessage(), intervals);
            	model.setGaugeLabel(m.getMessage() + " rh");
            	humIn = "ความชื้น ภายใน : " + m.getMessage() + " rH";
            }
            else if(io.equals("/hum/out")) {
            	intervals.add(house.getMushRoom().getHumidityOutMin());
            	intervals.add(house.getMushRoom().getHumidityOutMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(m.getMessage(), intervals);
            	model.setGaugeLabel(m.getMessage() + " rh");
            	humOut = "ความชื้น ภายนอก : " + m.getMessage() + " rH";
            } 
        } else {
        	if(io.equals("/temp/in")) {
            	intervals.add(house.getMushRoom().getTemperatureMin());
            	intervals.add(house.getMushRoom().getTemperatureMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(0, intervals);
            	model.setGaugeLabel(0 + " °C");
            	temIn = "อุณหภูมิ ภายใน : 0 °C";
            } else if(io.equals("/temp/out")) {
            	intervals.add(house.getMushRoom().getTemperatureOutMin());
            	intervals.add(house.getMushRoom().getTemperatureOutMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(0, intervals);
            	model.setGaugeLabel(0 + " °C");
            	temOut = "อุณหภูมิ ภายนอก : 0 °C";
            } else if(io.equals("/hum/in")) {
            	intervals.add(house.getMushRoom().getHumidityMin());
            	intervals.add(house.getMushRoom().getHumidityMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(0, intervals);
            	model.setGaugeLabel(0 + " rh");
            	humIn = "ความชื้น ภายใน : 0 rH";
            } else if(io.equals("/hum/out")) {
            	intervals.add(house.getMushRoom().getHumidityOutMin());
            	intervals.add(house.getMushRoom().getHumidityOutMax());
            	intervals.add(100);
            	model = new MeterGaugeChartModel(0, intervals);
            	model.setGaugeLabel(0 + " rh");
            	humOut = "ความชื้น ภายนอก : 0 rH";
            }
        }
        
        model.setTitle(title);
        model.setSeriesColors("93b75f,0015ff,cc6666");//E7E658
        
        return model;
    }
     
	public void selHouseChange() {
		refreshMeter();
	}
	
	private void setLED(House house) {
		if(house.isSw1()) {
			imgSW = on;
		} else {
			imgSW = off;
		}
		
		if(house.isOpenAlert()) {
			imgAlert = on;
		} else {
			imgAlert = off;
		}
		
		if(house.isOpenFrel()) {
			imgFrel = on;
		} else {
			imgFrel = off;
		}
		
		if(house.isOpenFin()) {
			imgFin = on;
		} else {
			imgFin = off;
		}
		
		if(house.isIspenFout()) {
			imgFout = on;
		} else {
			imgFout = off;
		}
		
		if(house.isOpenPout()) {
			imgPout = on;
		} else {
			imgPout = off;
		}
		
		if(house.isOpenPin()) {
			imgPin = on;
		} else {
			imgPin = off;
		}
	}
	
	private void sendSW(String pubMsg) {
		// setup MQTT Client
		String clientID = M2MIO_THING;
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		
		String topicName = house.getNodeMCU().getId() + "/pi3/sub";
	
		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, clientID);
			myClient.connect(connOpt);
			//System.out.print("Attempting MQTT connection " + BROKER_URL + " ... ");
			if(myClient.isConnected()) {
				//System.out.println("Connected");
				// setup MQTT Client
				// Connect to Broker
				MqttMessage pubMessage = new MqttMessage(pubMsg.getBytes());
				pubMessage.setQos(qos);
				pubMessage.setRetained(false);
				
				//System.out.println("Publishing to topic \"" + topicName + "\" qos " + qos + " Message " + pubMsg);
				MqttDeliveryToken token = null;
				MqttTopic topicControl = myClient.getTopic(topicName);
				// publish message to broker
				token = topicControl.publish(pubMessage);
				// Wait until the message has been delivered to the broker
				token.waitForCompletion();
				
				try {
					myClient.disconnect();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			} else {
				//System.out.println("Failed");
			}
		} catch (MqttException e) {
			try {
				Thread.sleep(5000);
				//System.out.println("Attempting MQTT connection " + BROKER_URL + " ... Failed");
			} catch(Exception ex) {
//				e.printStackTrace();
//				System.exit(-1);
			}
		}
	}
	
	public void onOff() {
		if(house.isSw1()) {
			sendSW("SW_1_ON");
			imgSW = on;
		} else {
			sendSW("SW_1_OFF");
			imgSW = off;
		}
		session.updateHouse(house);
	}
	
	public void onOffAlert() {
		if(house.isOpenAlert()) {
			sendSW("ALERT");
			imgAlert = on;
		} else {
			sendSW("ALERT_OFF");
			imgAlert = off;
		}
		session.updateHouse(house);
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}

	public List<House> getHouses() {
		return houses;
	}

	public void setHouses(List<House> houses) {
		this.houses = houses;
	}

	public long getSelectedHouseId() {
		return selectedHouseId;
	}

	public void setSelectedHouseId(long selectedHouseId) {
		this.selectedHouseId = selectedHouseId;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public MeterGaugeChartModel getTempMeterIn() {
		return tempMeterIn;
	}

	public void setTempMeterIn(MeterGaugeChartModel tempMeterIn) {
		this.tempMeterIn = tempMeterIn;
	}

	public MeterGaugeChartModel getHumMeterIn() {
		return humMeterIn;
	}

	public void setHumMeterIn(MeterGaugeChartModel humMeterIn) {
		this.humMeterIn = humMeterIn;
	}

	public MeterGaugeChartModel getCo2MeterIn() {
		return co2MeterIn;
	}

	public void setCo2MeterIn(MeterGaugeChartModel co2MeterIn) {
		this.co2MeterIn = co2MeterIn;
	}

	public MeterGaugeChartModel getTempMeterOut() {
		return tempMeterOut;
	}

	public void setTempMeterOut(MeterGaugeChartModel tempMeterOut) {
		this.tempMeterOut = tempMeterOut;
	}

	public MeterGaugeChartModel getHumMeterOut() {
		return humMeterOut;
	}

	public void setHumMeterOut(MeterGaugeChartModel humMeterOut) {
		this.humMeterOut = humMeterOut;
	}

	public String getImgNAOn() {
		return imgNAOn;
	}

	public String getImgAlert() {
		return imgAlert;
	}

	public void setImgAlert(String imgAlert) {
		this.imgAlert = imgAlert;
	}

	public String getImgSW() {
		return imgSW;
	}

	public void setImgSW(String imgSW) {
		this.imgSW = imgSW;
	}

	public String getImgFrel() {
		return imgFrel;
	}

	public void setImgFrel(String imgFrel) {
		this.imgFrel = imgFrel;
	}

	public String getImgFin() {
		return imgFin;
	}

	public void setImgFin(String imgFin) {
		this.imgFin = imgFin;
	}

	public String getImgFout() {
		return imgFout;
	}

	public void setImgFout(String imgFout) {
		this.imgFout = imgFout;
	}

	public String getImgPout() {
		return imgPout;
	}

	public void setImgPout(String imgPout) {
		this.imgPout = imgPout;
	}

	public String getImgPin() {
		return imgPin;
	}

	public void setImgPin(String imgPin) {
		this.imgPin = imgPin;
	}

	public void setImgNAOn(String imgNAOn) {
		this.imgNAOn = imgNAOn;
	}

	public String getTemIn() {
		return temIn;
	}

	public void setTemIn(String temIn) {
		this.temIn = temIn;
	}

	public String getHumIn() {
		return humIn;
	}

	public void setHumIn(String humIn) {
		this.humIn = humIn;
	}

	public String getTemOut() {
		return temOut;
	}

	public void setTemOut(String temOut) {
		this.temOut = temOut;
	}

	public String getHumOut() {
		return humOut;
	}

	public void setHumOut(String humOut) {
		this.humOut = humOut;
	}
}
