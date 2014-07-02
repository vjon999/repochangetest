package com.prapps.core.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 124809048698999960L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="USER_ID")
	private long userId;
	@Column(name="USER_NAME")
	private String userName;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="FIRST_NAME")
	private String firstName;
	@Column(name="LAST_NAME")
	private String lastName;
	//@Column(name="ROLE")
	private transient String roleStr;
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)  
    @JoinTable(name="USER_ROLES",  
        joinColumns = {@JoinColumn(name="USER_ID")},  
        inverseJoinColumns = {@JoinColumn(name="ROLE_ID")}  
    )  
    private Set<Role> roles;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRoleStr() {
		if(roles != null) {
			roleStr = "";
			for(Role r : roles) {
				if(null != r && r.getName()!=null) {
					roleStr += r.getName()+", ";
				}
			}
			if(roleStr!=null && !roleStr.isEmpty() && roleStr.endsWith(", ")) {
				roleStr.substring(0, roleStr.length()-2);
			}
		}
		return roleStr;
	}
	public void setRoleStr(String roleStr) {
		this.roleStr = roleStr;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", roleStr=" + roleStr + ", roles=" + roles + "]";
	}
}
