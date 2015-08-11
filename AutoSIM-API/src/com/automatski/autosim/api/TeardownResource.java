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

import java.util.UUID;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 * Resource which has only one representation.
 *
 */
public class TeardownResource extends ServerResource  {

	@Post("json")
	public APIResponse process(APIRequest request) {
		APIResponse response = new APIResponse();
		try {
			if (Authenticator.getInstance().isAuthenticated(request)) {
				Environments.getInstance().getEnvironment().teardown(request.environment);
				
				response.result="OK!";
				
				return response;
			} else {
				response.setError(402, "Couldn't Authenticate!");
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setError(403, "Internal Server Error!");
			return response;
		}
	}

}