package com.lbs.re.data.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lbs.re.data.dao.ResourceitemDAO;
import com.lbs.re.data.repository.ResourceitemRepository;
import com.lbs.re.model.AbstractBaseEntity;
import com.lbs.re.model.ReResourceitem;
import com.lbs.re.model.ReStandard;
import com.lbs.re.model.languages.ReEnglishus;
import com.lbs.re.model.languages.ReTurkishtr;
import com.lbs.re.util.LogoResConstants;

@Component
public class ResourceitemDAOImpl extends BaseDAOImpl<ReResourceitem, Integer> implements ResourceitemDAO {
	/**
	 * long serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private transient ResourceitemRepository repository;

	private DataSource dataSource;

	@PersistenceContext
	protected EntityManager em;

	@Autowired
	public void setRepository(ResourceitemRepository repository, DataSource dataSource) {
		this.repository = repository;
		this.dataSource = dataSource;
		super.setRepository(repository);
	}

	@Override
	public List<ReResourceitem> getItemListByResource(int resourceref) {
		return repository.findByresourceref(resourceref);
	}

	@Override
	@Transactional
	public List<ReResourceitem> getLastModifiedItemList() {
		Criteria criteriaResourceItem = em.unwrap(Session.class).createCriteria(ReResourceitem.class);
		criteriaResourceItem.addOrder(Order.desc("modifiedon"));
		criteriaResourceItem.setMaxResults(100);
		return criteriaResourceItem.list();
	}

	@Override
	@Transactional
	public List<ReResourceitem> getAdvancedSearchedItemList(List<Criterion> resourceItemCriterias, List<Criterion> turkishCriterias, List<Criterion> englishCriterias,
			List<Criterion> standardCriterias) {
		List<ReResourceitem> itemList = generateResourceItemListByCriterias(resourceItemCriterias, turkishCriterias, englishCriterias, standardCriterias);
		return itemList;
	}

	private List<ReResourceitem> generateResourceItemListByCriterias(List<Criterion> resourceItemCriterias, List<Criterion> turkishCriterias, List<Criterion> englishCriterias,
			List<Criterion> standardCriterias) {
		Criteria criteriaResourceItem = em.unwrap(Session.class).createCriteria(ReResourceitem.class);
		criteriaResourceItem.createAlias("resourceAtom", "resource");
		for (Criterion criterion : resourceItemCriterias) {
			criteriaResourceItem.add(criterion);
		}
		List<ReResourceitem> itemList = criteriaResourceItem.list();
		List<Integer> resourceIdList = getLimitedResourceRefList(generateResourceIdList(itemList));
		if (!itemList.isEmpty()) {
			Map<String, Integer> resourceItemIdList = findMinAndMaxId(itemList);
			if (!turkishCriterias.isEmpty()) {
				itemList = generateTurkishItemList(itemList, turkishCriterias, resourceItemIdList, resourceIdList);
				if (itemList.isEmpty()) {
					return new ArrayList();
				}
				resourceItemIdList = findMinAndMaxId(itemList);
			}
			if (!englishCriterias.isEmpty()) {
				itemList = generateEnglishItemList(itemList, englishCriterias, resourceItemIdList, resourceIdList);
				if (itemList.isEmpty()) {
					return new ArrayList();
				}
				resourceItemIdList = findMinAndMaxId(itemList);
			}
			if (!standardCriterias.isEmpty()) {
				itemList = generateStandardItemList(itemList, standardCriterias, resourceItemIdList, resourceIdList);
				if (itemList.isEmpty()) {
					return new ArrayList();
				}
				resourceItemIdList = findMinAndMaxId(itemList);
			}
		}
		return itemList;
	}

	private List<Integer> generateResourceIdList(List<ReResourceitem> itemList) {
		List<Integer> resourceIdList = new ArrayList<>();
		for (ReResourceitem item : itemList) {
			if (!resourceIdList.contains(item.getResourceref())) {
				resourceIdList.add(item.getResourceref());
			}
		}
		return resourceIdList;
	}

	private List<ReResourceitem> generateTurkishItemList(List<ReResourceitem> itemList, List<Criterion> turkishCriterias, Map<String, Integer> resourceItemIdList,
			List<Integer> resourceIdList) {
		List<ReResourceitem> removedItemList = new ArrayList<>();
		boolean isEmpyCriteria = false;
		Criteria criteriaTurkish = em.unwrap(Session.class).createCriteria(ReTurkishtr.class);
		criteriaTurkish.add(Restrictions.in("resourceref", resourceIdList));
		for (Criterion criterion : turkishCriterias) {
			if (criterion.toString().contains(LogoResConstants.ISEMPTY_CONTROL)) {
				isEmpyCriteria = true;
			} else {
				criteriaTurkish.add(criterion);
			}
		}
		List<ReTurkishtr> turkishList = criteriaTurkish.list();
		if (turkishList.isEmpty()) {
			return new ArrayList<>();
		}
		Iterator<ReResourceitem> itemIterator = itemList.iterator();
		while (itemIterator.hasNext()) {
			ReResourceitem reResourceitem = itemIterator.next();
			boolean isFound = false;
			for (ReTurkishtr tr : turkishList) {
				if (tr.getResourceitemref().equals(reResourceitem.getId())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				removedItemList.add(reResourceitem);
				itemIterator.remove();
			}
		}
		if (isEmpyCriteria) {
			return removedItemList;
		}
		return itemList;
	}

	private List<ReResourceitem> generateEnglishItemList(List<ReResourceitem> itemList, List<Criterion> englishCriterias, Map<String, Integer> resourceItemIdList,
			List<Integer> resourceIdList) {
		List<ReResourceitem> removedItemList = new ArrayList<>();
		boolean isEmpyCriteria = false;
		Criteria criteriaEnglish = em.unwrap(Session.class).createCriteria(ReEnglishus.class);
		criteriaEnglish.add(Restrictions.in("resourceref", resourceIdList));
		for (Criterion criterion : englishCriterias) {
			if (criterion.toString().contains(LogoResConstants.ISEMPTY_CONTROL)) {
				isEmpyCriteria = true;
			} else {
				criteriaEnglish.add(criterion);
			}
		}
		List<ReEnglishus> englishList = criteriaEnglish.list();
		if (englishList.isEmpty()) {
			return new ArrayList<>();
		}
		Iterator<ReResourceitem> itemIterator = itemList.iterator();
		while (itemIterator.hasNext()) {
			ReResourceitem reResourceitem = itemIterator.next();
			boolean isFound = false;
			for (ReEnglishus us : englishList) {
				if (us.getResourceitemref().equals(reResourceitem.getId())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				removedItemList.add(reResourceitem);
				itemIterator.remove();
			}
		}
		if (isEmpyCriteria) {
			return removedItemList;
		}
		return itemList;
	}

	private List<ReResourceitem> generateStandardItemList(List<ReResourceitem> itemList, List<Criterion> standardCriterias, Map<String, Integer> resourceItemIdList,
			List<Integer> resourceIdList) {
		List<ReResourceitem> removedItemList = new ArrayList<>();
		boolean isEmpyCriteria = false;
		Criteria criteriaStandrd = em.unwrap(Session.class).createCriteria(ReStandard.class);
		criteriaStandrd.add(Restrictions.in("resourceref", resourceIdList));
		for (Criterion criterion : standardCriterias) {
			if (criterion.toString().contains(LogoResConstants.ISEMPTY_CONTROL)) {
				isEmpyCriteria = true;
			} else {
				criteriaStandrd.add(criterion);
			}
		}
		List<ReStandard> standardList = criteriaStandrd.list();
		if (standardList.isEmpty()) {
			return new ArrayList<>();
		}
		Iterator<ReResourceitem> itemIterator = itemList.iterator();
		while (itemIterator.hasNext()) {
			ReResourceitem reResourceitem = itemIterator.next();
			boolean isFound = false;
			for (ReStandard st : standardList) {
				if (st.getResourceitemref().equals(reResourceitem.getId())) {
					isFound = true;
					break;
				}
			}
			if (!isFound) {
				removedItemList.add(reResourceitem);
				itemIterator.remove();
			}
		}
		if (isEmpyCriteria) {
			return removedItemList;
		}
		return itemList;
	}

	private <T extends AbstractBaseEntity> Map<String, Integer> findMinAndMaxId(List<T> itemList) {
		int minId = itemList.get(0).getId();
		int maxId = itemList.get(0).getId();
		for (T item : itemList) {
			if (item.getId() > maxId) {
				maxId = item.getId();
			}
			if (item.getId() < minId) {
				minId = item.getId();
			}
		}
		Map<String, Integer> listInfo = new HashMap<>();
		listInfo.put("min", minId);
		listInfo.put("max", maxId);
		return listInfo;
	}

	private List<Integer> getLimitedResourceRefList(List<Integer> resourceRefList) {
		if (resourceRefList.size() > 2000) {
			resourceRefList = resourceRefList.subList(0, 2000);
		}
		return resourceRefList;
	}

	@Override
	public void updateOrderNumbers(List<ReResourceitem> itemList) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE RE_RESOURCEITEMS SET ORDERNR = ? WHERE ID = ?";
		List<Object[]> parameters = new ArrayList<Object[]>();
		for (ReResourceitem item : itemList) {
			parameters.add(new Object[] { item.getOrdernr(), item.getId() });
		}
		jdbcTemplate.batchUpdate(sql, parameters);
	}

	@Override
	public Integer getMaximumOrderNumberByResourceRef(int resourceref) {
		ReResourceitem item = repository.findTop1ByresourcerefOrderByOrdernrDesc(resourceref);
		if (item != null) {
			return item.getOrdernr();
		} else {
			return null;
		}
	}

	@Override
	public Integer getMaximumTagNumberByResourceRef(int resourceref) {
		ReResourceitem item = repository.findTop1ByresourcerefOrderByTagnrDesc(resourceref);
		if (item != null) {
			return item.getTagnr();
		} else {
			return null;
		}
	}

}