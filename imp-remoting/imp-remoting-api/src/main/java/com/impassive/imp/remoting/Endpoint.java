package com.impassive.imp.remoting;

/**
 * 定义发送和接收相关的功能
 *
 * @author impassivey
 */
public interface Endpoint {

  /**
   * 发送信息
   *
   * @param object 发送的参数
   */
  void send(Object object);
}
