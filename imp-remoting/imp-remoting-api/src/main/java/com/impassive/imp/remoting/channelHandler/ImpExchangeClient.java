package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Client;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.imp.remoting.ExchangeClient;
import java.util.concurrent.CompletableFuture;

/** @author impassivey */
public class ImpExchangeClient implements ExchangeClient {

  private final Client client;

  private final ExchangeChannel channel;

  public ImpExchangeClient(Client client) {
    this.client = client;
    this.channel = new ImpExchangeChannel(client);
  }

  @Override
  public CompletableFuture<Object> request(Object req) {
    return channel.request(req);
  }
}
