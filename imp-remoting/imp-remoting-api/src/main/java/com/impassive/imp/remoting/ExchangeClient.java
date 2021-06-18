package com.impassive.imp.remoting;

import java.util.concurrent.CompletableFuture;

/**
 * 进行网络交互的客户端
 *
 * @author impassivey
 */
public interface ExchangeClient {

  /**
   * 发出请求
   *
   * @param object 请求的信息
   * @return 返回值
   */
  CompletableFuture<Object> request(Object object);
}
