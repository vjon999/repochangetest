package com.prapps.yavni.dao;

import java.sql.Date;
import java.util.List;

public interface YavniDao {

	List loadEntity(Class clazz);

	List loadEntity(Class clazz, String filterColumn, Object filterValue);

	Object loadDistinctEntity(Class clazz, String filterColumn, Object filterValue);

	Object loadDistinctEntity(Class clazz, String[] filterColumns, Object[] filterValues, String[] selectedColumns, boolean unique);

	List loadEntity(Class clazz, String[] filterColumns, Object[] filterValues, String[] selectedColumns);


}