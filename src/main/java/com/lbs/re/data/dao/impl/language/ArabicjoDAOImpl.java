package com.lbs.re.data.dao.impl.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbs.re.data.dao.language.ArabicjoDAO;
import com.lbs.re.data.repository.language.ArabicjoRepository;
import com.lbs.re.model.languages.ReArabicjo;

@Component
public class ArabicjoDAOImpl extends LanguageDAOImpl<ReArabicjo, Integer> implements ArabicjoDAO {
    /**
     * long serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private transient ArabicjoRepository repository;

    @Autowired
    public void setRepository(ArabicjoRepository repository) {
        this.repository = repository;
        super.setRepository(repository);
    }
}
