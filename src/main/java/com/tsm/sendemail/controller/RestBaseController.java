package com.tsm.sendemail.controller;

import com.tsm.sendemail.exceptions.BadRequestException;
import com.tsm.sendemail.model.BaseModel;
import com.tsm.sendemail.model.SearchCriteria;
import com.tsm.sendemail.parser.IParser;
import com.tsm.sendemail.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tsm.sendemail.util.ErrorCodes.INVALID_SEARCH_PARAMS;

@SuppressWarnings(value = {"rawtypes", "unchecked"})
@Slf4j
public abstract class RestBaseController<R, M extends BaseModel, I extends Serializable> extends BaseController {

    private static final String STATUS_KEY = "status";

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

    public R save(final R resource) {
        log.debug("Received a request to create a resource  [{}].", resource);

        validate(resource, Default.class);

        M model = getParser().toModel(resource);

        model = getService().save(model);

        R result = getParser().toResource(model);

        log.debug("returning resource [{}].", result);

        return result;
    }

    public R update(final Integer id, final R resource) {
        log.debug("Received a request to update a resource  [{}].", resource);

        validate(resource, Default.class);

        M model = getParser().toModel(resource);

        M origin = getService().findById((I) id);

        model = getService().update(origin, model);

        R result = getParser().toResource(model);

        log.debug("returning resource [{}].", result);

        return result;
    }

    public R findById(final Integer id) {

        log.info("Received a request to find an model by id [{}].", id);

        M model = getService().findById((I) id);

        R result = getParser().toResource(model);

        log.info("returning resource: [{}].", result);

        return result;
    }

    public void delete(final Integer id) {
        log.debug("Received a request to delete [{}].", id);

        M model = getService().findById((I) id);

        getService().delete(model);

        log.debug("model deleted.");
    }

    public Set<R> findAll(final String q) {

        log.debug("Received a request to find all models.");

        Set<M> models = getService().findAll();

        Set<R> resources = new HashSet<>();
        if (!models.isEmpty()) {
            resources = getParser().toResources(models);
        }

        log.debug("returning resources: [{}].", resources.size());

        return resources;
    }

    public Set<R> findAllSearch(final String search, final List<String> searchParamsAllowed,
                                Function<List<SearchCriteria>, List<M>> searchFunction, Class convertEnumClass) {

        log.info("Received a request to find all with search param [{}].", search);

        List<SearchCriteria> params =  new ArrayList<>();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                log.debug("matcher.group one [{}].", matcher.group(1));
                if (!searchParamsAllowed.contains(matcher.group(1))) {
                    throw new BadRequestException(INVALID_SEARCH_PARAMS);
                }
                if (matcher.group(1).equalsIgnoreCase(STATUS_KEY)) {
                    params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), convertEnumClass));
                } else  {
                    params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        List<M> searchResults = searchFunction.apply(params);

        if (!CollectionUtils.isEmpty(searchResults)) {
            return getParser().toResources(new HashSet<>(searchResults));
        }
        log.info("None result was found. Returning empty set.");
        return Collections.emptySet();
    }

}
