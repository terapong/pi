package toto.pi3.jsf.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import toto.pi3.ejb.entity.*;
import toto.pi3.ejb.session.Pi3Session;

@ManagedBean(name = "topicbean")
@ViewScoped
public class TopicBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Topic> topics;
	
	@EJB private Pi3Session session;
	
	@PostConstruct
	private void init() {
		topics = session.querryAllTopic();
	}
	
	@PreDestroy
	private void destroy() {
		
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
}
