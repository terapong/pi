package toto.pi3.ejb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="house")
public class House implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private String name;
	
	@Column(name = "isopenpout")
	private boolean isOpenPout = false;
	
	@Column(name = "isopenfin")
	private boolean isOpenFin = false;
	
	@Column(name = "isopenfout")
	private boolean ispenFout = false;
	
	@Column(name = "isopenpin")
	private boolean isOpenPin = false;
	
	@Column(name = "isopenfrel")
	private boolean isOpenFrel = false;
	
	@Column(name = "isopenalert")
	private boolean isOpenAlert = false;
	
	@Column(name="update_date")
	@Temporal(value= TemporalType.TIMESTAMP)
	private Date updateDate;

	@OneToOne @JoinColumn(name = "mushroomlevel_id")
	private MushRoomLevel mushRoomLevel;
	
	@OneToOne @JoinColumn(name = "mushroom_id")
	private MushRoom mushRoom;
	
	@OneToOne(fetch = FetchType.EAGER) @JoinColumn(name = "nodemcu_id")
	private NodeMCU nodeMCU;
	
	@Column(name="sw_1")
	private boolean sw1 = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public MushRoomLevel getMushRoomLevel() {
		return mushRoomLevel;
	}

	public void setMushRoomLevel(MushRoomLevel mushRoomLevel) {
		this.mushRoomLevel = mushRoomLevel;
	}

	public MushRoom getMushRoom() {
		return mushRoom;
	}

	public void setMushRoom(MushRoom mushRoom) {
		this.mushRoom = mushRoom;
	}

	public NodeMCU getNodeMCU() {
		return nodeMCU;
	}

	public void setNodeMCU(NodeMCU nodeMCU) {
		this.nodeMCU = nodeMCU;
	}

	public boolean isOpenPout() {
		return isOpenPout;
	}

	public void setOpenPout(boolean isOpenPout) {
		this.isOpenPout = isOpenPout;
	}

	public boolean isOpenFin() {
		return isOpenFin;
	}

	public void setOpenFin(boolean isOpenFin) {
		this.isOpenFin = isOpenFin;
	}

	public boolean isIspenFout() {
		return ispenFout;
	}

	public void setIspenFout(boolean ispenFout) {
		this.ispenFout = ispenFout;
	}

	public boolean isOpenPin() {
		return isOpenPin;
	}

	public void setOpenPin(boolean isOpenPin) {
		this.isOpenPin = isOpenPin;
	}

	public boolean isOpenFrel() {
		return isOpenFrel;
	}

	public void setOpenFrel(boolean isOpenFrel) {
		this.isOpenFrel = isOpenFrel;
	}

	public boolean isOpenAlert() {
		return isOpenAlert;
	}

	public void setOpenAlert(boolean isOpenAlert) {
		this.isOpenAlert = isOpenAlert;
	}

	public boolean isSw1() {
		return sw1;
	}

	public void setSw1(boolean sw1) {
		this.sw1 = sw1;
	}
	
}
