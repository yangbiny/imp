package com.impassive.remoting.netty;

import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.channel.AbstractServer;
import com.impassive.remoting.netty.adapter.CodecAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author impassivey
 */
@Slf4j
public class NettyService extends AbstractServer {

  private final CodecAdapter codecAdapter;

  private ServerBootstrap bootstrap;

  private NioEventLoopGroup boss;

  private NioEventLoopGroup worker;

  private List<Channel> channels;

  public NettyService(Url url, ChannelHandler handler) {
    super(url, handler);
    codecAdapter = new CodecAdapter(url);
  }

  @Override
  protected void doOpen() {
    bootstrap = new ServerBootstrap();
    boss = new NioEventLoopGroup();
    worker = new NioEventLoopGroup();
    NettyServiceHandler nettyServiceHandler = new NettyServiceHandler(handler, url);
    channels = nettyServiceHandler.getChannels();
    bootstrap
        .group(boss, worker)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("decode", codecAdapter.getDecode());
                socketChannel.pipeline().addLast("encode", codecAdapter.getEncode());
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
    if (CollectionUtils.isNotEmpty(channels)) {
      channels.forEach(Channel::destroy);
    }

    if (bootstrap != null) {
      worker.shutdownGracefully();
      boss.shutdownGracefully();
      log.info("close netty service");
    }
  }
}
