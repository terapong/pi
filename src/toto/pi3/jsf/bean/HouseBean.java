package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import toto.pi3.ejb.entity.House;
import toto.pi3.ejb.entity.MushRoom;
import toto.pi3.ejb.entity.NodeMCU;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "housebean")
@ViewScoped
public class HouseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<House> slave;
	private House selectedRow;
	private List<MushRoom> mushRooms;
	private long mushRoomId;
//	private List<MushRoomLevel> mushRoomLevels;
//	private long mushRoomLeveId;
	private List<NodeMCU> nodeMCUs;
	private long nodeMCUId;
	private Calendar cal;
	
	private String disabledAdd;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		slave = session.queryAllHouse();
		mushRooms = session.querryAllMushRoom();
//		mushRoomLevels = session.queryAllMushRoomLevel();
		nodeMCUs = session.queryNodeMCUNoUse();
		if(mushRooms.isEmpty() || nodeMCUs.isEmpty()) {
			disabledAdd = "true";
		} else {
			disabledAdd = "fase";
			
		}
	}

	@PreDestroy
	private void destroy() {
		
	}

	public void btnNewClick() {
		nodeMCUs = session.queryNodeMCUNoUse();
		selectedRow = new House();
		mushRoomId = mushRooms.get(0).getId();
		//mushRoomLeveId = mushRoomLevels.get(0).getId();
		nodeMCUId = nodeMCUs.get(0).getId();
		selectedRow.setMushRoom(mushRooms.get(0));
		//selectedRow.setMushRoomLevel(mushRoomLevels.get(0));
		selectedRow.setNodeMCU(nodeMCUs.get(0));
		selectedRow.setUpdateDate(cal.getTime());
	}
	
	public void btnSaveClick() {
		session.updateHouse(selectedRow);
		init();
	}
	
	public void btnEditClick(House o) {
		selectedRow = o;
		nodeMCUs = session.queryNodeMCUNoUse();
		nodeMCUs.add(o.getNodeMCU());
		mushRoomId = selectedRow.getMushRoom().getId();
		//mushRoomLeveId = selectedRow.getMushRoomLevel().getId();
		nodeMCUId = selectedRow.getNodeMCU().getId();
	}
	
	public void confirmDeleteClick() {
		try {
			session.deleteHouse(selectedRow);
			init();
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary("ไม่สามารถ ลบ ได้");
			msg.setDetail("ไม่สามารถ ลบ ได้ เพราะมี ข้อมูลที่เกี่ยวข้อง");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void btnDeleteClick(House o) {
		selectedRow = o;
	}
	
	public void selMushRoomChange() {
		selectedRow.setMushRoom(session.querryMushRoomById(mushRoomId));
	}
	
//	public void selMushRoomLevelChange() {
//		selectedRow.setMushRoomLevel(session.queryMushRoomLevelById(mushRoomLeveId));
//	}
	
	public void selNodeMCUChange() {
		selectedRow.setNodeMCU(session.queryNodeMCUById(nodeMCUId));
	}

	public List<House> getSlave() {
		return slave;
	}

	public void setSlave(List<House> slave) {
		this.slave = slave;
	}

	public House getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(House selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}

	public List<MushRoom> getMushRooms() {
		return mushRooms;
	}

	public void setMushRooms(List<MushRoom> mushRooms) {
		this.mushRooms = mushRooms;
	}

	public long getMushRoomId() {
		return mushRoomId;
	}

	public void setMushRoomId(long mushRoomId) {
		this.mushRoomId = mushRoomId;
	}

	public String getDisabledAdd() {
		return disabledAdd;
	}

	public void setDisabledAdd(String disabledAdd) {
		this.disabledAdd = disabledAdd;
	}

	public List<NodeMCU> getNodeMCUs() {
		return nodeMCUs;
	}

	public void setNodeMCUs(List<NodeMCU> nodeMCUs) {
		this.nodeMCUs = nodeMCUs;
	}

	public long getNodeMCUId() {
		return nodeMCUId;
	}

	public void setNodeMCUId(long nodeMCUId) {
		this.nodeMCUId = nodeMCUId;
	}
}
