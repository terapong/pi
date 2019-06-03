package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.primefaces.event.SlideEndEvent;

import toto.pi3.ejb.entity.MushRoom;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "mushroombean")
@ViewScoped
public class MushRoomBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<MushRoom> slave;
	private MushRoom selectedRow;
	private Calendar cal;
	
	private String renderedDelete;
	
	@ManagedProperty(value = "#{indexbean}")
	private IndexBean indexbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		slave = session.querryAllMushRoom();
	}

	@PreDestroy
	private void destroy() {
		
	}
	
	public void btnNewClick() {
		selectedRow = new MushRoom();
		selectedRow.setUpdateDate(cal.getTime());
	}
	
	public void btnSaveClick() {
		session.updateMushRoom(selectedRow);
		init();
	}
	
	public void btnEditClick(MushRoom o) {
		selectedRow = o;
	}
	
	public void confirmDeleteClick() {
		try {
			session.deleteMushRoom(selectedRow);
			init();
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary("ไม่สามารถ ลบ ได้");
			msg.setDetail("ไม่สามารถ ลบ ได้ เพราะมี ข้อมูลที่เกี่ยวข้อง");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void btnDeleteClick(MushRoom o) {
		selectedRow = o;
	}
	
	public void onSlideTempMinEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getTemperatureMax()) > 0)) {
				selectedRow.setTemperatureMin(selectedRow.getTemperatureMax());
			} else {
				selectedRow.setTemperatureMin(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setTemperatureMin(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideTempOutMinEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getTemperatureOutMax()) > 0)) {
				selectedRow.setTemperatureOutMin(selectedRow.getTemperatureOutMax());
			} else {
				selectedRow.setTemperatureOutMin(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setTemperatureOutMin(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideTempMaxEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getTemperatureMin()) < 0)) {
				selectedRow.setTemperatureMax(selectedRow.getTemperatureMin());
			} else {
				selectedRow.setTemperatureMax(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setTemperatureMax(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideTempOutMaxEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getTemperatureOutMin()) < 0)) {
				selectedRow.setTemperatureOutMax(selectedRow.getTemperatureOutMin());
			} else {
				selectedRow.setTemperatureOutMax(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setTemperatureOutMax(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideHumMinEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getHumidityMax()) > 0)) {
				selectedRow.setHumidityMin(selectedRow.getHumidityMax());
			} else {
				selectedRow.setHumidityMin(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setHumidityMin(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideHumOutMinEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getHumidityOutMax()) > 0)) {
				selectedRow.setHumidityOutMin(selectedRow.getHumidityOutMax());
			} else {
				selectedRow.setHumidityOutMin(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setHumidityOutMin(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideHumMaxEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getHumidityMin()) < 0)) {
				selectedRow.setHumidityMax(selectedRow.getHumidityMin());
			} else {
				selectedRow.setHumidityMax(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setHumidityMax(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideHumOutMaxEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getHumidityOutMin()) < 0)) {
				selectedRow.setHumidityOutMax(selectedRow.getHumidityOutMin());
			} else {
				selectedRow.setHumidityOutMax(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setHumidityOutMax(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideCo2MinEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getCo2Max()) > 0)) {
				selectedRow.setCo2Min(selectedRow.getCo2Max());
			} else {
				selectedRow.setCo2Min(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setCo2Min(BigDecimal.valueOf(e.getValue()));
		}
	}
	
	public void onSlideCo2MaxEnd(SlideEndEvent e) {
		try {
			if((BigDecimal.valueOf(e.getValue()).compareTo(selectedRow.getCo2Min()) < 0)) {
				selectedRow.setCo2Max(selectedRow.getCo2Min());
			} else {
				selectedRow.setCo2Max(BigDecimal.valueOf(e.getValue()));
			}
		} catch(Exception ex) {
			selectedRow.setCo2Max(BigDecimal.valueOf(e.getValue()));
		}
	}

	public List<MushRoom> getSlave() {
		return slave;
	}

	public void setSlave(List<MushRoom> slave) {
		this.slave = slave;
	}

	public MushRoom getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(MushRoom selectedRow) {
		this.selectedRow = selectedRow;
	}

	public String getRenderedDelete() {
		return renderedDelete;
	}

	public void setRenderedDelete(String renderedDelete) {
		this.renderedDelete = renderedDelete;
	}

	public IndexBean getIndexbean() {
		return indexbean;
	}

	public void setIndexbean(IndexBean indexbean) {
		this.indexbean = indexbean;
	}
}