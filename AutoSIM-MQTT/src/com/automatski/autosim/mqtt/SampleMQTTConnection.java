/*
 *  AutoSIM - Internet of Things Simulator
 *  Copyright (C) 2014, Aditya Yadav <aditya@automatski.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.automatski.autosim.mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.mqtt.config.AutoSIMConnectionConfig;

public class SampleMQTTConnection implements IConnection {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;
	// MQTT topic
	private String topic        = "/data/mychannel"; 
	private int qos             = 0;
	private MqttClient client = null;
	
	@Override
	public void connect() throws Exception {
		client = new MqttClient(config.url,UUID.randomUUID().toString().substring(0,23));//MqttClient.generateClientId().substring(0, 23));// client id cannot be more than 23 chars	    
		MqttConnectOptions	connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		//connOpts.setUserName(config.username);
		//connOpts.setPassword(config.password.toCharArray());
 

		client.connect(connOpts);
        System.out.println("Connected!");
	}

	@Override
	public void disconnect() throws Exception {
		client.disconnect();
		
	}

	@Override
	public Object send(Object arg0) throws Exception {
		String content = (String) arg0;
		System.out.println("Publishing message: "+content);
		MqttMessage message = new MqttMessage(content.getBytes());
		message.setQos(qos);
    	client.publish(topic, message);
	    System.out.println("Message published!");
		return null;
	}

	@Override
	public void setConfig(Object arg0) {
		config = (AutoSIMConnectionConfig) arg0;
		
	}

	@Override
	public void setListener(IConnectionListener listener) {
		this.listener = listener;
		
	}

}
