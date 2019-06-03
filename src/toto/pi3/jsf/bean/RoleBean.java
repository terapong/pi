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

import toto.pi3.ejb.entity.Role;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "rolebean")
@ViewScoped
public class RoleBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Role> slave;
	private Role selectedRow;
	private Calendar cal;
	
	private String renderedDelete;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		slave = session.querryAllRole();
		for(Role r : slave) {
			if(r.getUsers().isEmpty()) {
				r.setRenderedDelete("true");
			} else {
				r.setRenderedDelete("false");
			}
		}
	}

	@PreDestroy
	private void destroy() {
		
	}
	
	public void btnNewClick() {
		selectedRow = new Role();
		selectedRow.setUpdateDate(cal.getTime());
	}
	
	public void btnSaveClick() {
		session.updateRole(selectedRow);
		init();
	}
	
	public void btnEditClick(Role o) {
		selectedRow = o;
	}
	
	public void confirmDeleteClick() {
		try {
			session.deleteRole(selectedRow);
			init();
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary("ไม่สามารถ ลบ ได้");
			msg.setDetail("ไม่สามารถ ลบ ได้ เพราะมี ข้อมูลที่เกี่ยวข้อง");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void btnDeleteClick(Role o) {
		selectedRow = o;
	}

	public List<Role> getSlave() {
		return slave;
	}

	public void setSlave(List<Role> slave) {
		this.slave = slave;
	}

	public Role getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(Role selectedRow) {
		this.selectedRow = selectedRow;
	}

	public String getRenderedDelete() {
		return renderedDelete;
	}

	public void setRenderedDelete(String renderedDelete) {
		this.renderedDelete = renderedDelete;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}

}
