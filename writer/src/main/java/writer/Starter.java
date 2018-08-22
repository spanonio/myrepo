package writer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

import mysql.mysqlConn;

public class Starter {
	

	
	  private static final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<PubsubMessage>();

	  static class MessageReceiverExample implements MessageReceiver {

	    
	    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
	      messages.offer(message);
	      consumer.ack();
	    }
	  }

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

	    // set subscriber id, eg. my-sub
	    String subscriptionId = args[0]; //"my-test-sub";
	    String PROJECT_ID = args[1];
	    String dbipandport = args[2];
	    String dbname = args[3];
	    ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(PROJECT_ID, subscriptionId);
	    Subscriber subscriber = null;
	    try {
			System.out.println("#------------------>"+subscriptionName);

	      // create a subscriber bound to the asynchronous message receiver
	      subscriber =
	          Subscriber.newBuilder(subscriptionName, new MessageReceiverExample()).build();
	      subscriber.startAsync().awaitRunning();
    	  System.out.println("STARTING SUBSCRIBER");

	      // Continue to listen to messages
	      while (true) {
	        PubsubMessage message = messages.take();
	        System.out.println("Message Id: " + message.getMessageId());
	        System.out.println("Data: " + message.getData().toStringUtf8());
			mysqlConn conn = new mysqlConn();
	    	System.out.println("conn: " + conn.testConnection(dbipandport , dbname));

		    Statement st = conn.getInstance(dbipandport , dbname).createStatement();
	    	System.out.println("QUERY: " + st);

	    	
	    	//verifica dei messaggi esistenti sulla tabella
	    	System.out.println("STAMPO PER ESSERE SICURO DI ESSERE CONNESSO LE PRIME DIECI RIGHE DELLA TABELLA.");

	    	ResultSet rs = st.executeQuery("select idmessages from messages limit 10;");
	    	  // iterate through the java resultset
	        while (rs.next())
	        {
	          String id = rs.getString("idmessages");
	          // print the results
	          System.out.format("%s \n", id);
	        }
	        rs.close();
	       
	    	System.out.println("INSERISCO IL MESSAGGIO.");

		    if(message.getData().toStringUtf8() != "")
		    {
		    	String query = "INSERT INTO messages values(null," + "'" + message.getMessageId() + "','" + message.getData().toStringUtf8() +  "'," + "null);";
		    	System.out.println("QUERY: " + query);
		    	try {
		    		st.executeUpdate(query);	
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		    st.close();
	        conn.getInstance(dbipandport, dbname).close();
	        
		   
	      }
	    } finally {
	      if (subscriber != null) {
	    	  System.out.println("STOPPING SUBSCRIBER");
	        subscriber.stopAsync();
	        
	      }
	    }
	}

}
