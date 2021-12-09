package com.impassive.imp.remoting.codec;

import com.impassive.imp.util.json.JsonTools;

/**
 * @author impassivey
 */
public abstract class AbstractCodec implements Codec {

  protected CodecRequest convertToRequestParam(Object object) {
    if (object == null) {
      return null;
    }
    return new CodecRequest(object.getClass(), JsonTools.writeToJson(object));
  }

}
