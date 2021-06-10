package com.impassive.imp.net;

import com.impassive.imp.protocol.Url;
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
    if (channel.isActive()) {
      if (nettyChannel != null) {
        return nettyChannel;
      }
      NettyChannel tmpNettyChannel = new NettyChannel(channel, url, channelHandler);
      nettyChannel = CHANNEL_MAP.putIfAbsent(channel, tmpNettyChannel);
      if (nettyChannel == null) {
        nettyChannel = tmpNettyChannel;
      }
    }
    return nettyChannel;
  }

  @Override
  public InetSocketAddress getRemoteAddress() {
    return (InetSocketAddress) channel.remoteAddress();
  }
}
