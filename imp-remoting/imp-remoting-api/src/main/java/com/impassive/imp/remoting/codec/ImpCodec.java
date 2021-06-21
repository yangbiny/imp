package com.impassive.imp.remoting.codec;

import io.netty.buffer.ByteBuf;

/**
 * @author impassivey
 */
public class ImpCodec extends AbstractCodec {

  @Override
  public void encode(ByteBuf out, Object message) {

  }

  @Override
  public Object decode(ByteBuf in) {
    return null;
  }
}
