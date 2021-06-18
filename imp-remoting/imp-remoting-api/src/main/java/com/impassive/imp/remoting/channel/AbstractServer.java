package com.impassive.imp.remoting.channel;


import com.impassive.imp.common.Url;
import com.impassive.imp.remoting.ChannelHandler;

/** @author impassivey */
public abstract class AbstractServer extends AbstractChannelHandler {

  public AbstractServer(Url url, ChannelHandler handler) {
    super(url, handler);
    doOpen();
  }

  /** 打开连接的方式 */
  protected abstract void doOpen();
}
