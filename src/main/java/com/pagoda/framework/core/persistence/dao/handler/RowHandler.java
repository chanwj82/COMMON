/*
 * Copyright (c) 2016 by pagoda.
 * All right reserved.
 */
package com.pagoda.framework.core.persistence.dao.handler;

/**
 * RowHandler
 *
 * Updated on : 2016-07-20 Updated by : IT전략팀, chanwj.
 *
 * @param <T>
 *            오브젝트 제너릭
 * @param <R>
 *            오브젝트 제너릭
 */
public abstract class RowHandler<T, R> implements StreamHandler<T, R> {

	/** The stop. */
	private boolean stop = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.handler.
	 * StreamHandler#open()
	 */
	@Override
	public void open() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.handler.
	 * StreamHandler#close()
	 */
	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.handler.
	 * StreamHandler#isStop()
	 */
	@Override
	public boolean isStop() {
		return stop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.handler.
	 * StreamHandler#stop()
	 */
	@Override
	public void stop() {
		this.stop = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pagoda.framework.core.persistence.dao.handler.
	 * StreamHandler#handleRow(java.lang.Object)
	 */
	@Override
	public abstract R handleRow(T valueObject);

}
