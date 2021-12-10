package org.mule.extension.PubSub.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;


/**
 * This class (as it's name implies) provides connection instances and the funcionality to disconnect and validate those
 * connections.
 * <p>
 * All connection related parameters (values required in order to create a connection) must be
 * declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares that connections resolved by this provider
 * will be pooled and reused. There are other implementations like {@link CachedConnectionProvider} which lazily creates and
 * caches connections or simply {@link ConnectionProvider} if you want a new connection each time something requires one.
 */
public class GooglePubSubConnectionProvider implements PoolingConnectionProvider<GooglePubSubConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(GooglePubSubConnectionProvider.class);

 /**
  * A parameter that is always required to be configured.
  */
  @DisplayName("CredsPath")
  @Parameter
  private String credsPath;
  

  @Override
  public GooglePubSubConnection connect() throws ConnectionException {
	  String projectId = "pavan-322808";
      String topicId = "gcppublish";
	  File credentialsPath = new File(credsPath);
	  GoogleCredentials credentials;
	  FileInputStream serviceAccountStream;
	try {
		serviceAccountStream = new FileInputStream(credentialsPath);
		credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
		TopicName topicName = TopicName.of(projectId, topicId);
		Publisher publisher = null;
		CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
	      publisher = Publisher.newBuilder(topicName).setCredentialsProvider(credentialsProvider).build();
	      return new GooglePubSubConnection("success");
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	return new GooglePubSubConnection("success");
	  
  }

  @Override
  public void disconnect(GooglePubSubConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(GooglePubSubConnection connection) {
    return ConnectionValidationResult.success();
  }
}
