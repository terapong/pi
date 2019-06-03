package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import toto.pi3.ejb.entity.NodeMCU;
import toto.pi3.ejb.entity.Topic;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "nodemcubean")
@ViewScoped
public class NodeMCUBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<NodeMCU> slave;
	private NodeMCU selectedRow;
	private Calendar cal;
	private String mode = "insert";
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		slave = session.queryAllNodeMCU();
		mode = "insert";
	}

	@PreDestroy
	private void destroy() {
		
	}

	public void btnNewClick() {
		selectedRow = new NodeMCU();
		List<Topic> ts = new ArrayList<Topic>();
		Topic t1 = new Topic();
		Topic t2 = new Topic();
		Topic t3 = new Topic();
		Topic t4 = new Topic();
		Topic t5 = new Topic();
		Topic t6 = new Topic();
		Topic t7 = new Topic();
		Topic t8 = new Topic();
		
		
		t1.setName("pi3/pub/temp/in");
		t1.setUpdateDate(cal.getTime());
		t2.setName("pi3/pub/temp/out");
		t2.setUpdateDate(cal.getTime());
		t3.setName("pi3/pub/hum/in");
		t3.setUpdateDate(cal.getTime());
		t4.setName("pi3/pub/hum/out");
		t4.setUpdateDate(cal.getTime());
//		t5.setName("pi3/pub/co2/in");
//		t5.setUpdateDate(cal.getTime());
//		t6.setName("pi3/pub/co2/out");
//		t6.setUpdateDate(cal.getTime());
		t7.setName("pi3/sub");
		t7.setUpdateDate(cal.getTime());
		t8.setName("pi3");
		t8.setUpdateDate(cal.getTime());
		
		ts.add(t1);
		ts.add(t2);
		ts.add(t3);
		ts.add(t4);
//		ts.add(t5);
//		ts.add(t6);
		ts.add(t7);
		ts.add(t8);
		
		selectedRow.setTopics(ts);
		selectedRow.setUpdateDate(cal.getTime());
		mode = "insert";
	}
	
	public void btnSaveClick() {
		session.updateNodeMCU(selectedRow, mode);
		init();
	}
	
	public void btnEditClick(NodeMCU o) {
		selectedRow = o;
		mode = "update";
	}
	
	public void confirmDeleteClick() {
		try {
			session.deleteNodeMCU(selectedRow);
			init();
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary("ไม่สามารถ ลบ ได้");
			msg.setDetail("ไม่สามารถ ลบ ได้ เพราะมี ข้อมูลที่เกี่ยวข้อง");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void btnDeleteClick(NodeMCU o) {
		selectedRow = o;
	}

	public List<NodeMCU> getSlave() {
		return slave;
	}

	public void setSlave(List<NodeMCU> slave) {
		this.slave = slave;
	}

	public NodeMCU getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(NodeMCU selectedRow) {
		this.selectedRow = selectedRow;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}
}
