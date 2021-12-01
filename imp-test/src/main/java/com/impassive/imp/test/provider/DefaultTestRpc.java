package com.impassive.imp.test.provider;

import com.impassive.imp.test.Result;
import com.impassive.imp.test.TestRpc;

public class DefaultTestRpc implements TestRpc {

  @Override
  public String test(String arg) {
    return arg;
  }

  @Override
  public Result test(Param param) {
    return new Result();
  }
}
