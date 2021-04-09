package com.impassive.imp.protocol;

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
    System.out.println(invoker);
  }

  public void registry() {}
}
