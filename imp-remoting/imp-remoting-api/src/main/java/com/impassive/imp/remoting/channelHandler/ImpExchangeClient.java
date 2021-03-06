package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Client;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.imp.remoting.ExchangeClient;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * @author impassivey
 */
@Slf4j
public class ImpExchangeClient implements ExchangeClient {

  private final ExchangeChannel channel;

  /**
   * 创建一个负责进行网络交换的Client。传进来的client是最终负责请求的一个client
   *
   * @param client 负责请求的client实例
   */
  public ImpExchangeClient(Client client) {
    // client 是NettyClient
    // 负责发送请求的通道
    this.channel = new ImpExchangeChannel(client);
  }

  @Override
  public CompletableFuture<Object> request(Object req) {
    return channel.request(req);
  }

  @Override
  public void destroy() {
    channel.destroy();
  }
}
