package com.impassive.imp.remoting;

/** @author impassivey */
public interface Client extends Endpoint, Channel {

  /** 打开客户端连接 */
  void connect();
}
