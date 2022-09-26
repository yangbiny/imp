package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.net.NetUtils;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author impassivey
 */
public class NettyServiceHandler extends ChannelDuplexHandler {

  private final ChannelHandler channelHandler;

  private final Url url;

  private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

  public NettyServiceHandler(ChannelHandler channelHandler, Url url) {
    this.channelHandler = channelHandler;
    this.url = url;
  }

  @Override
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
      throws Exception {
    super.bind(ctx, localAddress, promise);
    NettyChannel channel = NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    CHANNEL_MAP.put(NetUtils.socketAddressToStr(ctx.channel().remoteAddress()), channel);
  }

  @Override
  public void connect(
      ChannelHandlerContext ctx,
      SocketAddress remoteAddress,
      SocketAddress localAddress,
      ChannelPromise promise)
      throws Exception {
    super.connect(ctx, remoteAddress, localAddress, promise);
    NettyChannel channel = NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    CHANNEL_MAP.put(NetUtils.socketAddressToStr(ctx.channel().remoteAddress()), channel);
  }

  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.disconnect(ctx, promise);
    NettyChannel.removeIfNotConnect(ctx.channel());
  }

  @Override
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.close(ctx, promise);
    NettyChannel.removeIfNotConnect(ctx.channel());
  }

  @Override
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.deregister(ctx, promise);
    NettyChannel channel = NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    CHANNEL_MAP.put(NetUtils.socketAddressToStr(ctx.channel().remoteAddress()), channel);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    final NettyChannel nettyChannel =
        NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    channelHandler.receive(nettyChannel, msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    NettyChannel channel = NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    CHANNEL_MAP.put(NetUtils.socketAddressToStr(ctx.channel().remoteAddress()), channel);
  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    super.write(ctx, msg, promise);
  }

  public List<Channel> getChannels() {
    return new ArrayList<>(CHANNEL_MAP.values());
  }
}
