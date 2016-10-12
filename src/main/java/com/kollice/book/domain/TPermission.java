package com.kollice.book.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * TPermission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_PERMISSION", schema = "BOOK")
public class TPermission implements java.io.Serializable {

	// Fields

	private String id;
	private String permissionname;
	private String url;

	// Constructors

	/** default constructor */
	public TPermission() {
	}

	/** minimal constructor */
	public TPermission(String id) {
		this.id = id;
	}

	/** full constructor */
	public TPermission(String id, String permissionname, String url) {
		this.id = id;
		this.permissionname = permissionname;
		this.url = url;
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

	@Column(name = "PERMISSIONNAME", length = 36)
	public String getPermissionname() {
		return this.permissionname;
	}

	public void setPermissionname(String permissionname) {
		this.permissionname = permissionname;
	}

	@Column(name = "URL", length = 200)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}