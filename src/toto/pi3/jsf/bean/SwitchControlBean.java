package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import toto.pi3.ejb.entity.*;
import toto.pi3.ejb.session.Pi3Session;
import toto.pi3.jsf.bean.Pi3SessionBean;

@ManagedBean(name = "switchcontrolbean")
@ViewScoped
public class SwitchControlBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<House> houses;
	private House house;
	private long selectedHouseId;
	private String topic;
	
	private boolean r_pin;
	private boolean r_pout;
	private boolean r_fin;
	private boolean r_fout;
	private boolean r_frel;
	private boolean r_sw_1;
	private boolean r_alert;
	private boolean r_openall;
	private boolean r_closeall;
	private String message;

	private MqttClient myClient;
	private MqttConnectOptions connOpt;
	private int qos = 0;

	static final String BROKER_URL = "tcp://localhost:1883";
	static final String M2MIO_THING = "terapong_sw";
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		houses = session.queryAllHouse();
		if(!houses.isEmpty()) {
			house = houses.get(0);
			selectedHouseId = house.getId();
			topic = house.getNodeMCU().getId() + "/pi3/sub";
		}
	}

	@PreDestroy
	private void destroy() {
		
	}
	
	public void selHouseChange() {
		house = session.queryHouseById(selectedHouseId);
		topic = house.getNodeMCU().getId() + "/pi3/sub";
	}
	
	public void OnOff_R_SW_1() {
		if(r_sw_1) {
			sendSW("SW_1_ON");
			message = "เปิด ไฟ โรงเรือน";
		} else {
			sendSW("SW_1_OFF");
			message = "ปิด ไฟ โรงเรือน";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void OnOff_R_Alert() {
		if(r_alert) {
			sendSW("ALERT");
			message = "เปิด เตือนภัย";
		} else {
			sendSW("ALERT_OFF");
			message = "ปิด เตือนภัย";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void OnOff_R_Pin() {
		if(r_pin) {
			sendSW("OPEN_PIN");
			message = "เปิด ปั๊มน้ำ ภายใน";
		} else {
			sendSW("CLOSE_PIN");
			message = "ปิด ปั๊มน้ำ ภายใน";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	public void OnOff_R_Pout() {
		if(r_pout) {
			sendSW("OPEN_POUT");
			message = "เปิด ปั๊มน้ำ ภายนอก";
		} else {
			sendSW("CLOSE_POUT");
			message = "ปิด ปั๊มน้ำ ภายนอก";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void OnOff_R_Frel() {
		if(r_frel) {
			sendSW("OPEN_FREL");
			message = "เปิด พัดลมระบายอากาศ ภายใน";
		} else {
			sendSW("CLOSE_FREL");
			message = "ปิด พัดลมระบายอากาศ ภายใน";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void OnOff_R_Fin() {
		if(r_fin) {
			sendSW("OPEN_FIN");
			message = "เปิด พัดลมดูดอากาศ เข้า";
		} else {
			sendSW("CLOSE_FIN");
			message = "ปิด พัดลมดูดอากาศ เข้า";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void OnOff_R_Fout() {
		if(r_fout) {
			sendSW("OPEN_FOUT");
			message = "เปิด พัดลมดูดอากาศ ออก";
		} else {
			sendSW("CLOSE_FOUT");
			message = "ปิด พัดลมดูดอากาศ ออก";
		}
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void Open_All() {
		sendSW("OPEN_ALL");
		message = "เปิด หมด";
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	public void Close_All() {
		sendSW("CLOSE_ALL");
		message = "ปิด หมด";
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Successful",  " Topic : " + topic + " Message : " + message) );
	}
	
	private void sendSW(String pubMsg) {
		// setup MQTT Client
		String clientID = M2MIO_THING;
		connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		
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
				
				//System.out.println("Publishing to topic \"" + topic + "\" qos " + qos + " Message " + pubMsg);
				MqttDeliveryToken token = null;
				MqttTopic topicControl = myClient.getTopic(topic);
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
				e.printStackTrace();
//				System.exit(-1);
			}
		}
	}
	
	public void onOff() {
		if(house.isSw1()) {
			sendSW("SW_1_ON");
		} else {
			sendSW("SW_1_OFF");
		}
		session.updateHouse(house);
	}

	public List<House> getHouses() {
		return houses;
	}

	public void setHouses(List<House> houses) {
		this.houses = houses;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public long getSelectedHouseId() {
		return selectedHouseId;
	}

	public void setSelectedHouseId(long selectedHouseId) {
		this.selectedHouseId = selectedHouseId;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}

	public boolean isR_pin() {
		return r_pin;
	}

	public void setR_pin(boolean r_pin) {
		this.r_pin = r_pin;
	}

	public boolean isR_pout() {
		return r_pout;
	}

	public void setR_pout(boolean r_pout) {
		this.r_pout = r_pout;
	}

	public boolean isR_fin() {
		return r_fin;
	}

	public void setR_fin(boolean r_fin) {
		this.r_fin = r_fin;
	}

	public boolean isR_frel() {
		return r_frel;
	}

	public void setR_frel(boolean r_frel) {
		this.r_frel = r_frel;
	}

	public boolean isR_sw_1() {
		return r_sw_1;
	}

	public void setR_sw_1(boolean r_sw_1) {
		this.r_sw_1 = r_sw_1;
	}

	public boolean isR_alert() {
		return r_alert;
	}

	public void setR_alert(boolean r_alert) {
		this.r_alert = r_alert;
	}

	public boolean isR_fout() {
		return r_fout;
	}

	public void setR_fout(boolean r_fout) {
		this.r_fout = r_fout;
	}

	public boolean isR_openall() {
		return r_openall;
	}

	public void setR_openall(boolean r_openall) {
		this.r_openall = r_openall;
	}

	public boolean isR_closeall() {
		return r_closeall;
	}

	public void setR_closeall(boolean r_closeall) {
		this.r_closeall = r_closeall;
	}
	
}
