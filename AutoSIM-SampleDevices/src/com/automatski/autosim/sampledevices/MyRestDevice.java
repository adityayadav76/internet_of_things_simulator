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

import java.util.Random;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.environments.IDevice;
import com.automatski.autosim.environments.IEnvironments;
import com.automatski.autosim.sampledevices.config.AutoSIMDeviceConfig;

public class MyRestDevice implements IDevice, IConnectionListener {

	private AutoSIMDeviceConfig config = null;
	private IEnvironments environments = null;
	private IConnection connection  = null;
	
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
		String product = "{\"qty\":"+(1+rnd.nextInt(999))+",\"name\":\"iPad "+(1+rnd.nextInt(6))+"\"}";
		connection.send(product);
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
