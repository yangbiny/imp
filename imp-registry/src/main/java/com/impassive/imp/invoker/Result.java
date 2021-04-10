package com.impassive.imp.invoker;

/** @author impassivey */
public interface Result {

  /**
   * 获取返回的结果
   *
   * @return 结果信息
   */
  Object getResult();

  /**
   * 设置返回值的信息
   *
   * @param object 返回的对象
   */
  void setResult(Object object);
}
