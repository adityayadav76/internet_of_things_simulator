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
package com.automatski.autosim.udp;


import java.util.HashMap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.automatski.autosim.environments.IEnvironments;
import com.automatski.autosim.udp.config.AutoSIMConnectionConfig;
import com.automatski.autosim.udp.config.AutoSIMUDPConfig;
import com.automatski.autosim.udp.utils.GsonUtils;


/**
 * This class implements a simple bundle that uses the bundle
 * context to register an English language dictionary service
 * with the OSGi framework. The dictionary service interface is
 * defined in a separate class file and is implemented by an
 * inner class.
**/



public class UDPServiceActivator implements BundleActivator
{
	private IEnvironments environments = null;
	private AutoSIMUDPConfig config = null;
	private HashMap<String,UDPConnectionFactory> factories = new HashMap<String,UDPConnectionFactory>();
	
    /**
     * Implements BundleActivator.start(). Registers an
     * instance of a dictionary service using the bundle context;
     * attaches properties to the service that can be queried
     * when performing a service look-up.
     * @param context the framework context for the bundle.
    **/
    public void start(BundleContext context) throws Exception 
    {
    	
    	//System.out.println(new File(".").getAbsolutePath());
    	
    	// read the config
    	config = (AutoSIMUDPConfig) GsonUtils.jsonToObject("./conf/autosim-udp.json", AutoSIMUDPConfig.class);

    	
    	ServiceReference[] refs = context.getServiceReferences(IEnvironments.class.getName(), "(Language=*)");
    	environments = (IEnvironments) context.getService(refs[0]);

    	for (AutoSIMConnectionConfig connectionConfig : config.connections){
			UDPConnectionFactory factory = new UDPConnectionFactory(connectionConfig);
			environments.registerConnectionFactory(connectionConfig.name, factory);
			factories.put(connectionConfig.name, factory);
		}   	
    	
    }

    /**
     * Implements BundleActivator.stop(). Does nothing since
     * the framework will automatically unregister any registered services.
     * @param context the framework context for the bundle.
    **/
    public void stop(BundleContext context)
    {
    	for (AutoSIMConnectionConfig connectionConfig : config.connections){
			environments.deregisterConnectionFactory(connectionConfig.name);
			factories.remove(connectionConfig.name);
		}   	
    	
    }
}