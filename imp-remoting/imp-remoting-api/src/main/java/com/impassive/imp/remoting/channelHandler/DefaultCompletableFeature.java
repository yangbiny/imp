package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.Request;
import com.impassive.imp.remoting.Result;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/** @author impassivey */
public class DefaultCompletableFeature extends CompletableFuture<Object> {

  private static final Map<Long, DefaultCompletableFeature> FUTURES = new ConcurrentHashMap<>();

  private final Channel channel;

  public DefaultCompletableFeature(Channel channel, Request req) {
    this.channel = channel;
    FUTURES.put(req.getRequestId(), this);
  }

  public static DefaultCompletableFeature getInstance(Long requestId){
    return FUTURES.remove(requestId);
  }

  public static void receive(Channel channel, Object msg){
    if (!(msg instanceof Result)){
      return;
    }
    Result result = (Result) msg;
    DefaultCompletableFeature instance = getInstance(result.getRequestId());
    instance.deReceive(msg);
  }

  private void deReceive(Object msg) {
    this.complete(msg);
  }
}
