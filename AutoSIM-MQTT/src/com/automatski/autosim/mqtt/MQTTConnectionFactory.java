package com.automatski.autosim.mqtt;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionFactory;
import com.automatski.autosim.mqtt.config.AutoSIMConnectionConfig;

public class MQTTConnectionFactory implements IConnectionFactory{

	private AutoSIMConnectionConfig connectionConfig = null;
	
	public MQTTConnectionFactory(AutoSIMConnectionConfig connectionConfig){
		this.connectionConfig = connectionConfig;
	}
	
	@Override
	public IConnection getConnection() {
		
		IConnection connection = null;
		try {
			connection = (IConnection) Class.forName(connectionConfig.connectorClassName).newInstance();
			connection.setConfig(connectionConfig);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return connection;
		
	}

}
