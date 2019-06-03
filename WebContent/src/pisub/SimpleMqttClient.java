package pisub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SimpleMqttClient implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;

	static final String BROKER_URL = "tcp://192.168.1.114:1883";
	static final String M2MIO_DOMAIN = "";
	static final String M2MIO_STUFF = "";
	static final String M2MIO_THING = "terapong";
	static final String M2MIO_USERNAME = "";
	static final String M2MIO_PASSWORD_MD5 = "";
	
	static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	static final String MYSQL_URL = "jdbc:mysql://localhost:3306/pi3?autoReconnect=true";
	static final String MYSQL_USER = "root";
	static final String MYSQL_PASS = "xxxxxx";

	static final String temperatureTopicInPub = "1/pi3/pub/temp/in";
	static final String humidityTopicInPub = "1/pi3/pub/hum/in";
	
	static final String temperatureTopicOutPub = "1/pi3/pub/temp/out";
	static final String humidityTopicOutPub = "1/pi3/pub/hum/out";
	
	static final String node1Pub = "1/pi3/sub";
	
	

	// the following two flags control whether this example is a publisher, a
	// subscriber or both
	static final Boolean subscriber = true;
	static final Boolean publisher = true;

	/**
	 * 
	 * connectionLost This callback is invoked upon losing the MQTT connection.
	 * 
	 */
	@Override
	public void connectionLost(Throwable t) {
		System.out.println("Connection lost!");
		// code to reconnect to the broker would go here if desired
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		(new Thread(new ManageTopic(topic, message))).start();
	}
	
//	@Override
//	public void messageArrived(String topic, MqttMessage message) throws Exception {
//		
//		String sql = "select * from topic where name = '" + topic.split("pub/")[1] + "'";
//		long topic_id;
//		String rgb = "";
//		//System.out.println(sql);
//		PreparedStatement stm = conn.prepareStatement(sql);
//		ResultSet rs = stm.executeQuery();
//		rs.first();
//		topic_id = rs.getLong("id");
//		
//		sql = "select * from message where topic_id = " + topic_id;
//		stm = conn.prepareStatement(sql);
//		rs = stm.executeQuery();
//		
//		String sql_1 = "select m.* from topic t left outer join mushroom m on t.mushroom_id = m.id where t.id = " + topic_id;
//		PreparedStatement stm_1 = conn.prepareStatement(sql_1);
//		ResultSet rs_1 = stm_1.executeQuery();
//		rs_1.first();
//		BigDecimal min, max;
//		if(topic.equals(temperatureTopicInPub)) {
//			min = rs_1.getBigDecimal("temperaturemin");
//			max = rs_1.getBigDecimal("temperaturemax");
//			if((new BigDecimal(new String(message.getPayload()))).compareTo(min) < 0) {
//				rgb = "b";
//			} else if((new BigDecimal(new String(message.getPayload()))).compareTo(max) > 0) {
//				rgb = "r";
//			} else {
//				rgb = "g";
//			}
//		} else if(topic.equals(humidityTopicInPub)) {
//			min = rs_1.getBigDecimal("humiditymin");
//			max = rs_1.getBigDecimal("humiditymax");
//			if((new BigDecimal(new String(message.getPayload()))).compareTo(min) < 0) {
//				rgb = "b";
//			} else if((new BigDecimal(new String(message.getPayload()))).compareTo(max) > 0) {
//				rgb = "r";
//			} else {
//				rgb = "g";
//			}
//		}
//		
//		
//		if(rs.first()) {
//			sql = "update message set message = " + new BigDecimal(new String(message.getPayload())) + ", update_date = now(), rgb = '" + rgb + "' where topic_id = " + topic_id;
//			
//		} else {
//			sql = "insert into message (message, update_date, topic_id, rgb) values(" + new BigDecimal(new String(message.getPayload())) + ", now(), " + topic_id + ", '" + rgb + "' )";
//		}
//		
//		
//		
//		stm = conn.prepareStatement(sql);
//		stm.executeUpdate();
//		
//		sql = "insert into messagehistory (message, update_date, topic_id) values(" + new BigDecimal(new String(message.getPayload())) + ", now(), " + topic_id + ")";
//		stm = conn.prepareStatement(sql);
//		stm.executeUpdate();
//		
//		rs_1.close();
//		stm_1.close();
//		
//		rs.close();
//		stm.close();
//		conn.close();
//	}
	
	private void mqttConnect() {
		// setup MQTT Client
		String clientID = M2MIO_THING;
		connOpt = new MqttConnectOptions();
	
		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		// connOpt.setUserName(M2MIO_USERNAME);
		// connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());
	
		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, clientID);
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	
		//System.out.println("Connected to " + BROKER_URL);
	}

	public void runClient() {
		mqttConnect();
		int subQoS = 0;
		String myTopic;
		try {
			Class.forName(MYSQL_DRIVER);
			Connection conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
			String sql = "select * from topic where nodemcu_id in (select nodemcu_id from house)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				myTopic = rs.getString("name");
				myClient.subscribe(myTopic, subQoS);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		// publish messages if publisher
		// if (publisher) {
		// for (int i=1; i<=10; i++) {
		// String pubMsg = "{\"pubmsg\":" + i + "}";
		// int pubQoS = 0;
		// MqttMessage message = new MqttMessage(pubMsg.getBytes());
		// message.setQos(pubQoS);
		// message.setRetained(false);
		//
		// // Publish the message
		// System.out.println("Publishing to topic \"" + topic + "\" qos " +
		// pubQoS);
		// MqttDeliveryToken token = null;
		// try {
		// // publish message to broker
		// token = topic.publish(message);
		// // Wait until the message has been delivered to the broker
		// token.waitForCompletion();
		// Thread.sleep(100);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }

		// disconnect
		try {
			// wait to ensure subscribed messages are delivered
			Thread.sleep(5000);
			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SimpleMqttClient smc = new SimpleMqttClient();
		while (true) {
			smc.runClient();
		}
	}
}