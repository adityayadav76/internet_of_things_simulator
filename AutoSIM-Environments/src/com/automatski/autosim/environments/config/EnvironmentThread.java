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
package com.automatski.autosim.environments.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.automatski.autosim.environments.IDevice;
import com.automatski.autosim.environments.IEnvironments;

public class EnvironmentThread{

	public String name;
	public int count;
	public String device;
	public int delayInMilliSeconds;
	public int threads;
	
	private List<IDevice> devices = new ArrayList<IDevice>();
	private IEnvironments environments = null;
	private boolean running = false;
	private Thread thread = null;
	
	public EnvironmentThread(AutoSIMEnvironmentConfig conf){
		this.name = conf.name;
		this.count = conf.count;
		this.device = conf.device;
		this.delayInMilliSeconds = conf.delayInMilliSeconds;
		this.threads = conf.threads;
	}
	
	
	public void setEnvironments(IEnvironments environments){
		this.environments = environments;
	}
	

	public void setup() throws Exception {
		for (int i=0; i<count;i++){
			IDevice d = environments.getDevice(device);
			d.setup();
			devices.add(d);
		}		
	}

	public void start() throws Exception {
		for (IDevice d: devices){
			d.start();
		}
		running = true;
		thread = new Thread(){
			public void run(){
				while (running){
					ExecutorService executor = Executors.newFixedThreadPool(threads);
					for (IDevice d: devices) {
						final IDevice device = d;
						Runnable worker = new Runnable(){
							public void run(){
								try {
									device.step();
								} catch (Exception e){
									System.out.println(e.getMessage());
								}
							}
						};
					    executor.execute(worker);
					}
					executor.shutdown();
					while (!executor.isTerminated()) {}
					
					try {
						Thread.currentThread().sleep(delayInMilliSeconds);
					}catch (Exception e){}
					
				}
			}
		};
		thread.start();
	}

	public void stop() throws Exception {
		thread = null;
		running = false;
		for (IDevice d: devices){
			d.stop();
		}
	}

	public void teardown() throws Exception {
		for (IDevice d: devices){
			d.teardown();
		}		
		devices.clear();
	}
	
}
