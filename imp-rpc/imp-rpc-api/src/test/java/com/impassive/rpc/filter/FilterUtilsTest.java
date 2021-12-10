package com.impassive.rpc.filter;

import static org.junit.Assert.*;

import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;
import java.util.List;
import org.junit.Test;

public class FilterUtilsTest {

  @Test
  public void buildAllFilter() {
    List<Filter> filters = FilterUtils.buildAllFilter();

    System.out.println(filters);
  }

  class TestInterface implements Filter{

    @Override
    public Result filter(Invoker<?> invoker, Invocation invocation) {
      return null;
    }
  }
}