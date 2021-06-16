package com.impassive.imp.net;

import com.impassive.imp.protocol.Url;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import java.net.InetSocketAddress;

/** @author impassivey */
public class NettyClient extends AbstractClient {

  private volatile Bootstrap bootstrap;

  private volatile Channel channel;

  public NettyClient(Url url, ChannelHandler channelHandler) {
    super(url, channelHandler);
  }

  @Override
  protected void doOpen(Url url) {
    bootstrap = new Bootstrap();
    bootstrap
        .group(new NioEventLoopGroup())
        .channel(NioSocketChannel.class)
        .handler(
            new ChannelInitializer<NioSocketChannel>() {
              @Override
              protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new MsgEncoder());
              }
            });
  }

  @Override
  protected void doConnect() {
    ChannelFuture connect =
        bootstrap
            .connect(getConnectAddress())
            .addListener(
                (ChannelFutureListener)
                    future -> {
                      if (future.isSuccess()) {
                        System.out.println("connection success");
                      }
                    });
    this.channel = connect.channel();
  }

  @Override
  protected com.impassive.imp.net.Channel getChannel() {
    return NettyChannel.getOrAddNetChannel(this.channel, url, this);
  }

  private InetSocketAddress getConnectAddress() {
    return new InetSocketAddress(url.getHost(), url.getPort());
  }

  @Override
  public InetSocketAddress getRemoteAddress() {
    return getChannel().getRemoteAddress();
  }
}
