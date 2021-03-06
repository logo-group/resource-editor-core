package com.lbs.re.data.service.impl.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbs.re.data.dao.language.RussianruDAO;
import com.lbs.re.data.service.language.RussianruService;
import com.lbs.re.model.languages.ReRussianru;

@Service
public class RussianruServiceImpl extends LanguageServiceImpl<ReRussianru, Integer> implements RussianruService {
    /**
     * long serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private RussianruDAO dao;

    @Autowired
    public void setDao(RussianruDAO dao) {
        this.dao = dao;
        super.setBaseDao(dao);
    }
}
