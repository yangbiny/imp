package com.impassive.imp.remoting.codec;

import com.impassive.imp.util.json.JsonTools;
import io.netty.buffer.ByteBuf;

/**
 * @author impassivey
 */
public abstract class AbstractCodec implements Codec {

  protected abstract void doEncode(ByteBuf out, Object message);

  protected abstract Object doDecode(ByteBuf in);

  protected CodecRequest convertToRequestParam(Object object) {
    if (object == null) {
      return null;
    }
    return new CodecRequest(object.getClass(), JsonTools.writeToJson(object));
  }

  @Override
  public void encode(ByteBuf out, Object message) {
    doEncode(out, message);
  }

  @Override
  public Object decode(ByteBuf in) {
    return doDecode(in);
  }
}
