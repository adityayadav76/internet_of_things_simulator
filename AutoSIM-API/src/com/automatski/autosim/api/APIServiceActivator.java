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


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.restlet.Component;
import org.restlet.data.Protocol;

import com.automatski.autosim.api.config.AutoSIMAPIConfig;
import com.automatski.autosim.api.config.AutoSIMAPIServerAccessAndSecretKeys;
import com.automatski.autosim.api.utils.GsonUtils;
import com.automatski.autosim.environments.IEnvironments;


/**
 * This class implements a simple bundle that uses the bundle
 * context to register an English language dictionary service
 * with the OSGi framework. The dictionary service interface is
 * defined in a separate class file and is implemented by an
 * inner class.
**/

//https://chamibuddhika.wordpress.com/2011/10/02/apache-thrift-quickstart-tutorial/

public class APIServiceActivator implements BundleActivator
{
	

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
    	final AutoSIMAPIConfig config = (AutoSIMAPIConfig) GsonUtils.jsonToObject("./conf/autosim-api.json", AutoSIMAPIConfig.class);

    	for (AutoSIMAPIServerAccessAndSecretKeys keys: config.server.access){
			Authenticator.getInstance().addKeyAndSecret(keys.accessKey, keys.secretKey);
		}
    	
    	ServiceReference[] refs = context.getServiceReferences(IEnvironments.class.getName(), "(Language=*)");
    	Environments.getInstance().setEnvironment((IEnvironments) context.getService(refs[0]));
    	   	
    	//Start the Blocking Server
    	final int port = config.server.port;
    	
    	Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					// Create a new Component.  
				    Component component = new Component();  

				    // Add a new HTTP server listening on port.  
				    component.getServers().add(Protocol.HTTP, port);  

				    // Attach the sample application.  
				    component.getDefaultHost().attach("/api",  
				            new AutoSIMAPIApplication());  

				    // Start the component.  
					System.out.println("Starting AutoSIM API Server on port "+port+" ...");
				    component.start();  
				} catch (Exception e){
					System.out.println("Error starting AutoSIM API Server! "+e.getMessage());
				}
			}
    		
    	});
    	thread.start();
    }

    /**
     * Implements BundleActivator.stop(). Does nothing since
     * the framework will automatically unregister any registered services.
     * @param context the framework context for the bundle.
    **/
    public void stop(BundleContext context)
    {

    }
}