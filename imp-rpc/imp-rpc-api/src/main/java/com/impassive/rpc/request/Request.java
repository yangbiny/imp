package com.impassive.rpc.request;

/** @author impassivey */
public interface Request {

  /**
   * 设置请求的ID
   *
   * @param id 请求ID
   */
  void setRequestId(long id);

  /**
   * 获取请求的ID
   *
   * @return 请求ID
   */
  Long getRequestId();
}
