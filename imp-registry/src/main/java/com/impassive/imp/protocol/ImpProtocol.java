package com.impassive.imp.protocol;

import com.impassive.imp.invoker.Invoker;
import com.impassive.imp.invoker.InvokerWrapper;
import com.impassive.imp.registry.AbstractRegistryFactory;
import com.impassive.imp.registry.Registry;
import com.impassive.imp.registry.RegistryFactory;

/** @author impassivey */
public class ImpProtocol implements Protocol {

  @Override
  public <T> void export(Invoker<T> invoker) {
    doExport(invoker);
  }

  private <T> void doExport(Invoker<T> invoker) {
    if (!(invoker instanceof InvokerWrapper)) {
      return;
    }
    InvokerWrapper<T> invokerWrapper = (InvokerWrapper<T>) invoker;
    openService(invokerWrapper.getUrl());
    registry(invokerWrapper);
  }

  private <T> void openService(Url url) {

  }

  private  <T> void registry(InvokerWrapper<T> invokerWrapper) {
    final Url url = invokerWrapper.getUrl();
    if (!url.getRegister()) {
      return;
    }
    final RegistryFactory registryFactory =
        AbstractRegistryFactory.getRegistryFactory(url.getRegistryType());
    final Registry registry = registryFactory.getRegistry(url);
    registry.registry(url);
  }
}
