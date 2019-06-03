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

import toto.pi3.ejb.entity.*;
import toto.pi3.ejb.session.Pi3Session;
import toto.util.FWUtil;

@ManagedBean(name = "userbean")
@ViewScoped
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Role> master;
	private Role selectedMaster;
	private long selectedMasterId;
	private List<User> slave;
	private User selectedRow;
	
	private String addDisabled;
	private String tempPass;
	
	private Calendar cal;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		master = session.querryAllRole();
		if(master.isEmpty()) {
			addDisabled = "true";
		} else {
			addDisabled = "false";
			if(selectedMaster == null) {
				selectedMaster = master.get(0);
			}
			slave = session.querryUserByRoleID(selectedMaster.getId());
			selectedMasterId = selectedMaster.getId();
		}
	}
	
	@PreDestroy
	private void destroy() {
		
	}
	
	public void btnNewClick() {
		selectedRow = new User();
		selectedRow.setRole(selectedMaster);
		selectedRow.setUpdateDate(cal.getTime());
	}
	
	public void btnSaveClick() {
		try {
			if(tempPass != null || !tempPass.equals("")) {
				tempPass = FWUtil.byteArrayToHexString(FWUtil.computeHash(tempPass));
			}
			
			if(!tempPass.equals(selectedRow.getPassword())) {
				selectedRow.setPassword(tempPass);
			}
			tempPass = null;
			session.updateUser(selectedRow);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		init();
	}
	
	public void btnEditClick(User o) {
		selectedRow = o;
	}
	
	public void confirmDeleteClick() {
		try {
			session.deleteUser(selectedRow);
			init();
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary("ไม่สามารถ ลบ ได้");
			msg.setDetail("ไม่สามารถ ลบ ได้ เพราะมี ข้อมูลที่เกี่ยวข้อง");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void btnDeleteClick(User o) {
		selectedRow = o;
	}
	
	public void selMasterChange() {
		selectedMaster = session.querryRoleById(selectedMasterId);
		slave = session.querryUserByRoleID(selectedMasterId);
	}

	public List<Role> getMaster() {
		return master;
	}

	public void setMaster(List<Role> master) {
		this.master = master;
	}

	public Role getSelectedMaster() {
		return selectedMaster;
	}

	public void setSelectedMaster(Role selectedMaster) {
		this.selectedMaster = selectedMaster;
	}

	public List<User> getSlave() {
		return slave;
	}

	public void setSlave(List<User> slave) {
		this.slave = slave;
	}

	public User getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(User selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}

	public String getAddDisabled() {
		return addDisabled;
	}

	public void setAddDisabled(String addDisabled) {
		this.addDisabled = addDisabled;
	}

	public long getSelectedMasterId() {
		return selectedMasterId;
	}

	public void setSelectedMasterId(long selectedMasterId) {
		this.selectedMasterId = selectedMasterId;
	}

	public String getTempPass() {
		return tempPass;
	}

	public void setTempPass(String tempPass) {
		this.tempPass = tempPass;
	}
}
