package com.impassive.imp.invoker;

/** @author impassivey */
public class ImpResult implements Result {

  private Object result;

  @Override
  public Object getResult() {
    return result;
  }

  @Override
  public void setResult(Object object) {
    result = object;
  }
}
