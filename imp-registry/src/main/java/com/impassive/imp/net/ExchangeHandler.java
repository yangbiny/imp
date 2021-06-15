package com.impassive.imp.net;

import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public interface ExchangeHandler extends ChannelHandler {

  /**
   * 返回的是执行后的结果
   *
   * @param exchangeChannel 请求的channel
   * @param request 请求的信息
   * @return 请求的结果
   */
  CompletableFuture<Object> reply(ExchangeChannel exchangeChannel, Object request);
}
