package toto.pi3.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="topic")
public class Topic implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length = 50)
	private String name;
	
	@Column(name="update_date")
	@Temporal(value= TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@ManyToOne @JoinColumn(name = "nodemcu_id")
	private NodeMCU nodemcu;
	
	@OneToMany(mappedBy = "topic")
	private List<Message> messages;
	
	@OneToMany(mappedBy = "topic")
	private List<MessageHistory> messageHistories;
	
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

	public NodeMCU getNodemcu() {
		return nodemcu;
	}

	public void setNodemcu(NodeMCU nodemcu) {
		this.nodemcu = nodemcu;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<MessageHistory> getMessageHistories() {
		return messageHistories;
	}

	public void setMessageHistories(List<MessageHistory> messageHistories) {
		this.messageHistories = messageHistories;
	}
}
