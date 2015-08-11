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

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.udp.config.AutoSIMConnectionConfig;

public class SampleUDPConnection implements IConnection {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;
	private MulticastSocket socket = null;
	
	@Override
	public void connect() throws Exception {
		socket = new MulticastSocket();
	}

	@Override
	public void disconnect() throws Exception {
		socket.close();
	}

	@Override
	public Object send(Object arg0) throws Exception {
		byte[] buf   = (byte[]) arg0;
		InetAddress group = InetAddress.getByName(config.host);//"203.0.113.0"
        DatagramPacket packet;
        packet = new DatagramPacket(buf, buf.length, group, config.port);//4446
        socket.send(packet, (byte) config.ttl);
        return null;
	}

	@Override
	public void setConfig(Object arg0) {
		config = (AutoSIMConnectionConfig) arg0;
		
	}

	@Override
	public void setListener(IConnectionListener listener) {
		this.listener = listener;
		
	}

}
