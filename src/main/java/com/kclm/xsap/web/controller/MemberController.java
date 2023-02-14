package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kclm.xsap.entity.MemberEntity;
import com.kclm.xsap.service.MemberService;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/x_member_list.do")
    public String toX_Member_List() {
        return "member/x_member_list";
    }

    @RequestMapping("/memberList.do")
    @ResponseBody
    public List getMemberList() {
        List<MemberVo> memberVoList = memberService.list(new LambdaQueryWrapper<MemberEntity>().orderByDesc(MemberEntity::getCreateTime)).stream().map(item -> {
            return new MemberVo().setMemberName(item.getName() + "(" + item.getPhone() + ")").setGender(item.getSex()).setId(item.getId())
                    .setNote(item.getNote()).setJoiningDate(LocalDate.from(item.getCreateTime())).setCardHold(null);
        }).collect(Collectors.toList());
        return memberVoList;
    }

    @RequestMapping("/x_member_add.do")
    public String toMemberAdd() {
        return "/member/x_member_add";
    }

    @RequestMapping("/memberAdd.do")
    @ResponseBody
    public R MemberAdd(@Valid MemberEntity member, BindingResult result) {
        //数据验证
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : fieldErrors) {
                System.out.println(fe.getField() + "=>" + fe.getDefaultMessage());
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            return R.error(400, "非法参数").put("errorMap", errorMap);
        }
        //手机号重复验证
        MemberEntity one = memberService.getOne(new LambdaQueryWrapper<MemberEntity>().eq(member != null, MemberEntity::getPhone, member.getPhone()));
        if (one != null) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("phone", "手机号已存在");
            return R.error(400, "手机号已经存在").put("errorMap", errorMap);
        }
        //添加会员
        memberService.save(member.setCreateTime(LocalDateTime.now()).setLastModifyTime(LocalDateTime.now()));
        return R.ok("添加成功").setData(member);
    }

    @RequestMapping("/x_member_edit.do")
    @ResponseBody
    public R memberEdit_do(Integer id) {
        MemberEntity one = memberService.getOne(new LambdaQueryWrapper<MemberEntity>().eq(id != null, MemberEntity::getId, id));
        return R.ok().setData(one);
    }

    @RequestMapping("/memberEdit.do")
    @ResponseBody
    public R memberEdit(MemberEntity member){
        MemberEntity m = memberService.getById(member);
        member.setVersion(m.getVersion()+1);
        boolean b = memberService.updateById(member);
        return R.ok("更新成功");
    }

    @RequestMapping("/deleteOne.do")
    @ResponseBody
    public R deleteOne(Integer id){
        boolean b = memberService.removeById(id);
        return R.ok("删除成功");
    }

    @RequestMapping("/x_member_list_details.do")
    public String toMemberListDetails(Integer id, Model model){
        model.addAttribute("ID",id);
        return "/member/x_member_list_details";
    }

    @RequestMapping("/memberDetail.do")
    @ResponseBody
    public R getMemberDetail(Integer id){
        MemberEntity member = memberService.getById(id);
        return R.ok().setData(member);
    }
}
