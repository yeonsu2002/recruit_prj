package kr.co.sist.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="ADMIN")
public class AdminEntity {
	@Id
	@Column(name="ADMIN_ID")
	private String adminId;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="NAME")
	private String name;
	@Column(name="DEPT")
	private String dept;
	@Column(name="JOG")
	private String job;
	@Column(name="APPROVAL_DATE")
	private String approvalDate;
	@Column(name="STAT")
	private String stat;
	@Column(name="LAST_LOGIN_TIME")
	private String lastLoginTime;
	@Column(name="TEL")
	private String tel;
	@Column(name="ROLE")
	private char role;
}
