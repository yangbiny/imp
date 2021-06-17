package com.impassive.imp.net;

import com.impassive.imp.protocol.Url;

/** @author impassivey */
public abstract class AbstractClient extends AbstractEndpoint implements Client {

  protected Url url;

  /**
   * 打开一个client客户端
   *
   * @param url url的信息
   */
  protected abstract void doOpen(Url url);

  /** 打开客户端连接 */
  protected abstract void doConnect();

  /**
   * 获得channel
   *
   * @return 通信的channel
   */
  protected abstract Channel getChannel();

  public AbstractClient(Url url,ChannelHandler channelHandler) {
    super(channelHandler);
    this.url = url;
    init();
  }

  @Override
  public void send(Object object) {
    Channel channel = getChannel();
    channel.send(object);
  }

  private void init() {
    doOpen(url);
    connect();
  }

  @Override
  public void connect() {
    doConnect();
  }
}
