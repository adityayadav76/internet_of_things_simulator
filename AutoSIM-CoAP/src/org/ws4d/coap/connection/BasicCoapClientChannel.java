/* Copyright [2011] [University of Rostock]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

package org.ws4d.coap.connection;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;

import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;
import org.ws4d.coap.interfaces.CoapMessage;
import org.ws4d.coap.interfaces.CoapRequest;
import org.ws4d.coap.interfaces.CoapResponse;
import org.ws4d.coap.interfaces.CoapSocketHandler;
import org.ws4d.coap.messages.BasicCoapRequest;
import org.ws4d.coap.messages.BasicCoapResponse;
import org.ws4d.coap.messages.CoapBlockOption;
import org.ws4d.coap.messages.CoapBlockOption.CoapBlockSize;
import org.ws4d.coap.messages.CoapEmptyMessage;
import org.ws4d.coap.messages.CoapPacketType;
import org.ws4d.coap.messages.CoapRequestCode;

/**
 * @author Christian Lerche <christian.lerche@uni-rostock.de>
 */

public class BasicCoapClientChannel extends BasicCoapChannel implements CoapClientChannel {
	CoapClient client = null;
	ClientBlockContext blockContext = null;
	CoapRequest lastRequest = null;
	Object trigger = null;
	
	public BasicCoapClientChannel(CoapSocketHandler socketHandler,
			CoapClient client, InetAddress remoteAddress,
			int remotePort) {
		super(socketHandler, remoteAddress, remotePort);
		this.client = client;
	}
	
	@Override
    public void close() {
        socketHandler.removeClientChannel(this);
    }

	@Override 
	public void handleMessage(CoapMessage message) { 
		if (message.isRequest()){
			/* this is a client channel, no requests allowed */
			message.getChannel().sendMessage(new CoapEmptyMessage(CoapPacketType.RST, message.getMessageID()));
			return;
		}
		
		if (message.isEmpty() && message.getPacketType() == CoapPacketType.ACK){
			/* this is the ACK of a separate response */
			//TODO: implement a handler or listener, that informs a client when a sep. resp. ack was received
			return;
		}  
		
		if (message.getPacketType() == CoapPacketType.CON) {
			/* this is a separate response */
			/* send ACK */
			this.sendMessage(new CoapEmptyMessage(CoapPacketType.ACK, message.getMessageID()));
		} 
		
		/* check for blockwise transfer */
		CoapBlockOption block2 = message.getBlock2();
		if (blockContext == null && block2 != null){
			/* initiate blockwise transfer */
			blockContext = new ClientBlockContext(block2, maxReceiveBlocksize);
			blockContext.setFirstRequest(lastRequest);
			blockContext.setFirstResponse((CoapResponse) message);
		}
		
		if (blockContext!= null){
			/*blocking option*/
			if (!blockContext.addBlock(message, block2)){
				/*this was not a correct block*/
				/* TODO: implement either a RST or ignore this packet */
			}
			
			if (!blockContext.isFinished()){
				/* TODO: implement a counter to avoid an infinity req/resp loop:
				 *  		if the same block is received more than x times -> rst the connection 
				 *  implement maxPayloadSize to avoid an infinity payload */
				CoapBlockOption newBlock = blockContext.getNextBlock();
				if (lastRequest == null){
					/*TODO: this should never happen*/
					System.out.println("ERROR: client channel: lastRequest == null");
				} else {
					/* create a new request for the next block */
					BasicCoapRequest request =  new BasicCoapRequest(lastRequest.getPacketType(), lastRequest.getRequestCode(), channelManager.getNewMessageID());
					request.copyHeaderOptions((BasicCoapRequest) blockContext.getFirstRequest()); 
					request.setBlock2(newBlock);
					sendMessage(request);
				}
				/* TODO: implement handler, inform the client that a block (but not the complete message) was received*/
				return;
			} 
			/* blockwise transfer finished */
			message.setPayload(blockContext.getPayload());
			/* TODO: give the payload separately and leave the original message as they is*/
		} 		

		/* normal or separate response */
		client.onResponse(this, (BasicCoapResponse) message);
	}

