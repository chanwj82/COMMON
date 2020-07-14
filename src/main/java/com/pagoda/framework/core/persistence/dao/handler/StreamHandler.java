/*
 * Copyright (c) 2016 by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.core.persistence.dao.handler;

/**
 * 대용량 데이터의 스트리밍 처리시 사용되며, MyBatis의 ResultHandler와 함께 사용된다.
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 *
 * @param <T>
 *            오브젝트 제너릭
 * @param <R>
 *            오브젝트 제너릭
 */
public interface StreamHandler<T, R> {

	/**
	 * 스트리밍 헤더 쓰기.
	 */
	void open();

	/**
	 * 스트리밍 마무리.
	 */
	void close();

	/**
	 * 행 단위 처리.
	 *
	 * @param valueObject
	 *            행 데이터.
	 * @return the r
	 */
	R handleRow(T valueObject);

	/**
	 * Stop.
	 */
	void stop();

	/**
	 * Checks if is stop.
	 *
	 * @return true, if is stop
	 */
	boolean isStop();

}
