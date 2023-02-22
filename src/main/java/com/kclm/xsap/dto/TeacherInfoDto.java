package com.kclm.xsap.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TeacherInfoDto {
    private String name;
    private Integer id;
}
