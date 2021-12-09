package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.rpc.request.Request;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/** @author impassivey */
public class ExchangeChannelHandler implements ExchangeChannel {

  private static final Map<Channel, ExchangeChannel> EXCHANGE_CHANNEL_MAP =
      new ConcurrentHashMap<>();

  private final Channel channel;

  public ExchangeChannelHandler(Channel channel) {
    this.channel = channel;
  }

  public static ExchangeChannel getOrAddExchangeHandler(Channel channel) {
    ExchangeChannel exchangeChannel = EXCHANGE_CHANNEL_MAP.get(channel);
    if (exchangeChannel != null) {
      return exchangeChannel;
    }
    exchangeChannel = new ExchangeChannelHandler(channel);
    EXCHANGE_CHANNEL_MAP.put(channel, exchangeChannel);
    return exchangeChannel;
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
    channel.send(req);
    return new DefaultCompletableFeature(channel, (Request) req);
  }
}
