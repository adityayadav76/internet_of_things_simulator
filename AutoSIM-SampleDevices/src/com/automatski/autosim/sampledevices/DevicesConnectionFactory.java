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

import com.automatski.autosim.environments.IDevice;
import com.automatski.autosim.environments.IDeviceFactory;
import com.automatski.autosim.environments.IEnvironments;
import com.automatski.autosim.sampledevices.config.AutoSIMDeviceConfig;

public class DevicesConnectionFactory implements IDeviceFactory{

	private AutoSIMDeviceConfig connectionConfig = null;
	private IEnvironments environments = null;
	
	public DevicesConnectionFactory(AutoSIMDeviceConfig connectionConfig, IEnvironments environments){
		this.connectionConfig = connectionConfig;
		this.environments = environments;
	}


	@Override
	public IDevice getDevice() throws Exception {
		IDevice device = null;
		try {
			device = (IDevice) Class.forName(connectionConfig.deviceClassName).newInstance();
			device.setConfig(connectionConfig);
			device.setEnvironments(environments);
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		return device;
	}

}
