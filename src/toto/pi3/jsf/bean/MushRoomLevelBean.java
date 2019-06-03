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

import toto.pi3.ejb.entity.MushRoomLevel;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "mushroomlevelbean")
@ViewScoped
public class MushRoomLevelBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<MushRoomLevel> slave;
	private MushRoomLevel selectedRow;
	private Calendar cal;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		slave = session.queryAllMushRoomLevel();
	}

	@PreDestroy
	private void destroy() {
		
	}

	public void btnNewClick() {
		selectedRow = new MushRoomLevel();
		selectedRow.setUpdateDate(cal.getTime());
	}
	
	public void btnSaveClick() {
		session.updateMushRoomLevel(selectedRow);
		init();
	}
	
	public void btnEditClick(MushRoomLevel o) {
		selectedRow = o;
	}
	
	public void confirmDeleteClick() {
		try {
			session.deleteMushRoomLevel(selectedRow);
			init();
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary("ไม่สามารถ ลบ ได้");
			msg.setDetail("ไม่สามารถ ลบ ได้ เพราะมี ข้อมูลที่เกี่ยวข้อง");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void btnDeleteClick(MushRoomLevel o) {
		selectedRow = o;
	}

	public List<MushRoomLevel> getSlave() {
		return slave;
	}

	public void setSlave(List<MushRoomLevel> slave) {
		this.slave = slave;
	}

	public MushRoomLevel getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(MushRoomLevel selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}
	
}
