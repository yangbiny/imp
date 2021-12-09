package com.impassive.imp.remoting.channelHandler;

import com.impassive.imp.remoting.Channel;
import com.impassive.imp.remoting.ChannelHandler;
import com.impassive.imp.remoting.ExchangeChannel;
import com.impassive.imp.remoting.ExchangeHandler;
import com.impassive.rpc.invocation.Invocation;
import com.impassive.rpc.request.Request;
import com.impassive.rpc.result.Result;
import java.util.concurrent.CompletableFuture;

/**
 * @author impassivey
 */
public class HeaderExchangeHandler implements ChannelHandler {

  protected ExchangeHandler exchangeHandler;

  public HeaderExchangeHandler(ExchangeHandler exchangeHandler) {
    this.exchangeHandler = exchangeHandler;
  }

  @Override
  public void connection(Channel channel) {
    exchangeHandler.connection(channel);
  }

  @Override
  public void receive(Channel channel, Object msg) throws Exception {
    // 此时收到的数据是编码过后的  RpcInvocation
    if (msg instanceof Invocation) {
      handlerRequest(channel, msg);
    } else if (msg instanceof Result) {
      exchangeHandler.receive(channel, msg);
    }
  }

  private void handlerRequest(Channel channel, Object msg) {
    ExchangeChannel exchangeChannel = ExchangeChannelHandler.getOrAddExchangeHandler(channel);
    try {
      CompletableFuture<Object> reply = exchangeHandler.reply(exchangeChannel, msg);
      reply.whenComplete(
          (res, t) -> {
            Request request = (Request) msg;
            Result result = (Result) res;
            result.setRequestId(request.getRequestId());
            channel.send(result);

          });
    } catch (Throwable throwable) {
      throw new RuntimeException("unknown exception", throwable);
    }
  }

  @Override
  public void close(Channel channel) {
    exchangeHandler.close(channel);
  }
}
