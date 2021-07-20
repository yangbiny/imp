package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import java.util.concurrent.CompletableFuture;

/**
 * @author impassivey
 */
public class DefaultCompletableFeature<T> extends CompletableFuture<T> {

  private Channel channel;

  public DefaultCompletableFeature(Channel channel) {
    this.channel = channel;
  }
}
