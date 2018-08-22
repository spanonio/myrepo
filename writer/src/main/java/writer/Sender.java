package writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

public class Sender {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		final String PROJECT_ID = "cobalt-balancer-198516";
		ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, "to-gateway");
		Publisher publisher = null;
		List<ApiFuture<String>> messageIdFutures = new ArrayList<ApiFuture<String>>();

		try {
		  // Create a publisher instance with default settings bound to the topic
		  publisher = Publisher.newBuilder(topicName).build();

		  List<String> messages = Arrays.asList("OK","OK2");

		  // schedule publishing one message at a time : messages get automatically batched
		  for (String message : messages) {
		    ByteString data = ByteString.copyFromUtf8(message);
		    PubsubMessage pubsubMessage = PubsubMessage.newBuilder().putAttributes("topic", "gateway-command/foo").setData(data).build();

		    // Once published, returns a server-assigned message id (unique within the topic)
		    ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
		    messageIdFutures.add(messageIdFuture);
		  }
		} finally {
		  // wait on any pending publish requests.
		  List<String> messageIds = ApiFutures.allAsList(messageIdFutures).get();

		  for (String messageId : messageIds) {
		    System.out.println("published with message ID: " + messageId);
		  }

		  if (publisher != null) {
		    // When finished with the publisher, shutdown to free up resources.
		    publisher.shutdown();
		  }
		}

	}

}
