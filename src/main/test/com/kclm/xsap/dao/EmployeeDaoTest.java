package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kclm.xsap.entity.EmployeeEntity;
import com.kclm.xsap.entity.MemberEntity;
import com.kclm.xsap.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class EmployeeDaoTest {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private MemberService memberService;

    @Test
    public void Test01(){
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 8L);
        List<EmployeeEntity> employeeEntities = employeeDao.selectTeacherNameListByIds(ids);
        employeeEntities.forEach(item->{
            System.out.println(item.getName());
        });
        System.out.println("===========");
        EmployeeEntity employee = employeeDao.selectTeacherNameById(8L);
        System.out.println(employee);
    }

    @Test
    public void Test02(){

    }
}
