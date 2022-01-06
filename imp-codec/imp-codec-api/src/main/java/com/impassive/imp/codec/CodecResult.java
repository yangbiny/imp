package com.impassive.imp.codec;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodecResult implements Serializable {

  private static final long serialVersionUID = -1249636383265611460L;

  private Long requestId;

  private Class<?> classType;

  private String value;

}
