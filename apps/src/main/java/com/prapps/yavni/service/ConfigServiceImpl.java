package com.prapps.yavni.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prapps.yavni.dao.YavniDao;
import com.prapps.yavni.domain.Config;

@Service(value="configService")
@Transactional(readOnly=true)
public class ConfigServiceImpl implements ConfigService {
	
	@Autowired
	private YavniDao yavniDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> loadCities() {
		return yavniDao.loadEntity(Config.class, new String[]{"type"}, new String[]{"CITY"}, new String[]{"desc"});
	}
}
