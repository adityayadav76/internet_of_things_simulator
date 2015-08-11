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

import java.util.HashMap;

public class Authenticator {

	private static Authenticator instance;
	private HashMap<String, String> keysAndSecrets = new HashMap<String, String>();
	
	private Authenticator(){};
	
	public synchronized static Authenticator getInstance(){
		if (instance == null) instance = new Authenticator();
		
		return instance;
	}
	
	public boolean isAuthenticated(APIRequest request){
		return (keysAndSecrets.containsKey(request.apiKey) && keysAndSecrets.get(request.apiKey).equals(request.apiSecret));
	}
	
	public void addKeyAndSecret(String apiKey, String apiSecret){
		keysAndSecrets.put(apiKey, apiSecret);
	}
}
