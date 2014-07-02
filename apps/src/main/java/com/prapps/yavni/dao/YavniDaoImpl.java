package com.prapps.yavni.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YavniDaoImpl implements YavniDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public List loadEntity(Class clazz) {
		return getSession().createCriteria(clazz).list();
	}
	
	@Override
	public List loadEntity(Class clazz, String filterColumn, Object filterValue) {
		return getSession().createCriteria(clazz).add(Restrictions.eq(filterColumn, filterValue)).list();
	}
	
	@Override
	public Object loadDistinctEntity(Class clazz, String filterColumn, Object filterValue) {
		return getSession().createCriteria(clazz).add(Restrictions.eq(filterColumn, filterValue)).uniqueResult();
	}
	
	@Override
	public Object loadDistinctEntity(Class clazz, String[] filterColumns, Object[] filterValues, String[] selectedColumns, boolean unique) {
		Criteria criteria = getSession().createCriteria(clazz);
		for(int i=0;i<filterColumns.length;i++) {
			if(null != filterValues[i]) {
				criteria.add(Restrictions.eq(filterColumns[i], filterValues[i]));
			}
			else {
				criteria.add(Restrictions.isNull(filterColumns[i]));
			}
		}
		
		if(selectedColumns != null && selectedColumns.length > 0) {
			ProjectionList projectionList = Projections.projectionList();
			for(String column : selectedColumns) {
				 projectionList.add( Projections.property(column), column);
			}
			criteria.setProjection(projectionList);
			criteria.setResultTransformer(Transformers.aliasToBean((clazz)));
		}
		return criteria.uniqueResult();
	}
	
	@Override
	public List loadEntity(Class clazz, String[] filterColumns, Object[] filterValues, String[] selectedColumns) {
		Criteria criteria = getSession().createCriteria(clazz);
		if(null != filterColumns && filterColumns.length > 0) {
			for(int i=0;i<filterColumns.length;i++) {
				if(filterValues[i] instanceof String) {
					if(null != filterValues[i]) {
						criteria.add(Restrictions.like(filterColumns[i], filterValues[i]));
					}
					else {
						criteria.add(Restrictions.isNull(filterColumns[i]));
					}
				}
				else {
					if(null != filterValues[i]) {
						criteria.add(Restrictions.eq(filterColumns[i], filterValues[i]));
					}
					else {
						criteria.add(Restrictions.isNull(filterColumns[i]));
					}
				}
			}
		}
		
		if(selectedColumns != null && selectedColumns.length > 0) {
			ProjectionList projectionList = Projections.projectionList();
			for(String column : selectedColumns) {
				 projectionList.add( Projections.property(column), column);
			}
			criteria.setProjection(projectionList);
		}
		return criteria.list();
	}
}
