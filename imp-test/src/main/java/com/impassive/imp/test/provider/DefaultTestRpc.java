package com.impassive.imp.test.provider;

import com.impassive.imp.test.Result;
import com.impassive.imp.test.TestRpc;
import java.util.List;

public class DefaultTestRpc implements TestRpc {

  @Override
  public String test(String arg) {
    return arg;
  }

  @Override
  public Result test(Param param) {
    Result result = new Result();
    result.setObj(param.getDate() + " : result");
    return result;
  }

  @Override
  public List<String> test(List<Long> param) {
    return null;
  }
}
