package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.CourseCardEntity;
import com.kclm.xsap.entity.CourseEntity;
import com.kclm.xsap.entity.MemberCardEntity;
import com.kclm.xsap.service.CourseCardService;
import com.kclm.xsap.service.CourseService;
import com.kclm.xsap.service.MemberCardService;
import com.kclm.xsap.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/card")
@Slf4j
public class CardController {
    @Autowired
    private CourseCardService courseCardService;
    @Autowired
    private MemberCardService memberCardService;

    @RequestMapping("/x_member_card.do")
    public String toMemberCard() {
        return "/member/x_member_card";
    }

    @RequestMapping("/cardList.do")
    @ResponseBody
    public R getCardList() {
        List<MemberCardEntity> cardList = memberCardService.list();
        R r = new R();
        r.setData(cardList);
        return r;
    }

    @RequestMapping("/x_member_add_card.do")
    public String toMemberAddCard() {
        return "/member/x_member_add_card";
    }

    @RequestMapping("/cardAdd.do")
    @ResponseBody
    @Transactional
    public R cardAdd(MemberCardEntity card, @RequestParam("courseListStr") List<Long> ids) {
        log.info("接收数据：{},{}", card, ids.toArray());
        boolean save = memberCardService.save(card.setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now()));
        List<CourseCardEntity> courseCardEntities = ids.stream().map(item -> {
            return new CourseCardEntity().setCardId(card.getId()).setCourseId(item);
        }).collect(Collectors.toList());
        boolean b = courseCardService.saveBatch(courseCardEntities);
        return R.ok("添加成功").setData(card);
    }
}
