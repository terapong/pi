package toto.pi3.ejb.session;

import java.io.Serializable;
import java.util.List;
import java.util.logging.*;

import javax.ejb.*;
import toto.pi3.ejb.entity.*;
import javax.persistence.*;

@Stateless
@LocalBean
public class Pi3Session implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final transient Logger log = Logger.getLogger("Pi3Session");
	
	@PersistenceContext(unitName="pi3PU")
	EntityManager em;

	public List<Role> querryAllRole() {
		return em.createNativeQuery("select * from role", Role.class).getResultList();
	}
	
	public Role querryRoleById(long id) {
		return em.find(Role.class, id);
	}
	
	public void updateRole(Role r) {
		em.merge(r);
	}
	
	public void deleteRole(Role r) throws Exception {
		r = querryRoleById(r.getId());
		em.remove(r);
	}
	
	public List<User> querryAllUser() {
		return em.createNativeQuery("select * from user", User.class).getResultList(); 
	}
	
	public List<User> querryUserByRoleID(long id) {
		return em.createNativeQuery("select * from user where role_id = " + id, User.class).getResultList();
	}
	
	public User querryUserById(long id) {
		return em.find(User.class, id );
		
	}
	
	public void updateUser(User u) {
		em.merge(u);
	}
	
	public void deleteUser(User u) throws Exception {
		u = querryUserById(u.getId());
		em.remove(u);
	}
	
	public List<Topic> querryAllTopic() {
		return em.createNativeQuery("select * from topic", Topic.class).getResultList();
	}
	
	public Topic querryTopicById(long id) {
		return em.find(Topic.class, id);
	}
	
	public void updateTopic(Topic t) {
		em.merge(t);
	}
	
	public void deleteTopic(Topic t) throws Exception {
		t = querryTopicById(t.getId());
		em.remove(t);;
	}
	
	public List<MushRoom> querryAllMushRoom() {
		return em.createNativeQuery("select * from mushroom", MushRoom.class).getResultList();
	}
	
	public MushRoom querryMushRoomById(long id) {
		return em.find(MushRoom.class, id);
	}
	
	public void updateMushRoom(MushRoom m) {
		em.merge(m);
	}
	
	public void deleteMushRoom(MushRoom m) throws Exception {
		m = querryMushRoomById(m.getId());
		em.remove(m);
	}
	
	public List<Message> querryAllMessage() {
		return em.createNativeQuery("select * from message", Message.class).getResultList();
	}
	
	public Message querryMessageById(long id) {
		return em.find(Message.class, id);
	}
	
	public Message queryMessageByTopicId(long id) {
		List<Message> ms = em.createNativeQuery("select * from message where topic_id = " + id, Message.class).getResultList();
		if(!ms.isEmpty()) {
			return ms.get(0);
		}
		return null; 
	}
	
	public List<MessageHistory> querryMessageHistoryLimit(int l, long topic_id) {
		return em.createNativeQuery("SELECT * FROM (SELECT * FROM messagehistory where topic_id = " + topic_id + " ORDER BY id DESC LIMIT " + l + ") sub ORDER BY id ASC", MessageHistory.class).getResultList();
	}
	
	public List<MessageHistory> queryMessageHistoryTest() {
		return em.createNativeQuery("select * from messagehistory limit 10", MessageHistory.class).getResultList();
	}
	
	public List<NodeMCU> queryAllNodeMCU() {
		return em.createNativeQuery("select * from nodemcu", NodeMCU.class).getResultList();
	}
	
	public NodeMCU queryNodeMCUById(long id) {
		return em.find(NodeMCU.class, id);
	}
	
	public List<NodeMCU> queryNodeMCUNoUse() {
		return em.createNativeQuery("select * from nodemcu where id not in (select nodemcu_id from house)", NodeMCU.class).getResultList();
	}
	
	public void updateNodeMCU(NodeMCU n, String mode) {
		List<Topic> ts = n.getTopics();
		n = em.merge(n);
		if(mode.equals("insert")) {
			for(Topic t : ts) {
				t.setName(n.getId() + "/" + t.getName());
				t.setNodemcu(n);
				updateTopic(t);
			}
		}
	}
	
	public void deleteNodeMCU(NodeMCU n) throws Exception {
		n = queryNodeMCUById(n.getId());
		em.remove(n);
	}
	
	
	public List<MushRoomLevel> queryAllMushRoomLevel() {
		return em.createNativeQuery("select * from mushroomlevel", MushRoomLevel.class).getResultList();
	}
	
	public MushRoomLevel queryMushRoomLevelById(long id) {
		return em.find(MushRoomLevel.class, id);
	}
	
	public void updateMushRoomLevel(MushRoomLevel m) {
		em.merge(m);
	}
	
	public void deleteMushRoomLevel(MushRoomLevel m) throws Exception {
		m = queryMushRoomLevelById(m.getId());
		em.remove(m);
	}
	
	public List<House> queryAllHouse() {
		return em.createNativeQuery("select * from house", House.class).getResultList();
	}
	
	public House queryHouseById(long id) {
		return em.find(House.class, id);
	}
	
	public void updateHouse(House h) {
		em.merge(h);
	}
	
	public void deleteHouse(House h) throws Exception {
		h = queryHouseById(h.getId());
		em.remove(h);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
