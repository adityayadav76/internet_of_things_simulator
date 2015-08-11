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

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class MyEventServerSocket
{
	
	private Session session;
	
    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
    	this.session = sess;
        System.out.println("Socket Connected: " + sess);
    }
    
    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println("Received TEXT message: " + message);
        try {
        	if ((session != null) && (session.isOpen()))
            {
                System.out.println("Echoing back text message "+message);
                session.getAsyncRemote().sendText("Received: "+message,new SendHandler(){

					@Override
					public void onResult(SendResult arg0) {
						if (!arg0.isOK()){
							System.out.println("Error Sending Response: "+arg0.getException().getMessage());
						}
					}
                	
                });
            }
        } catch (Exception e){
        	System.out.println("Error: "+e.getMessage());
        	e.printStackTrace();
        }
    }
    
    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
    }
    
    @OnError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }
}