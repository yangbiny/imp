package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.channel.AbstractChannel;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** @author impassivey */
public class NettyChannel extends AbstractChannel {

  private static final Map<Channel, NettyChannel> CHANNEL_MAP = new ConcurrentHashMap<>();

  private final Channel channel;

  private NettyChannel(Channel channel, Url url, ChannelHandler channelHandler) {
    super(url, channelHandler);
    this.channel = channel;
  }

  public static NettyChannel getOrAddNetChannel(
      Channel channel, Url url, ChannelHandler channelHandler) {
    if (channel == null) {
      throw new IllegalArgumentException("channel is null");
    }
    NettyChannel nettyChannel = CHANNEL_MAP.get(channel);
    if (nettyChannel != null) {
      return nettyChannel;
    }
    NettyChannel tmpNettyChannel = new NettyChannel(channel, url, channelHandler);
    nettyChannel = CHANNEL_MAP.putIfAbsent(channel, tmpNettyChannel);
    if (nettyChannel == null) {
      nettyChannel = tmpNettyChannel;
    }
    return nettyChannel;
  }

  public static void removeIfNotConnect(Channel channel) {
    if (channel.isActive()) {
      return;
    }
    CHANNEL_MAP.remove(channel);
  }

  @Override
  public InetSocketAddress getRemoteAddress() {
    return (InetSocketAddress) channel.remoteAddress();
  }

  @Override
  public void send(Object object) {
    channel.writeAndFlush(object);
  }

}
