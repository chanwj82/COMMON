package com.pagoda.common.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pagoda.framework.core.persistence.dao.CommonDAO;

@Service("commonService")
public class CommonService {
	
	private static final Logger log = LoggerFactory.getLogger(CommonService.class);

	@Autowired
	private CommonDAO commonDAO;

	/**
	 * 쿼리후 List를 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List getSelectList(String sqlId, Object paramMap) throws Exception {
		return commonDAO.queryForList(sqlId, paramMap);
	}


	/**
	 * 쿼리후 Object를 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Object getSelectObject(String sqlId, Object paramMap) throws Exception {
		return commonDAO.queryForObject(sqlId, paramMap);
	}
	
	/**
	 * 쿼리후 map을 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map getSelectMap(String sqlId, Object paramMap) throws Exception {
		return (Map) commonDAO.queryForObject(sqlId, paramMap);
	}

	
	/**
	 * 쿼리실행 후 쿼리결과 수를 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int getExecuteResult(String sqlId, Object paramMap) throws Exception {
		return commonDAO.getExecuteResult(sqlId, paramMap);
	}
	
	public int getExecuteResult(String[] sqlId, Map paramMap) throws Exception {
		int resultCnt = 0;
		for (String element : sqlId) {
			resultCnt += commonDAO.getExecuteResult(element, paramMap);
		}
		return resultCnt;
	}
	
	/**
	 * 쿼리후 페이지를 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List getSelectPageList(String sqlId, Map<String, Object> paramMap) throws Exception {
		return commonDAO.getSelectPaginatedResult(sqlId, paramMap);
	}

}