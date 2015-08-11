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
package com.automatski.autosim.automatski;

import com.automatski.autosim.automatski.config.AutoSIMConnectionConfig;
import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionFactory;

public class AutomatskiConnectionFactory implements IConnectionFactory{

	private AutoSIMConnectionConfig connectionConfig = null;
	
	public AutomatskiConnectionFactory(AutoSIMConnectionConfig connectionConfig){
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
