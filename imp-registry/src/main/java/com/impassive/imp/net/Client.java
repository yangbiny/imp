package com.impassive.imp.net;

/** @author impassivey */
public interface Client extends Endpoint, Channel {

  /** 打开客户端连接 */
  void connect();
}
