package com.kollice.book.domain;

import javax.persistence.*;

/**
 * TRoles entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_ROLES")
public class TRoles implements java.io.Serializable {

	// Fields

	private String id;
	private String rolename;

	// Constructors

	/** default constructor */
	public TRoles() {
	}

	/** minimal constructor */
	public TRoles(String id) {
		this.id = id;
	}

	/** full constructor */
	public TRoles(String id, String rolename) {
		this.id = id;
		this.rolename = rolename;
	}

	// Property accessors
	@Id
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @GenericGenerator(name="uuid_s",strategy="uuid")
//    @GeneratedValue(generator="uuid_s")
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ROLENAME", length = 36)
	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}