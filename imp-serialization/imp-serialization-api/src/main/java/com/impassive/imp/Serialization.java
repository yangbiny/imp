package com.impassive.imp;

import javax.annotation.Nullable;

public interface Serialization {

  /**
   * 序列化数据。将数据装换成byte字节数组
   *
   * <p>如果obj为 null，则返回空的直接数组</p>
   *
   * @param obj 需要序列化的对象
   * @return 序列化的结果
   */
  byte[] serialization(Object obj);

  /**
   * 反序列化。将给定的字节数组转换成对应的类型。
   *
   * <p>如果给定的自己额数组为空，则返回null</p>
   *
   * @param bytes 需要转换的直接数组
   * @param tClass 目标类型
   * @param <T> 类型
   * @return 反序列化结果
   */
  @Nullable
  <T> T deserialization(byte[] bytes, Class<T> tClass);

}
