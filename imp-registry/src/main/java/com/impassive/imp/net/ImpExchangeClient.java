package com.impassive.imp.net;

import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public class ImpExchangeClient implements ExchangeClient {

  private ExchangeChannel channel;

  @Override
  public CompletableFuture<Object> request(Object req) {
    return channel.request(req);
  }
}
