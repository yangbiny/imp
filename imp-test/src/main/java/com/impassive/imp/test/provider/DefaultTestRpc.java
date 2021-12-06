package com.impassive.imp.test.provider;

import com.impassive.imp.test.Result;
import com.impassive.imp.test.TestRpc;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

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
    if (CollectionUtils.isEmpty(param)) {
      return Collections.emptyList();
    }
    return param.stream().map(String::valueOf).collect(Collectors.toList());
  }
}
