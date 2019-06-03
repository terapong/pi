package pisub;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ManageTopic implements Runnable {
	static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	static final String MYSQL_URL = "jdbc:mysql://localhost:3306/pi3?autoReconnect=true";
	static final String MYSQL_USER = "root";
	static final String MYSQL_PASS = "xxxxxx";
	private String topic;
	private MqttMessage message;
	
	public ManageTopic(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
	}
	
	public void run() {
		try {
			String rgb = "";
			Class.forName(MYSQL_DRIVER);
			Connection conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
			String sql = "select * from topic where name = '" + topic + "'";
			PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			rs.first();
			long topicId = rs.getLong("id");
			long nodeMCUId = rs.getLong("nodemcu_id");
			sql = "select * from message where topic_id = " + topicId;
			stm = conn.prepareStatement(sql);
			rs = stm.executeQuery();
			
			if(rs.first()) {
				sql = "update message set message = " + new BigDecimal(new String(message.getPayload())) + ", update_date = now(), rgb = '" + rgb + "' where topic_id = " + topicId;
			
			} else {
				sql = "insert into message (message, update_date, topic_id, rgb) values(" + new BigDecimal(new String(message.getPayload())) + ", now(), " + topicId + ", '" + rgb + "' )";
			}
			
			stm = conn.prepareStatement(sql);
			stm.executeUpdate();			
			
			sql = "insert into messagehistory (message, update_date, topic_id) values(" + new BigDecimal(new String(message.getPayload())) + ", now(), " + topicId + ")";
			stm = conn.prepareStatement(sql);
			stm.executeUpdate();
			
			System.out.println("Topic = " + topic + " Message = " + new String(message.getPayload()));
			rs.close();
			stm.close();
			conn.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
