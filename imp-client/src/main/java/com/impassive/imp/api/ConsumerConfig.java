package com.impassive.imp.api;

import com.impassive.imp.config.BaseConsumerConfig;
import lombok.Getter;

/**
 * @author impassivey
 */
@Getter
public class ConsumerConfig<T> extends BaseConsumerConfig {

  private String groupName;

  private Class<T> classType;


  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setClassType(Class<T> classType) {
    this.classType = classType;
  }


}
