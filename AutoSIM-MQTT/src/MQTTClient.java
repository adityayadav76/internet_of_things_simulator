

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTTClient {

	public static void main(String[] args) throws Exception {

        // MQTT topic
		String topic        = "/data/mychannel"; 
 
    	
		String content      = "{ \"data\":666 }";
 
		// QoS supported at level 0 and 1
		int qos             = 0;
 
		// Connection URI
		String broker	    = "tcp://test.mosquitto.org:1883"; 
 
		String cpUserName = new String("developer");
		String cpPassword = new String("yourpassword");
 
		System.out.println(MqttClient.generateClientId());
		MqttClient sampleClient = new MqttClient(broker,MqttClient.generateClientId().substring(0, 23));// client id cannot be more than 23 chars	    
		MqttConnectOptions	connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		//connOpts.setUserName(cpUserName);
		//connOpts.setPassword(cpPassword.toCharArray());
 

		sampleClient.connect(connOpts);
        	        System.out.println("Connected!");
 
		System.out.println("Publishing message: "+content);
		MqttMessage message = new MqttMessage(content.getBytes());
				    message.setQos(qos);
    	                sampleClient.publish(topic, message);
	    System.out.println("Message published!");
 
		sampleClient.disconnect();
		
		

	}

}
