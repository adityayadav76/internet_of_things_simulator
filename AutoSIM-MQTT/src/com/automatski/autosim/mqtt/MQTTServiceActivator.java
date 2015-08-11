package com.automatski.autosim.mqtt;


import java.util.HashMap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.automatski.autosim.environments.IEnvironments;
import com.automatski.autosim.mqtt.config.AutoSIMConnectionConfig;
import com.automatski.autosim.mqtt.config.AutoSIMMQTTConfig;
import com.automatski.autosim.mqtt.utils.GsonUtils;


/**
 * This class implements a simple bundle that uses the bundle
 * context to register an English language dictionary service
 * with the OSGi framework. The dictionary service interface is
 * defined in a separate class file and is implemented by an
 * inner class.
**/



public class MQTTServiceActivator implements BundleActivator
{
	private IEnvironments environments = null;
	private AutoSIMMQTTConfig config = null;
	private HashMap<String,MQTTConnectionFactory> factories = new HashMap<String,MQTTConnectionFactory>();
	
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
    	config = (AutoSIMMQTTConfig) GsonUtils.jsonToObject("./conf/autosim-mqtt.json", AutoSIMMQTTConfig.class);

    	
    	ServiceReference[] refs = context.getServiceReferences(IEnvironments.class.getName(), "(Language=*)");
    	environments = (IEnvironments) context.getService(refs[0]);

    	for (AutoSIMConnectionConfig connectionConfig : config.connections){
			MQTTConnectionFactory factory = new MQTTConnectionFactory(connectionConfig);
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