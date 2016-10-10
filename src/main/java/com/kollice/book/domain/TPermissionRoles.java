package com.kollice.book.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * TPermissionRoles entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_PERMISSION_ROLES", schema = "BOOK")
public class TPermissionRoles implements java.io.Serializable {

	// Fields

	private String id;
	private String permissionid;
	private String roleid;

	// Constructors

	/** default constructor */
	public TPermissionRoles() {
	}

	/** minimal constructor */
	public TPermissionRoles(String id) {
		this.id = id;
	}

	/** full constructor */
	public TPermissionRoles(String id, String permissionid, String roleid) {
		this.id = id;
		this.permissionid = permissionid;
		this.roleid = roleid;
	}

	// Property accessors
	@Id
    @GenericGenerator(name="uuid_s",strategy="uuid")
    @GeneratedValue(generator="uuid_s")
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PERMISSIONID", length = 36)
	public String getPermissionid() {
		return this.permissionid;
	}

	public void setPermissionid(String permissionid) {
		this.permissionid = permissionid;
	}

	@Column(name = "ROLEID", length = 36)
	public String getRoleid() {
		return this.roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

}