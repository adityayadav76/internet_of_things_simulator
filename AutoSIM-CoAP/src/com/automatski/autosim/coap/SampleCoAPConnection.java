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
package com.automatski.autosim.coap;

import java.net.InetAddress;

import org.ws4d.coap.connection.BasicCoapChannelManager;
import org.ws4d.coap.interfaces.CoapChannelManager;
import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;
import org.ws4d.coap.interfaces.CoapRequest;
import org.ws4d.coap.interfaces.CoapResponse;
import org.ws4d.coap.messages.CoapRequestCode;

import com.automatski.autosim.coap.config.AutoSIMConnectionConfig;
import com.automatski.autosim.environments.IConnection;
import com.automatski.autosim.environments.IConnectionListener;

public class SampleCoAPConnection implements IConnection, CoapClient {

	private AutoSIMConnectionConfig config = null;
	private IConnectionListener listener = null;
	private CoapChannelManager channelManager = null;
    private CoapClientChannel clientChannel = null;
    
	@Override
	public void connect() throws Exception {
		channelManager = BasicCoapChannelManager.getInstance();
		clientChannel = channelManager.connect(this, InetAddress.getByName(config.host), config.port);
	}

	@Override
	public void disconnect() throws Exception {
		//nothing to do
		
	}

	@Override
	public Object send(Object arg0) throws Exception {
		CoapRequest coapRequest = clientChannel.createRequest(true, CoapRequestCode.GET);
//      coapRequest.setContentType(CoapMediaType.octet_stream);
//      coapRequest.setToken("ABCD".getBytes());
//      coapRequest.setUriHost("123.123.123.123");
//      coapRequest.setUriPort(1234);
//      coapRequest.setUriPath("/sub1/sub2/sub3/");
//      coapRequest.setUriQuery("a=1&b=2&c=3");
//      coapRequest.setProxyUri("http://proxy.org:1234/proxytest");
        clientChannel.sendMessage(coapRequest);
        System.out.println("Sent Request");
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

	@Override
    public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
            System.out.println("Connection Failed");
    }

    @Override
    public void onResponse(CoapClientChannel channel, CoapResponse response) {
            System.out.println("Received response");
            // do the listener message stuff here
            
    }
}
