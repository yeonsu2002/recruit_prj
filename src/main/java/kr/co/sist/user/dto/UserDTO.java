package kr.co.sist.user.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private String email;
	private Long corpNo;
	private String password;
	private String name;
	private String role;
	private String phone;
	private Date regDt;
	private Integer loginFailCnt;
	private Integer isLocked;
	private Date lockEndDt;
	private Date pwChangeDt;
	private Date lastLoginDt;
	private String lastLoginIp;
	private String zipcode;
	private String roadAddress;
	private String detailAddress;
	private Date birth;
	private String gender;
	private Integer pwResetRequired;
	private String profileImage;
	private Integer activeStatus;
}
