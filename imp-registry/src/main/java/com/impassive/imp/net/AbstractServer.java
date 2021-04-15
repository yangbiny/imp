package com.impassive.imp.net;

import com.impassive.imp.protocol.Url;

/** @author impassivey */
public abstract class AbstractServer extends AbstractChannelHandler {

  public AbstractServer(Url url, ChannelHandler handler) {
    super(url, handler);
    doOpen();
  }

  /** 打开连接的方式 */
  protected abstract void doOpen();
}
