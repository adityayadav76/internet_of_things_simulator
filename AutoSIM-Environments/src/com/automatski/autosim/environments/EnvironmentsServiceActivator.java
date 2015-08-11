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


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.omg.CORBA.Environment;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.automatski.autosim.environments.config.AutoSIMEnvironmentConfig;
import com.automatski.autosim.environments.config.AutoSIMEnvironmentsConfig;
import com.automatski.autosim.environments.config.EnvironmentThread;
import com.automatski.autosim.environments.utils.GsonUtils;


/**
 * This class implements a simple bundle that uses the bundle
 * context to register an English language dictionary service
 * with the OSGi framework. The dictionary service interface is
 * defined in a separate class file and is implemented by an
 * inner class.
**/

//https://chamibuddhika.wordpress.com/2011/10/02/apache-thrift-quickstart-tutorial/

public class EnvironmentsServiceActivator implements BundleActivator
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
    	Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("Language", "English");

        // read the config
    	final AutoSIMEnvironmentsConfig config = (AutoSIMEnvironmentsConfig) GsonUtils.jsonToObject("./conf/autosim-environments.json", AutoSIMEnvironmentsConfig.class);
    	List<EnvironmentThread> threads = new ArrayList<EnvironmentThread>();
    	for (AutoSIMEnvironmentConfig conf: config.environments){
    		EnvironmentThread thread = new EnvironmentThread(conf);
			threads.add(thread);
		}
    	EnvironmentsImpl environmentsImpl = new EnvironmentsImpl(threads);
    	for (EnvironmentThread thread: threads){
			thread.setEnvironments(environmentsImpl);
		}
        
        
    	context.registerService(
                IEnvironments.class.getName(), environmentsImpl, props);


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