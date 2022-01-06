package com.impassive.invocation;

import com.impassive.imp.common.LimiterInfo;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.Extension;
import com.impassive.imp.util.limiter.RateLimiter;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.invoker.Invoker;
import com.impassive.rpc.result.Result;

@Extension(active = false, order = -1)
public class InvokerLimiterWrapper<T> implements Invoker<T> {

  private final Invoker<T> invoker;

  private final Url url;

  private final RateLimiter rateLimiter;

  public InvokerLimiterWrapper(Invoker<T> invoker) {
    this.invoker = invoker;
    this.url = invoker.getUrl();
    this.rateLimiter = buildRateLimiter(url);
  }

  private RateLimiter buildRateLimiter(Url url) {
    LimiterInfo limiterInfo = url.getLimiterInfo();
    return new RateLimiter(limiterInfo);
  }

  @Override
  public void destroy() {
    invoker.destroy();
  }

  @Override
  public Class<T> getInterfaceClass() {
    return invoker.getInterfaceClass();
  }

  @Override
  public Result invoke(Invocation invocation) throws Throwable {
    // 限流的话，需要先申请资源，申请到了才允许执行
    rateLimiter.acquire();
    return invoker.invoke(invocation);
  }

  @Override
  public Url getUrl() {
    return url;
  }
}
