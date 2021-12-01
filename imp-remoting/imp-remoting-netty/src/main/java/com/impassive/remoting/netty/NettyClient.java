package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.channel.AbstractClient;
import com.impassive.remoting.netty.codec.DecodeRequest;
import com.impassive.remoting.netty.codec.EncoderRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassivey
 */
@Slf4j
public class NettyClient extends AbstractClient {

  private volatile Bootstrap bootstrap;

  private volatile Channel channel;

  public NettyClient(Url url, ChannelHandler channelHandler) {
    super(url, channelHandler);
  }

  @Override
  protected void doOpen(Url url) {
    bootstrap = new Bootstrap();
    NettyClientHandler nettyClientHandler = new NettyClientHandler(channelHandler, url);
    bootstrap
        .group(new NioEventLoopGroup())
        .option(ChannelOption.SO_KEEPALIVE, true)
        .channel(NioSocketChannel.class)
        .handler(
            new ChannelInitializer<NioSocketChannel>() {
              @Override
              protected void initChannel(NioSocketChannel nioSocketChannel) {
                nioSocketChannel
                    .pipeline()
                    .addLast("encoder", new EncoderRequest())
                    .addLast("decoder", new DecodeRequest())
                    .addLast("handler", nettyClientHandler);
              }
            });
  }

  @Override
  protected void doConnect() {
    InetSocketAddress connectAddress = getConnectAddress();
    ChannelFuture future = bootstrap.connect(connectAddress);
    boolean success = future.awaitUninterruptibly(10, TimeUnit.SECONDS);
    if (success && future.isSuccess()) {
      this.channel = future.channel();
      log.info("channel active status : {}", this.channel.isActive());
    } else if (future.cause() != null) {
      log.error("connect has error : ", future.cause());
    }else {
      log.error("failed connect to service : {}",connectAddress);
    }
  }

  @Override
  protected com.impassive.imp.remoting.Channel getChannel() {
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
