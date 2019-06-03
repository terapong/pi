package toto.pi3.ejb.entity;

import java.io.Serializable;
import java.util.Date;

public class MushRoomLevel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private Date updateDate;

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
}
