package com.impassive.imp.remoting;

import java.util.concurrent.CompletableFuture;

/**
 * 网络服务的channel，请求信息的发送
 *
 * @author impassivey
 */
public interface ExchangeChannel extends Channel {

  /**
   * 发出请求信息
   *
   * @param req 请求的参数
   * @return 返回结果
   */
  CompletableFuture<Object> request(Object req);
}
