package com.impassive.imp.remoting.codec;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.impassive.imp.Serialization;
import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.ExtensionLoader;
import com.impassive.imp.remoting.ChannelBuffer;
import com.impassive.imp.remoting.Codec;
import com.impassive.imp.util.json.JsonTools;
import java.lang.reflect.Type;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author impassivey
 */
@Slf4j
public abstract class AbstractCodec implements Codec {

  //private final Logger log = LoggerFactory.getLogger(AbstractCodec.class);

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
    try {
      doEncode(url, out, message);
    } catch (Exception e) {
      log.error("encode has error : ", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object decode(Url url, ChannelBuffer in) {
    try {
      return doDecode(url, in);
    } catch (Exception e) {
      log.error("decode has error : ", e);
      throw new RuntimeException(e);
    }
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

  protected <T> List<T> deserializeList(Url url, byte[] bytes, Class<T> classInfo) {
    Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
        .getExtensionByName(url.getParam(SERIALIZE_KEY));
    Type classInfoType = new ParameterizedTypeImpl(new Type[]{classInfo}, null, List.class);
    return serialization.deserialization(bytes, classInfoType);
  }

}
