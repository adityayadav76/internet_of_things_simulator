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
import org.restlet.Component;
import org.restlet.data.Protocol;



public class RestServer {

	public static void main(String[] args) throws Exception  {
		// Create a new Component.  
	    Component component = new Component();  

	    // Add a new HTTP server listening on port.  
	    component.getServers().add(Protocol.HTTP, 10080);  

	    // Attach the sample application.  
	    component.getDefaultHost().attach("/example",new MyRestApplication());  

	    // Start the component.  
		System.out.println("Starting Rest Server on port "+10080+" ...");
	    component.start();  

	}

}
