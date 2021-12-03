package com.impassive.imp.test;

import com.impassive.imp.test.provider.Param;
import java.util.List;
import org.junit.Test;

/** @author impassivey */
public interface TestRpc {

  /**
   * test
   *
   * @param arg 参数信息
   * @return 返回数据
   */
  String test(String arg);

  Result test(Param param);

  List<String> test(List<Long> param);

}
