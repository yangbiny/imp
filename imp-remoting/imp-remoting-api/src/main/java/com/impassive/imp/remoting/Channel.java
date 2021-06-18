package com.impassive.imp.remoting;

import java.net.InetSocketAddress;

/** @author impassivey */
public interface Channel extends Endpoint {

  /**
   * 获取远程服务器的IP地址
   *
   * @return 远程服务器的地址
   */
  InetSocketAddress getRemoteAddress();
}
