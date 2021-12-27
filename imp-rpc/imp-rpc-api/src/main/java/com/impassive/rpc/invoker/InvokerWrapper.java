package com.impassive.rpc.invoker;

import com.impassive.imp.common.Url;
import com.impassive.rpc.filter.Filter;
import com.impassive.rpc.filter.FilterUtils;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.result.Result;
import java.util.List;
import lombok.Getter;

/**
 * @author impassivey
 */
public class InvokerWrapper<T> implements Invoker<T> {

  private final Invoker<T> invoker;

  @Getter
  private final Url url;

  public InvokerWrapper(Invoker<T> invoker, Url url) {
    this.invoker = invoker;
    this.url = url;
  }

  @Override
  public Class<T> getInterfaceClass() {
    return (Class<T>) url.getClassType();
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    List<Filter> filters = FilterUtils.buildAllFilter();
    Invoker<T> lastInvoker = invoker;
    for (Filter filter : filters) {
      final Invoker<T> next = lastInvoker;
      lastInvoker = new Invoker<T>() {
        @Override
        public void destroy() {
          invoker.destroy();
        }

        @Override
        public Class<T> getInterfaceClass() {
          return invoker.getInterfaceClass();
        }

        @Override
        public Result invoke(Invocation invocation1) throws Throwable {
          return filter.filter(next, invocation1);
        }
      };
    }
    return lastInvoker.invoke(invocation);
  }

  @Override
  public void destroy() {
    invoker.destroy();
  }
}
