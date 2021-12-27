package com.impassive.imp.remoting;

import com.impassive.imp.Lifecycle;

/**
 * 定义发送和接收相关的功能
 *
 * @author impassivey
 */
public interface Endpoint extends Lifecycle {

  /**
   * 发送信息
   *
   * @param object 发送的参数
   */
  void send(Object object);
}
