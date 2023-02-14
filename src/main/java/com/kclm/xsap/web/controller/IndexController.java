package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.EmployeeEntity;
import com.kclm.xsap.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@RequestMapping("/index")
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/toRegister")
    public String toRegister() {
        return "x_register";
    }

    @RequestMapping("/register")
    public String register(String userName,String password, String pwd2, Model model){
        log.info("注册参数：用户名-》{}  密码-》{}  二次密码-》{}",userName,password,pwd2);
        EmployeeEntity one = employeeService.getOne(new LambdaQueryWrapper<EmployeeEntity>().eq(userName != null, EmployeeEntity::getName, userName));
        if (one!=null){
            model.addAttribute("CHECK_TYPE_ERROR", 0);
            return "x_register";
        }
        if (password.equals(pwd2)) {
            EmployeeEntity employee = new EmployeeEntity().setName(userName).setRolePassword(password).setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now());
            employeeService.save(employee);
            return "x_login";
        } else {
            model.addAttribute("CHECK_TYPE_ERROR", 1);
            return "x_register";
        }
    }

    @RequestMapping("/x_index_home.do")
    public String toIndexHome(){
        return "x_index_home";
    }
}
