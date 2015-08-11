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
package com.automatski.autosim.api;

import com.automatski.autosim.environments.IEnvironments;


public class Environments {

	private static Environments instance;
	private IEnvironments environments = null;
	
	private Environments(){};
	
	public synchronized static Environments getInstance(){
		if (instance == null) instance = new Environments();
		
		return instance;
	}
	
	
	public void setEnvironment(IEnvironments environments){
		this.environments = environments;
	}
	
	public IEnvironments getEnvironment(){
		return environments;
	}

}