	@Override
	public void lostConnection(boolean notReachable, boolean resetByServer) {
		client.onConnectionFailed(this, notReachable, resetByServer);		

	}
	
    @Override
    public BasicCoapRequest createRequest(boolean reliable, CoapRequestCode requestCode) {
    	BasicCoapRequest msg = new BasicCoapRequest(
                reliable ? CoapPacketType.CON : CoapPacketType.NON, requestCode,
                channelManager.getNewMessageID());
        msg.setChannel(this);
        return msg;
    }
    
    @Override
    public void sendMessage(CoapMessage msg) {
        super.sendMessage(msg);
        //TODO: check
        lastRequest = (CoapRequest) msg;
    }

    // public DefaultCoapClientChannel(CoapChannelManager channelManager) {
    // super(channelManager);
    // }
    //
    // @Override
    // public void connect(String remoteHost, int remotePort) {
    // socket = null;
    // if (remoteHost!=null && remotePort!=-1) {
    // try {
    // socket = new DatagramSocket();
    // } catch (SocketException e) {
    // e.printStackTrace();
    // }
    // }
    //
    // try {
    // InetAddress address = InetAddress.getByName(remoteHost);
    // socket.connect(address, remotePort);
    // super.establish(socket);
    // } catch (UnknownHostException e) {
    // e.printStackTrace();
    // }
    // }
    
    
    
    private class ClientBlockContext{

		ByteArrayOutputStream payload = new ByteArrayOutputStream();
    	boolean finished = false;
    	CoapBlockSize blockSize; //null means no block option
    	CoapRequest request;
    	CoapResponse response;
    	
    	
    	public ClientBlockContext(CoapBlockOption blockOption, CoapBlockSize maxBlocksize) {
    		
    		/* determine the right blocksize (min of remote and max)*/
    		if (maxBlocksize == null){
    			blockSize = blockOption.getBlockSize();
    		} else {
    			int max = maxBlocksize.getSize();
    			int remote = blockOption.getBlockSize().getSize();
    			if (remote < max){
    				blockSize = blockOption.getBlockSize();
    			} else {
    				blockSize = maxBlocksize;
    			}
    		}
    	}

		public byte[] getPayload() {
			return payload.toByteArray();
		}

		public boolean addBlock(CoapMessage msg, CoapBlockOption block){
    		int blockPos = block.getBytePosition();
    		int blockLength =  msg.getPayloadLength();
    		int bufSize = payload.size();
    		
    		/*TODO: check if payload length = blocksize (except for the last block)*/
    		if (blockPos > bufSize){
    			/* data is missing before this block */
    			return false;
    		} else if ((blockPos + blockLength) <= bufSize){
				/* data already received */
    			return false;
			}
    		int offset = bufSize - blockPos;
    		payload.write(msg.getPayload(), offset, blockLength - offset);
    		
    		if (block.isLast()){
    			/* was this the last block */
    			finished = true;
    		}
    		
    		return true;
    	}
    	
		public CoapBlockOption getNextBlock() {
			int num = payload.size() / blockSize.getSize(); //ignore the rest (no rest should be there)
			return new CoapBlockOption(num, false, blockSize);
		}
    	

		public boolean isFinished() {
			return finished;
		}

		public CoapRequest getFirstRequest() {
			return request;
		}

		public void setFirstRequest(CoapRequest request) {
			this.request = request;
		}

		public CoapResponse getFirstResponse() {
			return response;
		}

		public void setFirstResponse(CoapResponse response) {
			this.response = response;
		}
    }

	@Override
	public void setTrigger(Object o) {
		trigger = o;
		
	}

	@Override
	public Object getTrigger() {
		return trigger;
	}

}
