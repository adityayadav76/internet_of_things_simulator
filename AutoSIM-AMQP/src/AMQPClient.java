import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.RpcClient;

public class AMQPClient {

	public static void main(String[] args) throws Exception {
		
		String QUEUE_NAME = "hello";
		String uri = "amqp://localhost:5672";
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setUri(uri);
	    
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    
	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    
	    String message = "Hello World!";
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	    
	    channel.close();
	    connection.close();
		
	    System.out.println("Message Sent!");
	}
	
}