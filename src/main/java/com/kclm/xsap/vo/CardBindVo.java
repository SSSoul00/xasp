package com.kclm.xsap.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CardBindVo {
    private String name;
    private Long id;
}
