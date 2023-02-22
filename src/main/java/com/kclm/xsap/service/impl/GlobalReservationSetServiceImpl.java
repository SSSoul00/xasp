package com.kclm.xsap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kclm.xsap.dao.GlobalReservationSetDao;
import com.kclm.xsap.entity.GlobalReservationSetEntity;
import com.kclm.xsap.service.GlobalReservationSetService;
import org.springframework.stereotype.Service;

@Service
public class GlobalReservationSetServiceImpl extends ServiceImpl<GlobalReservationSetDao, GlobalReservationSetEntity> implements GlobalReservationSetService {
}
