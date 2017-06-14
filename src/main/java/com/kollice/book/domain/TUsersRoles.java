package com.kollice.book.domain;


import javax.persistence.*;

/**
 * TUsersRoles entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_USERS_ROLES")
public class TUsersRoles implements java.io.Serializable {

	// Fields

	private String id;
	private String userid;
	private String roleid;

	// Constructors

	/** default constructor */
	public TUsersRoles() {
	}

	/** minimal constructor */
	public TUsersRoles(String id) {
		this.id = id;
	}

	/** full constructor */
	public TUsersRoles(String id, String userid, String roleid) {
		this.id = id;
		this.userid = userid;
		this.roleid = roleid;
	}

	// Property accessors
	@Id
//    @GenericGenerator(name="uuid_s",strategy="uuid")
//    @GeneratedValue(generator="uuid_s")
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "USERID", length = 36)
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "ROLEID", length = 36)
	public String getRoleid() {
		return this.roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

}