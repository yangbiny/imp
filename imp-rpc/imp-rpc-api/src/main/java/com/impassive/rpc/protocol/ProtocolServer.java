package com.impassive.rpc.protocol;

import com.impassive.imp.Lifecycle;

/** @author impassivey */
public interface ProtocolServer extends Lifecycle {

  /**
   * 获取绑定的地址信息
   *
   * @return 绑定的地址信息
   */
  String getAddress();
}
