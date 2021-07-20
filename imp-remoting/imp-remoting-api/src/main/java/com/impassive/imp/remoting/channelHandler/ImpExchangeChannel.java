package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ExchangeChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public class ImpExchangeChannel implements ExchangeChannel {

  private final Channel channel;

  public ImpExchangeChannel(Channel channel) {
    this.channel = channel;
  }

  @Override
  public InetSocketAddress getRemoteAddress() {
    return channel.getRemoteAddress();
  }

  @Override
  public void send(Object object) {
    channel.send(object);
  }

  @Override
  public CompletableFuture<Object> request(Object req) {
    // 这里就是最终的 client 发送出请求。此处这里是nettyClient
    channel.send(req);
    return new DefaultCompletableFeature<>();
  }
}
