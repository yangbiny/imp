package com.impassive.imp.proxy;

import com.google.common.collect.Lists;
import com.impassive.rpc.invoker.Invoker;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class JdkProxyFactoryTest {

  private JdkProxyFactory jdkProxyFactory;

  @Before
  public void setUp() throws Exception {
    jdkProxyFactory = new JdkProxyFactory();
  }

  @Test
  public void doGetInvoker() throws NoSuchMethodException {
    TestInterfaceImp imp = new TestInterfaceImp();
    Invoker<TestInterfaceImp> invoker = jdkProxyFactory.doGetInvoker(imp,
        TestInterfaceImp.class);
    Class<List> arrayListClass = List.class;
    Method test = imp.getClass().getMethod("test", arrayListClass);
    System.out.println(test);
  }

  @Test
  public void getProxy() {
  }


  private interface TestInterface {

    List<String> test(List<Long> param);
  }

  private class TestInterfaceImp implements TestInterface {

    @Override
    public List<String> test(List<Long> param) {
      return Lists.newArrayList();
    }
  }
}