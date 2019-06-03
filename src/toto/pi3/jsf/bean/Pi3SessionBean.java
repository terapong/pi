package toto.pi3.jsf.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import toto.pi3.ejb.entity.User;

@ManagedBean(name = "pi3sessionbean")
@SessionScoped
public class Pi3SessionBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private User user;
	private String username;
	private String programName;
	private String contentCenter = "manageHouse.xhtml";
	
	@PostConstruct
	public void init() {
	}
	
	@PreDestroy
	public void destroy() {
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContentCenter() {
		return contentCenter;
	}
	public void setContentCenter(String contentCenter) {
		this.contentCenter = contentCenter;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	
}
