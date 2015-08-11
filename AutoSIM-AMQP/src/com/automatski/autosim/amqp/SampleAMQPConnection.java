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
package com.automatski.autosim.amqp;

import com.automatski.autosim.amqp.config.AutoSIMConnectionConfig;
import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SampleAMQPConnection implements IConnection {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;
	private String QUEUE_NAME = "hello";

	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Channel channel = null;
	
	@Override
	public void connect() throws Exception {

		factory = new ConnectionFactory();
	    factory.setUri(config.url);//"amqp://localhost:5672"
	    
	    connection = factory.newConnection();
	    channel = connection.createChannel();
	    
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	@Override
	public void disconnect() throws Exception {
	    channel.close();
	    connection.close();		
	}

	@Override
	public Object send(Object arg0) throws Exception {
		String message = (String) arg0;// "Hello World!";
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
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
