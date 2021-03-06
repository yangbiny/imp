package com.impassive.imp.remoting;

import com.impassive.imp.common.Url;
import com.impassive.imp.common.extension.SPI;

/**
 * @author impassivey
 */
@SPI(name = "impCodec")
public interface Codec {

  /**
   * 对数据进行编码，将数据转换为 直接数组，并写入到byteBuffer中
   *
   * @param out 写入的对象
   * @param message 需要写入的对象
   */
  void encode(Url url, ChannelBuffer out, Object message);

  /**
   * 对输入的二进制码进行解码，将二进制码转换为对应的数据类型
   *
   * @param in 输入的直接数组
   * @return 转换后的对象
   * @throws Exception 如果解析失败会直接抛出异常
   */
  Object decode(Url url, ChannelBuffer in) throws Exception;
}
