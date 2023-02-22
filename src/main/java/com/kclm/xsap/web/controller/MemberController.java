package com.kclm.xsap.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kclm.xsap.dto.convert.*;
import com.kclm.xsap.entity.*;
import com.kclm.xsap.service.*;
import com.kclm.xsap.utils.R;
import com.kclm.xsap.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private ApplicationHome applicationHome = new ApplicationHome(getClass());

    private static final String UPLOAD_IMAGES_MEMBER_IMG = "upload/images/member_img/";
    @Autowired
    private ConsumeRecordService consumeRecordService;
    @Autowired
    private ClassRecordConvertt classRecordConvertt;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ClassRecordService classRecordService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ScheduleRecordService scheduleRecordService;
    @Autowired
    private ReservationRecordVoConvert reservationRecordVoConvert;
    @Autowired
    private ReservationRecordService reservationRecordService;
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private MemberLogService memberLogService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private MemberBindRecordService memberBindRecordService;
    @Autowired
    private MemberService memberService;

    @RequestMapping("modifyMemberImg.do")
    @ResponseBody
    public R modifyMemberImg(Long id, MultipartFile avatarFile) throws IOException {
        MemberEntity member = memberService.getById(id);
        File dir = applicationHome.getDir();
        File realPath = new File(dir, UPLOAD_IMAGES_MEMBER_IMG);
        if (!realPath.exists()) {
            log.info("创建上传文件目录结构");
            realPath.mkdirs();
        }
        String originalFilename = avatarFile.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + substring;
        member.setAvatarUrl(fileName);
        memberService.updateById(member);
        avatarFile.transferTo(new File(realPath, fileName));
        return R.ok("修改头像成功").put("avatarUrl", fileName);
    }

    @RequestMapping("/toSearch.do")
    @ResponseBody
    public R searchMember() {
        List<MemberEntity> memberEntities = memberService.list();
        return R.ok().put("value", memberEntities);
    }

    @RequestMapping("/x_member_list.do")
    public String toX_Member_List() {
        return "member/x_member_list";
    }

    @RequestMapping("/memberList.do")
    @ResponseBody
    public List getMemberList() {
        List<MemberEntity> list = memberService.list(new LambdaQueryWrapper<MemberEntity>().orderByDesc(MemberEntity::getCreateTime));
        List<MemberVo> memberVoList = list.stream().map(item -> {
            List<String> cardNameList = memberBindRecordService.list(new LambdaQueryWrapper<MemberBindRecordEntity>().eq(MemberBindRecordEntity::getMemberId, item.getId()))
                    .stream().map(item2 -> {
                        MemberCardEntity card = memberCardService.getById(item2.getCardId());
                        return card.getName();
                    }).collect(Collectors.toList());
            String[] cardName = cardNameList.toArray(new String[cardNameList.size()]);
//            String cardName = String.join(",", cardNameList);
            return new MemberVo().setMemberName(item.getName() + "(" + item.getPhone() + ")").setGender(item.getSex()).setId(item.getId())
                    .setNote(item.getNote()).setJoiningDate(LocalDate.from(item.getCreateTime())).setCardHold(cardName);
        }).collect(Collectors.toList());

//        List<MemberVo> memberVoList = memberService.list(new LambdaQueryWrapper<MemberEntity>().orderByDesc(MemberEntity::getCreateTime)).stream().map(item -> {
//            return new MemberVo().setMemberName(item.getName() + "(" + item.getPhone() + ")").setGender(item.getSex()).setId(item.getId())
//                    .setNote(item.getNote()).setJoiningDate(LocalDate.from(item.getCreateTime())).setCardHold(null);
//        }).collect(Collectors.toList());
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
    public R memberEdit(MemberEntity member) {
        MemberEntity m = memberService.getById(member);
        member.setVersion(m.getVersion() + 1);
        memberService.updateById(member);
        return R.ok("更新成功");
    }

    @RequestMapping("/deleteOne.do")
    @ResponseBody
    public R deleteOne(Integer id) {
        LambdaUpdateWrapper uw = new LambdaUpdateWrapper<MemberEntity>().eq(MemberEntity::getId,id).set(MemberEntity::getIsDeleted,1).set(MemberEntity::getLastModifyTime,LocalDateTime.now());
        memberService.update(uw);
        return R.ok("删除成功");
    }

    @RequestMapping("/x_member_list_details.do")
    public String toMemberListDetails(Integer id, Model model) {
        model.addAttribute("ID", id);
        return "/member/x_member_list_details";
    }

    @RequestMapping("/memberDetail.do")
    @ResponseBody
    public R getMemberDetail(Long id) {
        MemberEntity member = memberService.getById(id);
        return R.ok().setData(member);
    }

    @RequestMapping("/toSearcherAll.do")
    @ResponseBody
    public R searcherAll() {
        List<MemberEntity> memberEntities = memberService.list();
        return R.ok().put("value", memberEntities);
    }

    @RequestMapping("/cardInfo.do")
    @ResponseBody
    public R cardInfo(Long id) {
        List<CardInfoVo> cardInfoVos = memberBindRecordService.list(new LambdaQueryWrapper<MemberBindRecordEntity>()
                .eq(id != null, MemberBindRecordEntity::getMemberId, id)).stream().map(item -> {
            MemberCardEntity card = memberCardService.getById(item.getCardId());
            CardInfoVo cardInfoVo = new CardInfoVo().setBindId(item.getId()).setCreateTime(item.getCreateTime()).setActiveStatus(item.getActiveStatus())
                    .setLastModifyTime(item.getLastModifyTime()).setName(card.getName()).setType(card.getType()).setValidDay(item.getValidDay())
                    .setTotalCount(item.getValidCount()).setDueTime(item.getCreateTime().plusDays(item.getValidDay()));
            return cardInfoVo;
        }).collect(Collectors.toList());

        return R.ok().setData(cardInfoVos);
    }

    //未完成
    @RequestMapping("/consumeInfo.do")
    @ResponseBody
    public R consumeInfo(Long id) {
        List<MemberBindRecordEntity> mbre = memberBindRecordService.list(new LambdaQueryWrapper<MemberBindRecordEntity>()
                .eq(id != null, MemberBindRecordEntity::getMemberId, id));
        List<ConsumeInfoVo> consumeInfoVos = new ArrayList<>();
        mbre.forEach(item -> {
            Long bindId = item.getId();
            String cardName = memberCardService.getOne(new LambdaQueryWrapper<MemberCardEntity>().eq(MemberCardEntity::getId, item.getCardId())).getName();
            List<MemberLogEntity> memberLogEntities = memberLogService.list(new LambdaQueryWrapper<MemberLogEntity>().eq(MemberLogEntity::getMemberBindId, bindId));
            memberLogEntities.forEach(item2 -> {
                if (item2.getType().contains("扣费")) {
                    ConsumeRecordEntity consumeRecord = consumeRecordService.getOne(new LambdaQueryWrapper<ConsumeRecordEntity>().eq(ConsumeRecordEntity::getLogId, item2.getId()));
                    ConsumeInfoVo consumeInfoVo = new ConsumeInfoVo().setConsumeId(item2.getId()).setCardName(cardName)
                            .setOperateTime(item2.getCreateTime()).setCardCountChange(consumeRecord.getCardCountChange()).setTimesRemainder(item.getValidCount())
                            .setMoneyCostBigD(item2.getInvolveMoney()).setMoneyCost("-￥" + item2.getInvolveMoney().toString())
                            .setOperateType(item2.getType()).setOperator(item2.getOperator()).setNote(consumeRecord.getNote()).setCreateTime(item.getCreateTime())
                            .setLastModifyTime(item.getCreateTime());
                    consumeInfoVos.add(consumeInfoVo);
                }
            });
        });
        return R.ok().setData(consumeInfoVos);
    }

    @RequestMapping("/reserveInfo.do")
    @ResponseBody
    public R reserveInfo(Long id) {
        List<ReservationRecordEntity> rreList = reservationRecordService.list(new LambdaQueryWrapper<ReservationRecordEntity>().eq(id != null, ReservationRecordEntity::getMemberId, id));
        List<ReservationRecordVo> reservationRecordVos = rreList.stream().map(item -> {
            ScheduleRecordEntity sre = scheduleRecordService.getById(item.getScheduleId());
            LocalDate startDate = sre.getStartDate();
            LocalTime classTime = sre.getClassTime();
            LocalDateTime reserveTime = LocalDateTime.of(startDate, classTime);
            CourseEntity course = courseService.getById(sre.getCourseId());
            ReservationRecordVo reservationRecordVo = reservationRecordVoConvert.entity2Vo(item, course, sre, reserveTime);
            return reservationRecordVo;
        }).collect(Collectors.toList());
        return R.ok().setData(reservationRecordVos);
    }

    @RequestMapping("/classInfo.do")
    @ResponseBody
    public R classInfo(Long id) {
        List<ClassRecordEntity> creList = classRecordService.list(new LambdaQueryWrapper<ClassRecordEntity>().eq(id != null, ClassRecordEntity::getMemberId, id));
        List<ClassRecordVoo> classRecordVoos = creList.stream().map(item -> {
            ScheduleRecordEntity sre = scheduleRecordService.getById(item.getScheduleId());
            CourseEntity ce = courseService.getById(sre.getCourseId());
            EmployeeEntity ee = employeeService.getById(sre.getTeacherId());
            ReservationRecordEntity rre = reservationRecordService.getOne(new LambdaQueryWrapper<ReservationRecordEntity>().eq(ReservationRecordEntity::getMemberId, id)
                    .eq(ReservationRecordEntity::getScheduleId, sre.getId()));
            LocalDateTime classTime = LocalDateTime.of(sre.getStartDate(), sre.getClassTime());
            Integer reserveNums = rre == null ? 1 : rre.getReserveNums();
            ClassRecordVoo classRecordVoo = classRecordConvertt.entity2Vo(item, sre,reserveNums, ce, ee.getName(), classTime);
            return classRecordVoo;
        }).collect(Collectors.toList());
        return R.ok().setData(classRecordVoos);
    }
}
