package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.*;
import org.primefaces.event.CloseEvent;

import toto.pi3.ejb.entity.House;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "menubean")
@ViewScoped
public class MenuBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;

	@EJB private Pi3Session session;
	
	public String manageHouseClick() {
		pi3sessionbean.setProgramName("จัดการ โรงเรือน");
		pi3sessionbean.setContentCenter("manageHouse.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String handleClose(CloseEvent event) {
		List<House> hs = session.queryAllHouse();
		if(hs.isEmpty()) {
			pi3sessionbean.setContentCenter("blank.xhtml");
		} else {
			pi3sessionbean.setContentCenter("manageHouse.xhtml");
		}
		return "main?facesRedirect=true";
	}
	
	public String messageHistoryClick() {
		pi3sessionbean.setProgramName("ประวัติ");
		pi3sessionbean.setContentCenter("messageHistory.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String roleClick() {
		pi3sessionbean.setProgramName("ข้อมูล กลุ่ม");
		pi3sessionbean.setContentCenter("admin/role.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String userClick() {
		pi3sessionbean.setProgramName("ข้อมูล ผู้ใช้งาน");
		pi3sessionbean.setContentCenter("admin/user.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String nodeMCUClick() {
		pi3sessionbean.setProgramName("ข้อมูล NodeMCU");
		pi3sessionbean.setContentCenter("admin/nodeMCU.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String houseClick() {
		pi3sessionbean.setProgramName("ข้อมูล โรงเรือน");
		pi3sessionbean.setContentCenter("admin/house.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String mushRoomClick() {
		pi3sessionbean.setProgramName("ช้อมูล เห็ด");
		pi3sessionbean.setContentCenter("admin/mushRoom.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String mushRoomLevelClick() {
		pi3sessionbean.setProgramName("ช้อมูล ระยะเห็ด");
		pi3sessionbean.setContentCenter("admin/mushRoomLevel.xhtml");
		return "main?facesRedirect=true";
	}
	
	public String topicClick() {
		pi3sessionbean.setProgramName("ช้อมูล topic");
		pi3sessionbean.setContentCenter("admin/topic.xhtml");
		return "main?facesRedirect=true";
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}
}