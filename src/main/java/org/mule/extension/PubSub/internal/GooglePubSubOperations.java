package org.mule.extension.PubSub.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileInputStream;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class GooglePubSubOperations {

  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
  
  @MediaType(value = ANY, strict = false)
  public String publisher(@Config GooglePubSubConfiguration configuration, Object payload, String credsPath)
	      throws IOException, ExecutionException, InterruptedException {
		  String projectId = "pavan-322808";
	      String topicId = "gcppublish";
		  File credentialsPath = new File(credsPath);
		  GoogleCredentials credentials;
		  FileInputStream serviceAccountStream = new FileInputStream(credentialsPath);
		  credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
		  
	    TopicName topicName = TopicName.of(projectId, topicId);

	    Publisher publisher = null;
	    try {
	      // Create a publisher instance with default settings bound to the topic
		  CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
	      publisher = Publisher.newBuilder(topicName).setCredentialsProvider(credentialsProvider).build();

	      String message = (String) payload;
	      ByteString data = ByteString.copyFromUtf8(message);
	      PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

	      // Once published, returns a server-assigned message id (unique within the topic)
	      ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
	      String messageId = messageIdFuture.get();
	      System.out.println("Published message ID: " + messageId);
	    } finally {
	      if (publisher != null) {
	        // When finished with the publisher, shutdown to free up resources.
	        publisher.shutdown();
	        publisher.awaitTermination(1, TimeUnit.MINUTES);
	      }
	    }
		return "Successfully posted on to the topic";
	  }
}
