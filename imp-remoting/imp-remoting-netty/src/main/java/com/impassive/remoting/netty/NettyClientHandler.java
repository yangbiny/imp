package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassivey
 */
@Slf4j
public class NettyClientHandler extends ChannelDuplexHandler {

  private final ChannelHandler channelHandler;

  private final Url url;

  public NettyClientHandler(ChannelHandler channelHandler, Url url) {
    this.channelHandler = channelHandler;
    this.url = url;
  }

  @Override
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
      SocketAddress localAddress, ChannelPromise promise) throws Exception {
    super.connect(ctx, remoteAddress, localAddress, promise);
    log.debug("netty is connect : remote : {},local : {}", remoteAddress, localAddress);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    log.debug("channel is inactive : {}, channel active status {}",
        ctx.channel(), ctx.channel().isActive());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyChannel channel = NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    channelHandler.receive(channel, msg);
  }

}
