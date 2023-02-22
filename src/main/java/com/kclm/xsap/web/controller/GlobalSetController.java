package com.kclm.xsap.web.controller;

import com.kclm.xsap.entity.GlobalReservationSetEntity;
import com.kclm.xsap.service.GlobalReservationSetService;
import com.kclm.xsap.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/globalSet")
public class GlobalSetController {
    @Autowired
    private GlobalReservationSetService globalReservationSetService;

    @RequestMapping("globalSetUpdate.do")
    @ResponseBody
    public R globalSetUpdate(GlobalReservationSetEntity globalReservationSet){
        globalReservationSetService.updateById(globalReservationSet);
        return R.ok("修改成功");
    }

    @RequestMapping("/x_course_reservation.do")
    public String toCourseReservation(Model model){
        GlobalReservationSetEntity globalReservationSet = globalReservationSetService.getById(1);
        String endTimeStr = globalReservationSet.getEndTime().toString();
        String cancelTimeStr = globalReservationSet.getCancelTime().toString();
        model.addAttribute("GLOBAL_SET",globalReservationSet);
        model.addAttribute("endTimeStr",endTimeStr);
        model.addAttribute("cancelTimeStr",cancelTimeStr);
        return "course/x_course_reservation";
    }
}
