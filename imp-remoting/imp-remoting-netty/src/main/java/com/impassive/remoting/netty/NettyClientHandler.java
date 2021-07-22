package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;

/** @author impassivey */
public class NettyClientHandler extends ChannelDuplexHandler {

  private final ChannelHandler channelHandler;

  private final Url url;

  public NettyClientHandler(ChannelHandler channelHandler, Url url) {
    this.channelHandler = channelHandler;
    this.url = url;
  }


  @Override
  public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise)
      throws Exception {
    super.bind(ctx, localAddress, promise);
  }

  @Override
  public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
      SocketAddress localAddress, ChannelPromise promise) throws Exception {
    super.connect(ctx, remoteAddress, localAddress, promise);
  }

  @Override
  public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.disconnect(ctx, promise);
  }

  @Override
  public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.close(ctx, promise);
  }

  @Override
  public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
    super.deregister(ctx, promise);
  }

  @Override
  public void read(ChannelHandlerContext ctx) throws Exception {
    super.read(ctx);
  }

  @Override
  public void flush(ChannelHandlerContext ctx) throws Exception {
    super.flush(ctx);
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    super.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    super.channelUnregistered(ctx);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    super.channelReadComplete(ctx);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    super.channelWritabilityChanged(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    super.write(ctx, msg, promise);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyChannel channel = NettyChannel.getOrAddNetChannel(ctx.channel(), url, channelHandler);
    channelHandler.receive(channel, msg);
  }

}
