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

public interface IEnvironments {

	public void setup(String environment) throws Exception;
	public void start(String environment) throws Exception;
	public void stop(String environment) throws Exception;
	public void teardown(String environment) throws Exception;
	
	
	public void registerConnectionFactory(String name, IConnectionFactory factory); 
	public void deregisterConnectionFactory(String name);
	public IConnection getConnection(String name) throws Exception;
	
	public void registerDeviceFactory(String name, IDeviceFactory factory); 
	public void deregisterDeviceFactory(String name);
	public IDevice getDevice(String name) throws Exception;
	
	
	
}
