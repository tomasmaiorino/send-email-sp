package com.tsm.example.service;

import com.tsm.example.exceptions.ResourceNotFoundException;
import com.tsm.example.model.BaseModel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

@Slf4j
public abstract class BaseService<I extends BaseModel, T extends Serializable> implements IBaseService<I, T> {

	private static final String ID_ASSERT_MESSAGE = "The id must not be null!";
	private static final String MODEL_ASSERT_MESSAGE = "The model must not be null!";

	protected void saveValidation(final I model) {
	}

	protected void updateValidation(final I origin, final I model) {
	}

	protected void merge(final I origin, final I model) {
	}

	protected abstract String getClassName();

	@Override
	@Transactional
	public I save(final I model) {
		Assert.notNull(model, MODEL_ASSERT_MESSAGE);

		log.info("Saving model [{}] .", model);

		saveValidation(model);

		getRepository().save(model);

		log.info("Saved model [{}].", model);

		return model;
	}

	@Override
	@Transactional
	public I update(I origin, I model) {
		Assert.notNull(origin, "The origin must not be null!");
		Assert.isTrue(!origin.isNew(), "The origin must not be new!");
		Assert.notNull(model, MODEL_ASSERT_MESSAGE);

		log.info("Updating origin[{}] to model [{}].", origin, model);

		updateValidation(origin, model);

		merge(origin, model);

		getRepository().save(origin);

		log.info("Updated origin [{}].", origin);

		return origin;
	}

	@Override
	public I findById(final T id) {
		Assert.notNull(id, ID_ASSERT_MESSAGE);
		log.info("Finding model by id [{}] .", id);

		I model = findOptionalById(id)
				.orElseThrow(() -> new ResourceNotFoundException(getNotFoundError(getClassName())));

		log.info("Model found [{}].", model);

		return model;
	}

	@Override
	public Optional<I> findOptionalById(final T id) {
		Assert.notNull(id, ID_ASSERT_MESSAGE);
		log.info("Finding optional by id [{}] .", id);

		Optional<I> optModel = getRepository().findById(id);

		log.info("Model found [{}].", optModel.isPresent());

		return optModel;
	}

	@Override
	public Set<I> findAll() {
		log.info("Finding all.");

		Set<I> allFound = getRepository().findAll();

		log.info("All Found [{}].", allFound.size());

		return allFound;
	}

	@Transactional
	public void delete(final I model) {
		Assert.notNull(model, MODEL_ASSERT_MESSAGE);
		Assert.isTrue(!model.isNew(), "The model must not be new!");
		log.info("Deleting model [{}].", model);

		getRepository().delete(model);

		log.info("Model deleted.");

	}

	private String getNotFoundError(final String className) {
		return className + "NotFound";
	}

}
