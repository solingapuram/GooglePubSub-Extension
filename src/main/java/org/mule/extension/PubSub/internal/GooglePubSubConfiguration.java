package org.mule.extension.PubSub.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(GooglePubSubOperations.class)
@ConnectionProviders(GooglePubSubConnectionProvider.class)
public class GooglePubSubConfiguration {

  @Parameter
  private String Transactionid;

  public String getConfigId(){
    return Transactionid;
  }
}
