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
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.automatski.autosim.streamer.api.Streamer;
import com.automatski.autosim.streamer.api.Tuple;

public class StreamerClientTest {

	public static void main(String[] args) throws Exception {
		
		
		TTransport transport;

		transport = new TSocket("localhost", 7911);

		TProtocol protocol = new TBinaryProtocol(transport);

		Streamer.Client client = new Streamer.Client(protocol);
		transport.open();
		
		
		
		
		
		
		String accessKey = "defaultAccessKey";
		String secretKey = "defaultSecretKey";
		
		int count = 100;
		Random rnd = new Random();
		for (int i=0; i<count; i++){
			Tuple tuple = new Tuple();
			
			String id = UUID.randomUUID().toString();
			tuple.setId(id);
			
			String topic = "testTopic01";
			tuple.setTopic(topic);
			
			String correlationId = UUID.randomUUID().toString();
			tuple.setCorrelationId(correlationId);
			
			Map<String,String> headers = new HashMap<String,String>();
			headers.put("category", "test");
			tuple.setHeaders(headers);
			
			Map<String,String> payload = new HashMap<String,String>();
			payload.put("id", 6900+i+"");
			String name = "Aditya"+rnd.nextInt();
			payload.put("name", name);
			tuple.setPayload(payload);
			
			client.tellTuple(accessKey, secretKey, tuple);
			System.out.println("Sent: "+name);
		}

		transport.close();
		System.out.println("Done!");
		System.exit(0);
	}

}
