package com.tsm.example.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tsm.example.model.Message;
import com.tsm.example.model.SearchCriteria;

import java.util.List;

/**
 * Created by tomas on 5/15/18.
 */
public class MessageRepositoryImpl implements IBaseSearchRepositoryCustom<Message> {

	@PersistenceContext
	private EntityManager entityManager;

	public List<Message> search(List<SearchCriteria> params) {
		return search(params, Message.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
