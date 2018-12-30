package com.lbs.re.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbs.re.data.dao.ResourceDAO;
import com.lbs.re.data.repository.ResourceRepository;
import com.lbs.re.model.ReResource;

@Component
public class ResourceDAOImpl extends BaseDAOImpl<ReResource, Integer> implements ResourceDAO {
    /**
     * long serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private transient ResourceRepository repository;

    @Autowired
    public void setRepository(ResourceRepository repository) {
        this.repository = repository;
        super.setRepository(repository);
    }

    @Override
	public ReResource getResourceByNumber(int resourceNr) {
		return repository.findByresourceNr(resourceNr);
    }
}
