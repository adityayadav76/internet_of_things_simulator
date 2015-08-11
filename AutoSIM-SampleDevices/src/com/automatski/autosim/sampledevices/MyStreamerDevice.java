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
package com.automatski.autosim.sampledevices;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.environments.IDevice;
import com.automatski.autosim.environments.IEnvironments;
import com.automatski.autosim.sampledevices.config.AutoSIMDeviceConfig;
import com.automatski.autosim.streamer.api.Tuple;

public class MyStreamerDevice implements IDevice, IConnectionListener {

	private AutoSIMDeviceConfig config = null;
	private IEnvironments environments = null;
	private IConnection connection  = null;
	private int i =0;
	
	
	@Override
	public void setConfig(Object arg0) {
		config = (AutoSIMDeviceConfig) arg0;
		
	}

	@Override
	public void setEnvironments(IEnvironments arg0) {
		this.environments = arg0;
	}

	@Override
	public void setup() throws Exception {
		// get a connection
		connection = environments.getConnection(config.connectionName);
		connection.setListener(this);
	}


	@Override
	public void start() throws Exception {
		connection.connect();
		
	}

	@Override
	public void step() throws Exception {
		Random rnd = new Random();
		Tuple tuple = new Tuple();
		
		String id = UUID.randomUUID().toString();
		tuple.setId(id);
		
		String topic = "testTopic01";
		tuple.setTopic(topic);
		
		String correlationId = UUID.randomUUID().toString();
		tuple.setCorrelationId(correlationId);
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("category", "test");
		tuple.setHeaders(headers);
		
		Map<String,String> payload = new HashMap<String,String>();
		payload.put("id", 6900+(i++)+"");
		String name = "Aditya"+rnd.nextInt();
		payload.put("name", name);
		tuple.setPayload(payload);
		
		connection.send(tuple);
		
	}

	@Override
	public void stop() throws Exception {
		connection.disconnect();
		
	}

	@Override
	public void teardown() throws Exception {
		connection.setListener(null);
	}

	@Override
	public void receive(Object arg0) throws Exception {
		System.out.println("Recieved Message: "+arg0.toString());
		
	}


}
