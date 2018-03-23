package com.tsm.sendemail.controller;

import com.tsm.sendemail.model.BaseModel;
import com.tsm.sendemail.parser.IParser;
import com.tsm.sendemail.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@SuppressWarnings(value = {"rawtypes"})
@Slf4j
public abstract class BaseController<R, M extends BaseModel, I extends Serializable> {

    protected static final String ADMIN_TOKEN_HEADER = "AT";

    protected static final String COMMA_SEPARATOR = ",";

    public static final String JSON_VALUE = "application/json";

    @Autowired
    private Validator validator;

    protected <T> void validate(final T object, Class clazz) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, clazz);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

    public abstract BaseService<M, I> getService();

    public abstract IParser<R, M> getParser();

    @RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(CREATED)
    public R save(@RequestBody final R resource) {
        log.debug("Recieved a request to create a resource  [{}].", resource);

        validate(resource, Default.class);

        M model = getParser().toModel(resource);

        model = getService().save(model);

        R result = getParser().toResource(model);

        log.debug("returning resource [{}].", result);

        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = PUT, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public R update(@PathVariable final Integer id, @RequestBody final R resource) {
        log.debug("Recieved a request to update a resource  [{}].", resource);

        validate(resource, Default.class);

        M model = getParser().toModel(resource);

        M origin = getService().findById((I) id);

        model = getService().update(origin, model);

        R result = getParser().toResource(model);

        log.debug("returning resource [{}].", result);

        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/{id}", method = GET, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public R findById(@PathVariable final Integer id) {

        log.info("Recieved a request to find an model by id [{}].", id);

        M model = getService().findById((I) id);

        R result = getParser().toResource(model);

        log.info("returning resource: [{}].", result);

        return result;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = DELETE, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final Integer id) {
        log.debug("Recieved a request to delete [{}].", id);

        M model = getService().findById((I) id);

        getService().delete(model);

        log.debug("model deleted.");
    }

    @RequestMapping(method = GET, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public Set<R> findAll(@RequestParam(value = "q", required = false) String q) {

        log.debug("Recieved a request to find all models.");

        Set<M> models = getService().findAll();

        Set<R> resources = new HashSet<>();
        if (!models.isEmpty()) {
            resources = getParser().toResources(models);
        }

        log.debug("returning resources: [{}].", resources.size());

        return resources;
    }

}
