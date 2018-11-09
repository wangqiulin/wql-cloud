package com.wql.cloud.basic.idgenerator.generator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.wql.cloud.basic.idgenerator.dao.IdGenDAO;
import com.wql.cloud.basic.idgenerator.model.IdGenInfo;

public class IncrementIdGenerator implements IdGenerator {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private LinkedBlockingQueue<Long> sequences = new LinkedBlockingQueue<>();

	private int defaultSteps = 1;
	private String project;
	private String model;
	
	private Map<Long, Integer> id2stepsMap = new ConcurrentHashMap<>();
	
	private IdGenDAO dao;
	
	public void setDao(IdGenDAO dao) {
		this.dao = dao;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setSequences(LinkedBlockingQueue<Long> sequences) {
		this.sequences = sequences;
	}

	@Override
	public long gen() {
		return gen(1)[0];
	}

	@Override
	public long[] gen(int count) {
		preCheck(count);
		long[] results = new long[count];
		try {
			for (int i = 0; i < count; i++) {
				results[i] = sequences.take();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return results;
	}
	
	public void preCheck(int count) {
		if (sequences.size() < count) {
			int finalSteps = count > defaultSteps ? count : defaultSteps;
			IdGenInfo  info = new IdGenInfo();
			info.setProjectName(project);
			info.setModelName(model);
			info.setSteps(finalSteps);
			info = applyFromDb(info);
			id2stepsMap.put(info.getId(), info.getSteps());
			long start = info.getLastIssued() - finalSteps;
			for (long i = 1; i <= finalSteps; i++) {
				sequences.add(start + i);
			}
		}
	}
	
	private TransactionTemplate txTemplate;

	public void setTxTemplate(TransactionTemplate txTemplate) {
		this.txTemplate = txTemplate;
	}
	
	public IdGenInfo applyFromDb(IdGenInfo info) {
		return txTemplate.execute(new TxCallback(info));
	}
	
	class TxCallback implements TransactionCallback<IdGenInfo> {
		private IdGenInfo info;

		TxCallback(IdGenInfo info) {
			this.info = info;
		}

		@Override
		public IdGenInfo doInTransaction(TransactionStatus status) {
			try {
				dao.update(info);
				return dao.get(info);
			} catch (Exception e) {
				logger.error("发号失败", e);
				status.setRollbackOnly();
			}
			return null;
		}
	}
}
