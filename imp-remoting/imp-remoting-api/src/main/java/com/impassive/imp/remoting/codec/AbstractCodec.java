package com.impassive.imp.remoting.codec;

import com.impassive.imp.Serialization;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.ChannelBuffer;
import com.impassive.imp.remoting.Codec;
import com.impassive.imp.util.json.JsonTools;

/**
 * @author impassivey
 */
public abstract class AbstractCodec implements Codec {

  private static final String SERIALIZE_KEY = "serialize";

  protected abstract void doEncode(Url url, ChannelBuffer out, Object message);

  protected abstract Object doDecode(Url url, ChannelBuffer in);

  protected CodecRequest convertToRequestParam(Object object) {
    if (object == null) {
      return null;
    }
    return new CodecRequest(object.getClass(), JsonTools.writeToJson(object));
  }

  @Override
  public void encode(Url url, ChannelBuffer out, Object message) {
    doEncode(url, out, message);
  }

  @Override
  public Object decode(Url url, ChannelBuffer in) {
    return doDecode(url, in);
  }

  protected byte[] serialize(Url url, Object object) {
    Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
        .getExtensionByName(url.getParam(SERIALIZE_KEY));
    return serialization.serialization(object);
  }

  protected <T> T deserialize(Url url, byte[] bytes, Class<T> classInfo) {
    Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
        .getExtensionByName(url.getParam(SERIALIZE_KEY));
    return serialization.deserialization(bytes, classInfo);
  }
}
