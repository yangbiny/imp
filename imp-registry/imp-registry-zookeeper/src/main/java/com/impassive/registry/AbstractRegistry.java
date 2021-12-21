package com.impassive.registry;

import com.impassive.imp.common.Url;
import com.impassive.registry.registry.Registry;

/**
 * @author impassivey
 */
public abstract class AbstractRegistry implements Registry {

  protected Url url;

  public AbstractRegistry(Url url) {
    setUrl(url);
  }

  private void setUrl(Url url) {
    if (url == null) {
      throw new IllegalArgumentException("url can not be null");
    }
    this.url = url;
  }

  @Override
  public void registry(Url url) {
    doRegister(url);
  }

  @Override
  public void unRegistry(Url url) {

  }

  /**
   * 写入数据到注册中心的具体方式
   *
   * @param url 需要注册的对象
   */
  protected abstract void doRegister(Url url);

  /**
   * 从注册中心取消。会删除对应的数据
   *
   * @param url url的信息
   */
  protected abstract void doUnRegistry(Url url);
}
