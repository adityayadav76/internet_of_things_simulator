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
package com.automatski.autosim.environments.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

	private static GsonBuilder builder = null;
	
	static {
        builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
	}
	
	public static String objectToJson(Object obj) throws Exception {
        Gson gson = builder.create();
        return gson.toJson(obj);
	}
	
	public static Object jsonToObject(String path, Class clas) throws Exception {
        Gson gson = builder.create();
		String json = new String(Files.readAllBytes(Paths.get(path)));//"./albums.json")));
        return gson.fromJson(json, clas);
	}
	
	
}
