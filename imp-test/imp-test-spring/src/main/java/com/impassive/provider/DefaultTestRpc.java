package com.impassive.provider;

import com.impassive.Param;
import com.impassive.Result;
import com.impassive.TestRpc;
import com.impassive.rpc.context.RpcContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class DefaultTestRpc implements TestRpc {

  @Override
  public String test(String arg) {
    RpcContext rpcContext = RpcContext.rpcContextInstance();
    System.out.println("context : " + rpcContext.getInvocation());
    return arg;
  }

  @Override
  public Result test(Param param) {
    Result result = new Result();
    result.setObj(param.getDate() + " : result");
    return result;
  }

  @Override
  public List<String> test(ArrayList<Long> param) {
    if (CollectionUtils.isEmpty(param)) {
      return Collections.emptyList();
    }
    System.out.println("这里是 array list");
    return param.stream().map(String::valueOf).collect(Collectors.toList());
  }

  @Override
  public List<String> test(List<Long> param) {
    System.out.println("这里是 list");
    return param.stream().map(item -> item + " : list")
        .collect(Collectors.toList());
  }
}
