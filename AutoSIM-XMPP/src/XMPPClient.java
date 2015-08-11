import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;


public class XMPPClient {

	public static void main(String[] args) throws Exception {

		String username = "adityayadav76@jabber.hot-chilli.net:5222";
		String password = "qwertyuiop";
		
		XMPPTCPConnectionConfiguration connConfig = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword(username,password)
				  .setServiceName("jabber.hot-chilli.net")
				  .setHost("jabber.hot-chilli.net")
				  .setPort(5222)
				  .build();
		
		
		AbstractXMPPConnection connection = new XMPPTCPConnection(connConfig);
		// Connect to the server
		connection.connect();
		// Log into the server
		connection.login();

		String receiver = username;
		// Assume we've created an XMPPConnection name "connection"._
		ChatManager chatmanager = ChatManager.getInstanceFor(connection);
		Chat chat = chatmanager.createChat(username, new ChatMessageListener() {
			public void processMessage(Chat chat, Message message) {
				System.out.println("Received message: " + message);
			}
		});

		String message = "hello world";
		chat.sendMessage(message);
		
		
		
		
		connection.disconnect();
		
	}

}
