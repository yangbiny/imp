package com.impassive.rpc.invocation;

/**
 * @author impassivey
 */
public interface Invocation {

  /**
   * 返回执行对象的接口全路径名
   *
   * @return 执行对象的接口全路径名
   */
  String getServiceName();

  /**
   * 获取待执行方法的方法名称
   *
   * @return 方法名称
   */
  String getMethodName();

  /**
   * 获取执行方法的参数类型
   *
   * @return 参数类型
   */
  Class<?>[] getParamTypes();

  /**
   * 参数的实际类型。例如：如果参数是list，则 paramType是list，该值可能是ArrayList，也可能是LinkedList
   *
   * @return 参数的实际类型
   */
  Class<?>[] argumentsType();

  /**
   * 获取执行方法的参数信息
   *
   * @return 参数信息
   */
  Object[] getParams();
}
