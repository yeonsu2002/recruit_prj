package kr.co.sist.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminDTO {
	private String admin_id;
	private String password;
	private String name;
	private String dept;
	private String job;
	private String approval_date;
	private String stat;
	private String last_login_time;
	private char role;
}
