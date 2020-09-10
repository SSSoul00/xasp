package com.kclm.xsap.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName(value = "t_consume_record",resultMap = "TConsumeRecordMap")
public class TConsumeRecord extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 *	用来封装会员实体
	 */
	@TableField(exist = false)
    private TMember member;
	
	/**
	 * 	关联的会员
    */
	private Integer memberId;
	
    /**
    * 操作类型
    */
    private String operateType;

    /**
    * 卡次变化
    */
    private Integer cardCountChange;

    /**
    * 有效天数变化
    */
    private Integer cardDayChange;

    /**
    * 操作员
    */
    private String operator;

    private String note;

}