package com.kollice.book.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * TUsers entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_USERS", schema = "BOOK")
public class TUsers implements java.io.Serializable {

	// Fields

	private String id;
	private String username;
	private String password;

	// Constructors

	/** default constructor */
	public TUsers() {
	}

	/** minimal constructor */
	public TUsers(String id) {
		this.id = id;
	}

	/** full constructor */
	public TUsers(String id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	// Property accessors
	@Id
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GenericGenerator(name="uuid_s",strategy="uuid")
    @GeneratedValue(generator="uuid_s")
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "USERNAME", length = 36)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", length = 36)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}