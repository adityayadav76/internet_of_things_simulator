import java.net.InetAddress;
import java.net.UnknownHostException;

import org.ws4d.coap.Constants;
import org.ws4d.coap.connection.BasicCoapChannelManager;
import org.ws4d.coap.interfaces.CoapChannelManager;
import org.ws4d.coap.interfaces.CoapClient;
import org.ws4d.coap.interfaces.CoapClientChannel;
import org.ws4d.coap.interfaces.CoapRequest;
import org.ws4d.coap.interfaces.CoapResponse;
import org.ws4d.coap.messages.CoapEmptyMessage;
import org.ws4d.coap.messages.CoapRequestCode;

/**
 * @author Christian Lerche <christian.lerche@uni-rostock.de>
 */

public class CoAPClient implements CoapClient {
    private static final String SERVER_ADDRESS = "californium.eclipse.org";
    private static final int PORT = Constants.COAP_DEFAULT_PORT;//5683
    static int counter = 0;
    CoapChannelManager channelManager = null;
    CoapClientChannel clientChannel = null;

    public static void main(String[] args) {
        System.out.println("Start CoAP Client");
        CoAPClient client = new CoAPClient();
        client.channelManager = BasicCoapChannelManager.getInstance();
        client.runTestClient();
    }
    
    public void runTestClient(){
        try {
                        clientChannel = channelManager.connect(this, InetAddress.getByName(SERVER_ADDRESS), PORT);
                        CoapRequest coapRequest = clientChannel.createRequest(true, CoapRequestCode.GET);
//                      coapRequest.setContentType(CoapMediaType.octet_stream);
//                      coapRequest.setToken("ABCD".getBytes());
//                      coapRequest.setUriHost("123.123.123.123");
//                      coapRequest.setUriPort(1234);
//                      coapRequest.setUriPath("/sub1/sub2/sub3/");
//                      coapRequest.setUriQuery("a=1&b=2&c=3");
//                      coapRequest.setProxyUri("http://proxy.org:1234/proxytest");
                        clientChannel.sendMessage(coapRequest);
                        System.out.println("Sent Request");
                } catch (UnknownHostException e) {
                        e.printStackTrace();
                }
    }

        @Override
        public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
                System.out.println("Connection Failed");
        }

        @Override
        public void onResponse(CoapClientChannel channel, CoapResponse response) {
                System.out.println("Received response");
                
                
        }
}