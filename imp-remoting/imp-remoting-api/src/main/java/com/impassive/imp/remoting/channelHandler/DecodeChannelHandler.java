package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.channel.AbstractDecodeChannelHandler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 解码器
 *
 * @author impassivey
 */
@Slf4j
public class DecodeChannelHandler extends AbstractDecodeChannelHandler {

  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(10, 10, 10,
      TimeUnit.MINUTES, new ArrayBlockingQueue<>(100), r -> {
    Thread thread = new Thread(r);
    thread.setName("decodeChannelHandler");
    return thread;
  }, new AbortPolicy());

  public DecodeChannelHandler(ChannelHandler channelHandler) {
    super(channelHandler);
  }

  @Override
  public void receive(Channel channel, Object msg) throws Exception {
    THREAD_POOL_EXECUTOR.submit(() -> {
      try {
        channelHandler.receive(channel, msg);
      } catch (Exception e) {
        log.error("receive has error : ", e);
        throw new RuntimeException("receive has exception", e);
      }
    });
  }
}
