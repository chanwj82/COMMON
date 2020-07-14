package com.pagoda.framework.core.persistence.dao;


import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.pagoda.common.CommonUtil;
import com.pagoda.framework.core.persistence.dao.handler.StreamHandler;
import com.pagoda.framework.core.persistence.dao.page.PageInfo;
import com.pagoda.framework.core.persistence.dao.page.PageStatement;
import com.pagoda.framework.core.persistence.dao.page.PagenateInfo;


/**
 * {@link CommonDAO}의 MyBatis 구현체 .
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
@Qualifier("CommonDAOMyBatisImpl")
public class CommonDAOMyBatisImpl implements CommonDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonDAOMyBatisImpl.class);

	protected SqlSessionTemplate template;
	
	@Value("#{properties['Globals.pageUnit']}")
	private String globalPageUnit;
	
	@Value("#{properties['Globals.pageSize']}")
	private String globalPageSize;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForInt(java.lang.String, java.lang.Object)
	 */
	@Override
	public Integer queryForInt(String statementId, Object parameter) {
		Integer value = (Integer) this.template.selectOne(statementId, parameter);

		if (value == null) {
			value = 0;
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForLong(java.lang.String, java.lang.Object)
	 */
	@Override
	public Long queryForLong(String statementId, Object parameter) {
		return (Long) this.template.selectOne(statementId, parameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForObject(java.lang.String, java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T queryForObject(String statementId, Object parameter, Class<T> clazz) {
		return clazz.cast(this.template.selectOne(statementId, parameter));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object queryForObject(String statementId, Object parameter) {
		return this.template.selectOne(statementId, parameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForMap(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public Map<?, ?> queryForMap(String statementId, String mapKey, Object parameter) {
		return this.template.selectMap(statementId, parameter, mapKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForList(java.lang.String, java.lang.Object, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(String statementId, Object parameter, Class<T> clazz) {
		List<T> list = (List<T>) this.template.selectList(statementId, parameter);

		LOGGER.debug("Statement[{}] Executed ({}) : {} Records retrieved.", new Object[] { statementId, new Date(), list == null ? 0 : list.size() });

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForList(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<?> queryForList(String statementId, Object parameter) {
		List<?> list = this.template.selectList(statementId, parameter);

		LOGGER.debug("Statement[{}] Executed ({}) : {} Records retrieved.", new Object[] { statementId, new Date(), list == null ? 0 : list.size() });

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PageInfo<T> queryForPagenatedList(PageStatement statement, Object parameter) {
		int totalCount = this.queryForInt(statement.getTotalCountStatementId(), parameter);

		List<T> list = null;
		PagenateInfo pim = new PagenateInfo();

		try {
			pim = (PagenateInfo) (parameter.getClass().getMethod("getPage")).invoke(parameter);
			pim.setTotalCount(totalCount);
			(parameter.getClass().getMethod("setPage", PagenateInfo.class)).invoke(parameter, pim);
		} catch (Exception ex) {
			LOGGER.error("queryForPagenatedList 중 에러가 발생 했습니다. \\n{}", ex);
			throw new RuntimeException(ex);
		}

		if (totalCount > 0) {
			list = (List<T>) this.template.selectList(statement.getDataStatementId(), parameter);
		}

		PageInfo<T> pageInfo = new PageInfo<T>(list == null ? new ArrayList<T>(0) : list);
		pageInfo.setPage(pim);

		return pageInfo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryForPagenatedList
	 * (com.pagoda.framework.core.persistence.dao.page.PageStatement,
	 * java.lang.Object, int, int, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> PageInfo<T> queryForPagenatedList(PageStatement statement, Object parameter, int pageNum, int pageRows) {
		int totalCount = this.queryForInt(statement.getTotalCountStatementId(), parameter);

		List<T> list = null;
		PageInfo<T> pageInfo = null;

		boolean hasPagenateInfoVO = false;

		for (Field field : parameter.getClass().getDeclaredFields()) {
			if (field.getType().isAssignableFrom(PagenateInfo.class)) {
				hasPagenateInfoVO = true;
			}
		}

		if (hasPagenateInfoVO) {
			PagenateInfo pim = new PagenateInfo();
			try {
				pim.setTotalCount(totalCount);
				pim.setNo(pageNum);
				pim.setRows(pageRows);
				(parameter.getClass().getMethod("setPage", PagenateInfo.class)).invoke(parameter, pim);
			} catch (Exception ex) {
				LOGGER.error("queryForPagenatedList 중 에러가 발생 했습니다. \\n{}", ex);
				throw new RuntimeException(ex);
			}

			if (totalCount > 0) {
				list = (List<T>) this.template.selectList(statement.getDataStatementId(), parameter);

				LOGGER.debug("Statement[{}] Executed ({}) : {} Records retrieved.", new Object[] { statement.getDataStatementId(), new Date(),
						list == null ? 0 : list.size() });
			}

			pageInfo = new PageInfo<T>(list == null ? new ArrayList<T>(0) : list);
			pageInfo.setPage(pim);
		} else {
			if (totalCount > 0) {
				int skipRows = (pageNum - 1) * pageRows;

				list = (List<T>) this.template.selectList(statement.getDataStatementId(), parameter, new RowBounds(skipRows, pageRows));

				LOGGER.debug("Statement[{}] Executed ({}) : {} Records retrieved.", new Object[] { statement.getDataStatementId(), new Date(),
						list == null ? 0 : list.size() });
			}
			pageInfo = new PageInfo<T>(list == null ? new ArrayList<T>(0) : list);
		}
		return pageInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryWithResultHandler(java.lang.String, java.lang.Object,
	 * com.skplanet.storeplatform
	 * .framework.core.persistence.dao.handler.StreamHandler)
	 */
	@Override
	public <T, R> List<R> queryWithResultHandler(String statementId, Object parameter, final StreamHandler<T, R> streamHandler) {
		streamHandler.open();

		try {
			final List<R> list = new ArrayList<R>();

			this.template.select(statementId, parameter, new ResultHandler() {
				@Override
				@SuppressWarnings("unchecked")
				public void handleResult(ResultContext context) {
					R result = streamHandler.handleRow((T) context.getResultObject());

					if (result != null) {
						list.add(result);
					}

					if (streamHandler.isStop()) {
						context.stop();
					}
				}
			});

			return list;
		} finally {
			streamHandler.close();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#
	 * queryWithResultHandler(com.skplanet.storeplatform
	 * .framework.core.persistence.dao.page.PageStatement, java.lang.Object,
	 * int, int, com.pagoda.framework.core.persistence.dao.handler
	 * .StreamHandler)
	 */
	@Override
	public <T, R> PageInfo<R> queryWithResultHandler(PageStatement statement, Object parameter, int pageNum, int pageRows,
			final StreamHandler<T, R> streamHandler) {
		streamHandler.open();

		try {
			int totalRowCount = this.queryForInt(statement.getTotalCountStatementId(), parameter);
			final List<R> list = new ArrayList<R>();

			if (totalRowCount > 0) {
				int startRows = (pageNum - 1) * pageRows;
				int endRows = pageRows;

				LOGGER.debug("Start={}, End={}", startRows, endRows);

				this.template.select(statement.getDataStatementId(), parameter, new RowBounds(startRows, endRows), new ResultHandler() {

					@SuppressWarnings("unchecked")
					@Override
					public void handleResult(ResultContext context) {
						R result = streamHandler.handleRow((T) context.getResultObject());

						if (result != null) {
							list.add(result);
						}

						if (streamHandler.isStop()) {
							context.stop();
						}
					}
				});
			}

			LOGGER.debug("Statement[{}] Executed ({}) : {} Records retrieved.", new Object[] { statement.getDataStatementId(), new Date(),
					list == null ? 0 : list.size() });

			return new PageInfo<R>(totalRowCount, list);

		} finally {
			streamHandler.close();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#update
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public Integer update(String statementId, Object parameter) {
		return this.template.update(statementId, parameter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#insert
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public Object insert(String statementId, Object parameter) {
		return this.template.insert(statementId, parameter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.pagoda.framework.core.persistence.dao.CommonDAO#delete
	 * (java.lang.String, java.lang.Object)
	 */
	@Override
	public Integer delete(String statementId, Object parameter) {
		return this.template.delete(statementId, parameter);
	}

	/**
	 * {@link SqlSessionTemplate}에 대한 의존성을 직접 주입.
	 *
	 * @param template
	 *            template
	 */
	public void setSqlSessionTemplate(SqlSessionTemplate template) {
		this.template = template;
	}
	
	
	/**
	 * Method CUD쿼리 결과 리턴
	 * @param sqlMapId - ibatis sqlMapId
	 * @param parameterMap - FORM 객체
	 * @return CUD쿼리 결과
	 * @exception   Exception
	 * @see
	 */
	@Override
	public int getExecuteResult(String sqlMapId, Object parameterMap)  throws Exception {
		int updatedRowCount = 0;

		try {
			long startTime = System.currentTimeMillis();
			updatedRowCount = this.template.update(sqlMapId, parameterMap);
			long endTime = System.currentTimeMillis();

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("[sqlMapId:"+sqlMapId+"]query execute TIME : " + (endTime - startTime) + "(ms)]]");
			}
		}
		catch(Exception e) {
			LOGGER.debug("DAO ERROR : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return updatedRowCount;
	}
	
	@Override
	public void saveTest(String sqlMapId, Object parameterMap) throws SQLException {
		this.template.insert(sqlMapId,parameterMap);
	}
	
	/**
	 * Method 쿼리 결과 페이징 리턴
	 * @param sqlMapId - ibatis sqlMapId
	 * @param parameterMap - FORM 객체
	 * @return 쿼리 결과 페이징 List
	 * @throws FdlException
	 * @exception   Exception
	 * @see
	 */
	public List getSelectPaginatedResult(String sqlMapId, Map parameterMap) throws Exception{
		List list;

		try
		{
			long startTime = System.currentTimeMillis();

			String pageIndex = CommonUtil.getStringEmpty(parameterMap.get("pageIndex"), "1");
			String pageUnit  = ObjectUtils.toString(parameterMap.get("pageUnit"), globalPageUnit);
			String pageSize  = ObjectUtils.toString(parameterMap.get("pageSize"), globalPageSize);

			LOGGER.debug("pageIndex : " + parameterMap.get("pageIndex"));
			LOGGER.debug("pageIndex : " + pageIndex);

			int index = Integer.parseInt(pageIndex);
			int size  = Integer.parseInt(pageSize);
			int unit  = Integer.parseInt(pageUnit);

			parameterMap.put("startPageingNumber", String.valueOf( ((index-1)*size)+1 )) ;
			parameterMap.put("endPageingNumber",   String.valueOf( index*size ));

			list = this.template.selectList(sqlMapId, parameterMap);

			// 총개수
			String totalCnt = list.size() > 0 ? ((Map) list.get(0)).get("TOTAL").toString() : "0";

			int pageLastIndex = (int)Math.ceil((Double.parseDouble(totalCnt)/size));
			int pageStartIndex = (((index-1)/unit)*unit) + 1;

			// 페이징 관련 처리
			parameterMap.put("pageIndex", pageIndex);
			parameterMap.put("pageUnit",  pageUnit);
			parameterMap.put("pageSize",  pageSize);
			parameterMap.put("pageTotalCnt",   totalCnt);
			parameterMap.put("pageLastIndex",  pageLastIndex);
			parameterMap.put("pageStartIndex", pageStartIndex);

			long endTime = System.currentTimeMillis();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("[sqlMapId:"+sqlMapId+"]query execute TIME : " + (endTime - startTime) + "(ms)]]");
			}
		}
		catch(Exception e) {
			LOGGER.debug("DAO ERROR : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return list;
	}

}