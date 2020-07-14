package com.pagoda.framework.core.persistence.dao;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.pagoda.framework.core.persistence.dao.handler.StreamHandler;
import com.pagoda.framework.core.persistence.dao.page.PageInfo;
import com.pagoda.framework.core.persistence.dao.page.PageStatement;

/**
 * 내부적으로 초기화하여 지원하는 편의 클래스. 배치 트랜잭션 모드를 지원하는 배치 연산 추가 제공 .
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 */
public interface CommonDAO {

	/**
	 * 단건 조회(int) 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return 조회된 데이터
	 */
	Integer queryForInt(String statementId, Object parameter);

	/**
	 * 단건 조회(long) 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return 조회된 데이터
	 */
	Long queryForLong(String statementId, Object parameter);

	/**
	 * 단건 조회 연산을 수행한다.
	 *
	 * @param <T>
	 *            generic type class
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 * @param clazz
	 *            generic type class
	 *
	 * @return 조회된 데이터 (단건) or null
	 */
	<T> T queryForObject(String statementId, Object parameter, Class<T> clazz);

	/**
	 * 단건 조회 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return 조회된 데이터 (단건) or null
	 */
	Object queryForObject(String statementId, Object parameter);

	/**
	 * 단건 조회 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param mapKey
	 *            resultMap key
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return 조회된 데이터 (단건) or null
	 */
	Map<?, ?> queryForMap(String statementId, String mapKey, Object parameter);

	/**
	 * 다건 조회 연산을 수행한다.
	 *
	 * @param <T>
	 *            generic type class
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 * @param clazz
	 *            generic type class
	 *
	 * @return 조회된 데이터 (0건 이상)
	 */
	<T> List<T> queryForList(String statementId, Object parameter, Class<T> clazz);

	/**
	 * 다건 조회 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return 조회된 데이터 (0건 이상)
	 */
	List<?> queryForList(String statementId, Object parameter);

	/**
	 * @param <T>
	 *            generic type class 다건 조회 연산을 수행한다.
	 *
	 * @param statement
	 *            TotalCount와 데이터를 조회하기 위한 Statement (MyBatis namespace +
	 *            statementId)
	 * @param parameter
	 *            입력 parameter
	 * @return PageInfo
	 */
	<T> PageInfo<T> queryForPagenatedList(PageStatement statement, Object parameter);

	/**
	 * 다건 조회 연산을 수행한다.
	 *
	 * @param <T>
	 *            generic type class
	 * @param statement
	 *            TotalCount와 데이터를 조회하기 위한 Statement (MyBatis namespace +
	 *            statementId)
	 * @param parameter
	 *            입력 parameter
	 * @param pageNum
	 *            페이징 처리에 사용 (1..n)
	 * @param pageRows
	 *            페이징 처리에 사용
	 * @return PageInfo
	 */
	<T> PageInfo<T> queryForPagenatedList(PageStatement statement, Object parameter, int pageNum, int pageRows);

	/**
	 * 다건 조회 연산을 수행한다.
	 *
	 * @param <T>
	 *            generic type class
	 * @param <R>
	 *            generic type class
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 * @param rowHandler
	 *            ResultSet을 가공하기 위한 row mapper
	 * @return List
	 */
	<T, R> List<R> queryWithResultHandler(String statementId, Object parameter, StreamHandler<T, R> rowHandler);

	/**
	 * <pre>
	 * 다건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param <T>
	 *            generic type class
	 * @param <R>
	 *            generic type class
	 * @param statement
	 *            TotalCount와 데이터를 조회하기 위한 Statement (MyBatis namespace +
	 *            statementId)
	 * @param parameter
	 *            입력 parameter
	 * @param pageNum
	 *            페이징 처리에 사용
	 * @param pageRows
	 *            페이징 처리에 사용
	 * @param streamHandler
	 *            ResultSet을 가공하기 위한 row mapper
	 * @return PageInfo PageInfo
	 */
	<T, R> PageInfo<R> queryWithResultHandler(PageStatement statement, Object parameter, int pageNum, int pageRows, final StreamHandler<T, R> streamHandler);

	/**
	 * 수정 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return null or inline query result
	 */
	Integer update(String statementId, Object parameter);

	/**
	 * 입력 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return affected row count
	 */
	Object insert(String statementId, Object parameter);

	/**
	 * 삭제 연산을 수행한다.
	 *
	 * @param statementId
	 *            MyBatis namespace + statementId
	 * @param parameter
	 *            입력 parameter
	 *
	 * @return affected row count
	 */
	Integer delete(String statementId, Object parameter);

	/**
	 * 쿼리실행 후 쿼리결과 수를 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public int getExecuteResult(String sqlId, Object paramMap) throws Exception;
	
	
	/**
	 * 쿼리후 페이지를 리턴하는 공통 메소드
	 * @param sqlId
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */	
	public List getSelectPaginatedResult(String sqlMapId, Map parameterMap) throws Exception;
	
	public void saveTest(String sqlMapId, Object parameterMap) throws SQLException;

}