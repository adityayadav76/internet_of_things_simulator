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
package com.automatski.autosim.automatski;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.automatski.autosim.automatski.config.AutoSIMConnectionConfig;
import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;
import com.automatski.autosim.streamer.api.Streamer;
import com.automatski.autosim.streamer.api.Tuple;

public class SampleStreamerConnection implements IConnection {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;
	
	private TTransport transport;
	private Streamer.Client client;
	
	@Override
	public void connect() throws Exception {
		this.transport = new TSocket(config.host, config.port);

		TProtocol protocol = new TBinaryProtocol(transport);

		this.client = new Streamer.Client(protocol);
		transport.open();
	}

	@Override
	public void disconnect() throws Exception {
		if (transport != null && transport.isOpen()) transport.close();
		
	}

	@Override
	public Object send(Object arg0) throws Exception {
		Tuple tuple = (Tuple) arg0;
		
		try {
			client.tellTuple(config.username, config.password, tuple);
		} catch (Exception e) {
			System.out.println("Exception while sending Tuple: "
					+ e.getMessage());
			try {
				reconnect();
				client.tellTuple(config.username, config.password, tuple);
				// try once more or else drop
			} catch (Exception g) {
			}
		}
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

	private void reconnect() throws Exception {
		// do nothing
		if (transport != null && transport.isOpen()) transport.close();
		this.transport = new TSocket(config.host, config.port);

		TProtocol protocol = new TBinaryProtocol(transport);

		this.client = new Streamer.Client(protocol);
		transport.open();
	}	
}
