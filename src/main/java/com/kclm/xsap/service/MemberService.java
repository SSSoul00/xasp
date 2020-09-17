package com.kclm.xsap.service;

import java.util.List;

import com.kclm.xsap.entity.TConsumeRecord;
import com.kclm.xsap.entity.TMember;
import com.kclm.xsap.entity.TMemberBindRecord;
import com.kclm.xsap.entity.TReservationRecord;

/**
 * 
 * @author harima
 * @since JDK11.0
 * @CreateDate 2020年9月15日 上午11:18:38 
 * @description 此类用来描述了会员管理业务
 *
 */
public interface MemberService {
	
	boolean save(TMember member);
	
	boolean deleteById(Integer id);
	
	boolean update(TMember member);
	
	/**
	 *  获取所有会员信息
	 * @return List<TMember>。会员信息的结果集
	 */
	List<TMember> findAll();
	
	/**
	 *  分页查询。获取所有会员信息
	 * @param currentPage
	 * @param pageSize
	 * @return List<TMember>。会员信息的结果集
	 */
	List<TMember> findAllByPage(Integer currentPage,Integer pageSize);
	
	/**
	 * 根据条件搜索匹配的会员id
	 * @param condition
	 * @return Integer。会员id
	 */
	Integer findByKeyword(String condition);
	
	/**
	 *  绑定会员卡
	 * @param cardBind
	 * @return boolean。true：绑定成功；false：绑定失败
	 */
	boolean bindCard(TMemberBindRecord cardBind);
	
	/**
	 *  批量导入会员
	 * @param idList
	 * @return boolean。true：导入成功；false：导入失败
	 */
	boolean saveByBundle(String filePath);
	
	/**
	 *  批量绑定会员卡
	 * @param idList
	 * @return boolean。true：绑定成功；false：绑定失败
	 */
	boolean bindByBunble(String filePath);
	
	/**
	 * 分页查询。当前会员绑定的所有会员卡信息
	 * @param id
	 * @param currentPage
	 * @param pageSize
	 * @return List<TMemberBindRecord>。会员绑定记录结果集
	 */
	List<TMemberBindRecord> findAllCardByPage(Integer id,Integer currentPage,Integer pageSize);

	/**
	 * 分页查询。当前会员的上课记录
	 * @param id
	 * @param currentPage
	 * @param pageSize
	 * @return List<TReservationRecord>。上课记录结果集
	 */
	List<TReservationRecord> getClassRecords(Integer id,Integer currentPage,Integer pageSize);
	
	/**
	 * 分页查询。当前会员的预约记录
	 * @param id
	 * @param currentPage
	 * @param pageSize
	 * @return List<TReservationRecord>。预约记录结果集
	 */
	List<TReservationRecord> getReserveRecords(Integer id,Integer currentPage,Integer pageSize);
	
	/**
	 * 分页查询。当前会员的消费记录
	 * @param id
	 * @param currentPage
	 * @param pageSize
	 * @return List<TConsumeRecord>。消费记录结果集
	 */
	List<TConsumeRecord> getConsumeRecord(Integer id,Integer currentPage,Integer pageSize);
	
	
}
