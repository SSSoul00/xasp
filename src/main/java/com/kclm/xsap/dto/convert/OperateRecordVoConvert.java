package com.kclm.xsap.dto.convert;

import com.kclm.xsap.entity.ConsumeRecordEntity;
import com.kclm.xsap.entity.MemberLogEntity;
import com.kclm.xsap.entity.RechargeRecordEntity;
import com.kclm.xsap.vo.OperateRecordVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OperateRecordVoConvert {
    @Mappings({
            @Mapping(source = "memberLog.id",target = "id"),
            @Mapping(source = "memberLog.createTime", target = "operateTime"),
            @Mapping(source = "memberLog.type", target = "operateType"),
            @Mapping(source = "rechargeRecord.addCount", target = "addCount"),
            @Mapping(source = "rechargeRecord.addCount", target = "changeCount"),
            @Mapping(source = "rechargeRecord.receivedMoney", target = "receivedMoney"),
            @Mapping(source = "changeMoney", target = "changeMoney"),
            @Mapping(source = "memberLog.operator", target = "operator"),
            @Mapping(source = "memberLog.note", target = "cardNote"),
            @Mapping(source = "memberLog.cardActiveStatus", target = "status"),
            @Mapping(source = "memberLog.createTime", target = "createTime"),
            @Mapping(source = "memberLog.lastModifyTime", target = "lastModifyTime"),
    })
    OperateRecordVo rechargeEntity2Vo(MemberLogEntity memberLog, RechargeRecordEntity rechargeRecord, String changeMoney);

    @Mappings({
            @Mapping(source = "memberLog.id",target = "id"),
            @Mapping(source = "memberLog.createTime", target = "operateTime"),
            @Mapping(source = "memberLog.type", target = "operateType"),
            @Mapping(source = "consumeRecord.cardCountChange", target = "cardCountChange"),
            @Mapping(source = "consumeRecord.cardCountChange", target = "changeCount"),
            @Mapping(source = "consumeRecord.moneyCost", target = "moneyCost"),
            @Mapping(source = "changeMoney", target = "changeMoney"),
            @Mapping(source = "memberLog.operator", target = "operator"),
            @Mapping(source = "memberLog.note", target = "cardNote"),
            @Mapping(source = "memberLog.cardActiveStatus", target = "status"),
            @Mapping(source = "memberLog.createTime", target = "createTime"),
            @Mapping(source = "memberLog.lastModifyTime", target = "lastModifyTime"),
    })
    OperateRecordVo consumerEntity2Vo(MemberLogEntity memberLog, ConsumeRecordEntity consumeRecord, String changeMoney);
}
