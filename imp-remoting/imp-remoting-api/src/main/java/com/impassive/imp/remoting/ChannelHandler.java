package com.impassive.imp.remoting;

/** @author impassivey */
public interface ChannelHandler {

  /**
   * 连接的时候的操作
   *
   * @param channel handler
   */
  void connection(Channel channel);

  /**
   * 接收到数据的时候的操作
   *
   * @param msg 接收到的信息
   * @param channel handler
   */
  void receive(Channel channel, Object msg);

  /**
   * 连接关闭时的操作
   *
   * @param channel handler
   */
  void close(Channel channel);
}
