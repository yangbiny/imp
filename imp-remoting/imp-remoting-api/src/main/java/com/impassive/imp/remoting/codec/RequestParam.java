package com.impassive.imp.remoting.codec;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用于记录方法参数的信息。分别包含了 参数的类型以及参数的值
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestParam implements Serializable {

  private static final long serialVersionUID = 2660484466036339268L;

  private Class<?> classType;

  private String value;

}
