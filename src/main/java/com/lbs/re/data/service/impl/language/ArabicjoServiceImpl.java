package com.lbs.re.data.service.impl.language;

import com.lbs.re.data.dao.language.ArabicjoDAO;
import com.lbs.re.data.service.impl.BaseServiceImpl;
import com.lbs.re.data.service.language.ArabicjoService;
import com.lbs.re.model.languages.ReArabicjo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArabicjoServiceImpl extends BaseServiceImpl<ReArabicjo, Integer> implements ArabicjoService {
    /**
     * long serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private ArabicjoDAO dao;

    @Autowired
    public void setDao(ArabicjoDAO dao) {
        this.dao = dao;
        super.setBaseDao(dao);
    }
}
