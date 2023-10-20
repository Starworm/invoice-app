package io.project.invoiceApp.repositories;

import io.project.invoiceApp.domain.User;

import java.util.Collection;

public interface UserRepository<T extends User>  {

    ///////////// basic CRUD operation ////////////////

    /**
     * generic interface for creation record into DB
     * @param data - data to store
     * @return - returns new created object
     */
    T create(T data);

    /**
     * generic interface for getting list of records from specified page with specified amount of records
     * @param page - number of page
     * @param pageSize - amount of records on the page
     * @return returns list of records
     */
    Collection<T> list(int page, int pageSize);

    /**
     * generic interface for getting a record by its id
     * @param id - record id
     * @return - returns a record
     */
    T get(Long id);

    /**
     * generic interface for update a record
     * @param data - data for update
     * @return - returns an updated record
     */
    T update(T data);

    /**
     * interface for deleting record by its id
     * @param id - id record
     * @return - returns success/failure of delete operation
     */
    Boolean delete(Long id);

    ///////////// More complex operation ////////////////
    User getUserByEmail(String email);
}
