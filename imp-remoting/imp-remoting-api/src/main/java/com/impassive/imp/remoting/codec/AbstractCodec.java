package com.impassive.imp.remoting.codec;

import com.impassive.imp.Serialization;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.util.json.JsonTools;
import io.netty.buffer.ByteBuf;

/**
 * @author impassivey
 */
public abstract class AbstractCodec implements Codec {

  private static final String SERIALIZE_KEY = "serialize";

  protected abstract void doEncode(Url url, ByteBuf out, Object message);

  protected abstract Object doDecode(Url url,ByteBuf in);

  protected CodecRequest convertToRequestParam(Object object) {
    if (object == null) {
      return null;
    }
    return new CodecRequest(object.getClass(), JsonTools.writeToJson(object));
  }

  @Override
  public void encode(Url url, ByteBuf out, Object message) {
    doEncode(out, message);
  }

  @Override
  public Object decode(Url url, ByteBuf in) {
    return doDecode(in);
  }

  protected byte[] serialize(Url url, Object object) {
    Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
        .getExtensionByName(url.getParam(SERIALIZE_KEY));
    return serialization.serialization(object);
  }
}
