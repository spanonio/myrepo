package hello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;


@RestController
public class DataController {

    @RequestMapping("/test")
    public Data testMeth(
    		@RequestParam(value="pname", defaultValue="World") String pname,
    		@RequestParam(value="tname", defaultValue="World") String tname,
    		@RequestParam(value="aname", defaultValue="World") String aname,
    		@RequestParam(value="avalue", defaultValue="World") String avalue,
    		@RequestParam(value="msg", defaultValue="World") String msg) throws Exception {
    	
    	
		ProjectTopicName topicName = ProjectTopicName.of("narroband3", "to-gateway");
		Publisher publisher = null;
		List<ApiFuture<String>> messageIdFutures = new ArrayList<ApiFuture<String>>();

		try {
		  // Create a publisher instance with default settings bound to the topic
		  publisher = Publisher.newBuilder(topicName).build();

		  List<String> messages = Arrays.asList(msg);

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
        return new Data(pname,tname,aname,avalue);
    }
}
