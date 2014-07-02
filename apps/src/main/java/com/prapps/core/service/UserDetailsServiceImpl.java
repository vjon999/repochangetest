package com.prapps.core.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prapps.core.dao.ConfigDao;
import com.prapps.core.domain.Role;
import com.prapps.core.domain.User;


@Service("userDetailsService")
@Transactional(readOnly=true)
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ConfigDao yavniDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = (User) yavniDao.loadDistinctEntity(User.class, new String[] {"userName"}, new String[] {username}, null, true);
		if (user == null)
		      throw new UsernameNotFoundException("user not found");
	    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	    for(Role role:user.getRoles()) {
	        authorities.add(new SimpleGrantedAuthority(role.getName()));
	    }
		return new org.springframework.security.core.userdetails.User(
				user.getUserName(),
	            user.getPassword().toLowerCase(),
	            authorities);
	}

}
