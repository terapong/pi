package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import toto.pi3.ejb.entity.House;
import toto.pi3.ejb.entity.User;
import toto.pi3.ejb.session.Pi3Session;
import toto.util.FWUtil;

@ManagedBean(name = "indexbean")
@ViewScoped
public class IndexBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final ResourceBundle resources = ResourceBundle.getBundle("resources.Index");
	private static final ResourceBundle indexR = ResourceBundle.getBundle("resources.Index");
	private User user;
	private long userId;
	private List<User> users;
	private String username;
	private String password;
	private String oldPass;
	private String newPass;
	private String newPassAgain;
	private Calendar cal;
	
	@ManagedProperty(value = "#{pi3sessionbean}")
	private Pi3SessionBean pi3sessionbean;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		cal = Calendar.getInstance();
		users = session.querryAllUser();
		if(users.size() != 0) {
			user = users.get(0);
			username = user.getName();
			pi3sessionbean.setUser(user);
			pi3sessionbean.setUsername(username);
			userId = user.getId();
		}
	}
	
	@PreDestroy
	private void destroy() {
		
	}
	
	public void selUserChange() {
		user = session.querryUserById(userId);
		pi3sessionbean.setUser(user);
		pi3sessionbean.setUsername(username);
	}
	
	public String loginClick() {
		try {
			if(user != null) {
				password = FWUtil.byteArrayToHexString(FWUtil.computeHash(password));
				 
				if(password.equals(user.getPassword())) {
					user.setUpdateDate(cal.getTime());
					session.updateUser(user);
					List<House> hs = session.queryAllHouse();
					if(hs.isEmpty()) {
						pi3sessionbean.setContentCenter("blank.xhtml");
					} else {
						pi3sessionbean.setContentCenter("manageHouse.xhtml");
					}
					
					return "main.jsf";
				} else {
					FacesMessage msg = new FacesMessage();
					msg.setSummary(resources.getString("LoginFail.Summary"));
					msg.setDetail(resources.getString("LoginFail.Description"));
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(null, msg);	
				}
			} else {
				FacesMessage msg = new FacesMessage();
				msg.setSummary(resources.getString("LoginFail.Summary"));
				msg.setDetail(resources.getString("LoginFail.Description"));
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage();
			msg.setSummary(resources.getString("LoginFail.Summary"));
			msg.setDetail(resources.getString("LoginFail.Description"));
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			ex.printStackTrace();
		}
		return "null";
	}
	
	public String logout() {
		user = pi3sessionbean.getUser();
		FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed!"));
        } 
        context.getExternalContext().invalidateSession();
        return "index?facesRedirect=true";
    }
	
	public void changPassClick() {
		user = pi3sessionbean.getUser();
		try {
			String password = FWUtil.byteArrayToHexString(FWUtil.computeHash(oldPass));
			if(user.getPassword().compareTo(password) == 0) {
				if(newPass.compareTo(newPassAgain) == 0) {
					user.setPassword(FWUtil.byteArrayToHexString(FWUtil.computeHash(newPass)));
					session.updateUser(user);
				} else {
					FacesMessage msg = new FacesMessage(indexR.getString("ChangePassword.NewConfirmMismatch"));
		    		FacesContext.getCurrentInstance().addMessage(null, msg);
		    		FacesContext.getCurrentInstance().validationFailed();
				}
			} else {
				FacesMessage msg = new FacesMessage(indexR.getString("ChangePassword.OldIncorrect"));
	    		FacesContext.getCurrentInstance().addMessage(null, msg);
	    		FacesContext.getCurrentInstance().validationFailed();
			}
		} catch(Exception ex) {
			FacesMessage msg = new FacesMessage(indexR.getString("ChangePassword.SystemFailure"));
    		FacesContext.getCurrentInstance().addMessage(null, msg);
    		FacesContext.getCurrentInstance().validationFailed();
			ex.printStackTrace();
		}
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getNewPassAgain() {
		return newPassAgain;
	}

	public void setNewPassAgain(String newPassAgain) {
		this.newPassAgain = newPassAgain;
	}

	public Pi3SessionBean getPi3sessionbean() {
		return pi3sessionbean;
	}

	public void setPi3sessionbean(Pi3SessionBean pi3sessionbean) {
		this.pi3sessionbean = pi3sessionbean;
	}

}
