package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.channel.AbstractServer;
import com.impassive.remoting.netty.codec.DecodeRequest;
import com.impassive.remoting.netty.codec.EncodeResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassivey
 */
@Slf4j
public class NettyChannelHandler extends AbstractServer {

  private ServerBootstrap bootstrap;

  private NioEventLoopGroup boss;

  private NioEventLoopGroup worker;

  public NettyChannelHandler(Url url, ChannelHandler handler) {
    super(url, handler);
  }

  @Override
  protected void doOpen() {
    bootstrap = new ServerBootstrap();
    boss = new NioEventLoopGroup();
    worker = new NioEventLoopGroup();
    NettyServiceHandler nettyServiceHandler = new NettyServiceHandler(handler, url);
    bootstrap
        .group(boss, worker)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("decode", new DecodeRequest());
                socketChannel.pipeline().addLast("encode", new EncodeResponse());
                socketChannel.pipeline().addLast(nettyServiceHandler);
              }
            })
        .bind(getPort());
  }

  private Integer getPort() {
    if (url == null) {
      throw new IllegalArgumentException("url can not be null");
    }
    return url.getPort();
  }

  @Override
  public void connection(Channel channel) {
  }

  @Override
  public void close(Channel channel) {
    if (bootstrap != null) {
      worker.shutdownGracefully();
      boss.shutdownGracefully();
      log.info("close netty service");
    }
  }
}
