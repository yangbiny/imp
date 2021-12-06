package com.impassive.imp.test;

import com.impassive.imp.test.provider.Param;
import java.util.ArrayList;
import java.util.List;

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

  List<String> test(ArrayList<Long> param);

  List<String> test(List<Long> param);
}
