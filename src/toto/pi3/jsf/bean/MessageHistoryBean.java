package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import toto.pi3.ejb.entity.*;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "messagehistorybean")
@ViewScoped
public class MessageHistoryBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private LineChartModel lineModelIn;
	private List<House> houses;
	private House house;
	private long selectedHouseId;
	private List<Topic> topics;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	public void init() {
		houses = session.queryAllHouse();
		if(!houses.isEmpty()) {
			house = houses.get(0);
			selectedHouseId = house.getId();
		}
		topics = house.getNodeMCU().getTopics();
		createLineModels();
	}
	
	private void createLineModels() {
        lineModelIn = initLinearModel("/temp/in", "/hum/in", "", "/temp/out", "/hum/out");
    }
	
    private LineChartModel initLinearModel(String subTopicTemp, String subTopicHum, String subTopicCo2, String subTopicTempOut, String subTopicHumOut) {
        LineChartModel model = new LineChartModel();
        List<MessageHistory> mhs = new ArrayList<MessageHistory>();
        LineChartSeries series1 = new LineChartSeries();
        LineChartSeries series2 = new LineChartSeries();
        LineChartSeries series3 = new LineChartSeries();
        LineChartSeries series4 = new LineChartSeries();
        
        series1.setLabel("Temperature ภายใน");
        series2.setLabel("Temperature ภายนอก");
        series3.setLabel("Humidity ภายใน");
        series4.setLabel("Humidity ภายนอก");
        int i;
        
        Message m = getMessageValue(subTopicTemp);
        if(m != null) {
        	mhs = session.querryMessageHistoryLimit(20, m.getTopic().getId());
        	i = 1;
        	if(!mhs.isEmpty()) {
        		for(MessageHistory mh : mhs) {
                	series1.set(i, mh.getMessage());
                	i++;
                }
        	} else {
        		series1.set(i, 0);
        	}
        }

        m = getMessageValue(subTopicTempOut);
        if(m != null) {
        	mhs = session.querryMessageHistoryLimit(20, m.getTopic().getId());
        	i = 1;
        	if(!mhs.isEmpty()) {
        		for(MessageHistory mh : mhs) {
                	series2.set(i, mh.getMessage());
                	i++;
                }
        	} else {
        		series2.set(i, 0);
        	}
        }
        
        m = getMessageValue(subTopicHum);
        if(m != null) {
        	mhs = session.querryMessageHistoryLimit(20, m.getTopic().getId());
        	i = 1;
        	if(!mhs.isEmpty()) {
        		for(MessageHistory mh : mhs) {
                	series3.set(i, mh.getMessage());
                	i++;
                }
        	} else {
        		series3.set(i, 0);
        	}
        }
        
        m = getMessageValue(subTopicHumOut);
        if(m != null) {
        	mhs = session.querryMessageHistoryLimit(20, m.getTopic().getId());
        	i = 1;
        	if(!mhs.isEmpty()) {
        		for(MessageHistory mh : mhs) {
                	series4.set(i, mh.getMessage());
                	i++;
                }
        	} else {
        		series4.set(i, 0);
        	}
        }
        
//        m = getMessageValue(subTopicCo2);
//        if(m != null) {
//        	mhs = session.querryMessageHistoryLimit(20, m.getTopic().getId());
//        	i = 1;
//            for(MessageHistory mh : mhs) {
//            	series3.set(i, mh.getMessage());
//            	i++;
//            }
//        }
        
        model.addSeries(series1);
        model.addSeries(series2);
        model.addSeries(series3);
        model.addSeries(series4);
        model.setLegendPosition("e");
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(100);
        return model;
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
    
    public void selHouseChange() {
		house = session.queryHouseById(selectedHouseId);
		topics = house.getNodeMCU().getTopics();
		createLineModels();
	}
    
    public void refreshMeter() {
		createLineModels();
	}

	public LineChartModel getLineModelIn() {
		return lineModelIn;
	}

	public void setLineModelIn(LineChartModel lineModelIn) {
		this.lineModelIn = lineModelIn;
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

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}
}
