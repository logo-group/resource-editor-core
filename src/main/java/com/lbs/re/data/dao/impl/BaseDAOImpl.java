/*
 * Copyright 2014-2019 Logo Business Solutions
 * (a.k.a. LOGO YAZILIM SAN. VE TIC. A.S)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.lbs.re.data.dao.impl;

import com.lbs.re.data.dao.BaseDAO;
import com.lbs.re.data.repository.BaseRepository;
import com.lbs.re.exception.localized.GeneralLocalizedException;
import com.lbs.re.exception.localized.LocalizedException;
import com.lbs.re.exception.localized.UniqueConstraintException;
import com.lbs.re.model.AbstractBaseEntity;
import com.lbs.re.util.HasLogger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class BaseDAOImpl<T extends AbstractBaseEntity, ID extends Serializable> implements BaseDAO<T, ID>, HasLogger {

    /**
     * long serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private transient BaseRepository<T, ID> repository;

    public void setRepository(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T getById(ID id) throws LocalizedException {
        try {
            return repository.findOne(id);
        } catch (Exception e) {
            throw new GeneralLocalizedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T save(T entity) throws LocalizedException {
        try {
            T savedAndFlush = repository.saveAndFlush(entity);
            T object = getById((ID) savedAndFlush.getId());
            return object;
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintException(e);
        } catch (Exception e) {
            throw new GeneralLocalizedException(e);
        }
    }

    @Override
    public List<T> save(List<T> entityList) throws LocalizedException {
        try {
            List<T> savedEntityList = repository.save(entityList);
            return savedEntityList;
        } catch (Exception e) {
            throw new GeneralLocalizedException(e);
        }
    }

    @Override
    public void delete(T entity) throws LocalizedException {
        try {
            repository.delete(entity);
        } catch (Exception e) {
            throw new GeneralLocalizedException(e);
        }
    }

    @Override
    public void deleteById(ID id) throws LocalizedException {
        try {
            repository.delete(id);
        } catch (Exception e) {
            throw new GeneralLocalizedException(e);
        }
    }

    @Override
    public List<T> getAll() throws LocalizedException {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new GeneralLocalizedException(e);
        }
    }


}
