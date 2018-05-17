package com.tsm.sendemail.repository;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public interface IBaseRepository<I, T extends Serializable> {

    I save(I model);

    Optional<I> findById(final T id);

    void delete(final I model);

    Set<I> findAll();
}