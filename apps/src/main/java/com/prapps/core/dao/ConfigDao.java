package com.prapps.core.dao;

import java.util.List;

public interface ConfigDao {

	void save(Object object);

	List loadEntity(Class clazz, String filterColumn, Object filterValue);

	List loadEntity(Class clazz);

	List loadEntity(Class clazz, String[] filterColumns, Object[] filterValues, String[] selectedColumns);

	Object loadDistinctEntity(Class clazz, String[] filterColumns, Object[] filterValues, String[] selectedColumns, boolean unique);

	Object loadDistinctEntity(Class clazz, String filterColumn, Object filterValue);

	void delete(Object object);

	Object loadDistinctEntity(Class clazz, String[] filterColumns, Object[] filterValues);

	Object findObjectById(Class clazz, long id);

	void update(Object object);
}