package com.impassive.rpc.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterMeta {

  private Filter filter;

  private boolean active;

  private Integer order;


}
