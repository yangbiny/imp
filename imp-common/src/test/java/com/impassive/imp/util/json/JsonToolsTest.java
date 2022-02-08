package com.impassive.imp.util.json;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

public class JsonToolsTest {

  @Test
  public void writeToJson() {
    List<TestClass> list = new ArrayList<>();
    list.add(new TestClass(1L, "name"));
    System.out.println(JsonTools.writeToJson(list));
  }

  @Test
  public void readFromJson() {
    String value = "[{\"id\":1,\"name\":\"name\"}]";
    Type type = new ParameterizedTypeImpl(new Type[]{TestClass.class}, null, List.class);
    List<TestClass> testClasses = JsonTools.readFromJson(value, type);
    System.out.println(testClasses);
  }

  @Test
  public void testReadFromJson() {
  }

  @Test
  public void readFromJsonList() {
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class TestClass {

    private Long id;

    private String name;


  }
}