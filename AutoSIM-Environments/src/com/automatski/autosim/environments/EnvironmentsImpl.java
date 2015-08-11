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
package com.automatski.autosim.environments;

import java.util.HashMap;
import java.util.List;

import com.automatski.autosim.environments.config.EnvironmentThread;

public class EnvironmentsImpl implements IEnvironments{

	private HashMap<String, EnvironmentThread> environments = new HashMap<String,EnvironmentThread>();
	private HashMap<String, IConnectionFactory> connectionFactories = new HashMap<String,IConnectionFactory>();
	private HashMap<String, IDeviceFactory> deviceFactories = new HashMap<String,IDeviceFactory>();
	
	public EnvironmentsImpl(List<EnvironmentThread> threads){
		for (EnvironmentThread thread: threads){
			environments.put(thread.name, thread);
		}
	}
	
	
	@Override
	public void setup(String environment) throws Exception {
		environments.get(environment).setup();
	}

	@Override
	public void start(String environment) throws Exception {
		environments.get(environment).start();
	}

	@Override
	public void stop(String environment) throws Exception {
		environments.get(environment).stop();
	}

	@Override
	public void teardown(String environment) throws Exception {
		environments.get(environment).teardown();
	}

	@Override
	public void registerConnectionFactory(String name,
			IConnectionFactory factory) {
		connectionFactories.put(name, factory);
		
	}

	@Override
	public void deregisterConnectionFactory(String name) {
		connectionFactories.remove(name);
	}

	@Override
	public void registerDeviceFactory(String name, IDeviceFactory factory) {
		deviceFactories.put(name, factory);
	}

	@Override
	public void deregisterDeviceFactory(String name) {
		deviceFactories.remove(name);
	}


	@Override
	public IConnection getConnection(String name) throws Exception {
		return connectionFactories.get(name).getConnection();
	}


	@Override
	public IDevice getDevice(String name) throws Exception {
		return deviceFactories.get(name).getDevice();
	}

}
