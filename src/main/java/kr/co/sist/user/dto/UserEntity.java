package kr.co.sist.user.dto;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.sist.corp.dto.CorpEntity;
import lombok.Data;

@Data
@Entity
@Table(name="member")
public class UserEntity {

	@Id
	@Column(name="email")
	private String email;
	
	@ManyToOne
	@JoinColumn(name="corp_no")
	private CorpEntity corpEntity;
	
	@Column(name="password")
	private String password;
	@Column(name="name")
	private String name;
	@Column(name="role")
	private String role;
	@Column(name="phone")
	private String phone;
	@Column(name="reg_dt")
	private String regDt;
	@Column(name="login_fail_cnt")
	private Integer loginFailCnt;
	@Column(name="is_locked")
	private Integer isLocked;
	@Column(name="lock_end_dt")
	private Date lockEndDt;
	@Column(name="pw_change_dt")
	private Date pwChangeDt;
	@Column(name="last_login_dt")
	private Date lastLoginDt;
	@Column(name="last_login_ip")
	private String lastLoginIp;
	@Column(name="zipcode")
	private Integer zipcode;
	@Column(name="road_address")
	private String roadAddress;
	@Column(name="detail_address")
	private String detailAddress;
	@Column(name="birth")
	private Date birth;
	@Column(name="gender")
	private String gender;
	@Column(name="pw_reset_required")
	private String pwResetRequired;
	@Column(name="profile_image")
	private String profileImage;
	@Column(name="active_status")
	private String activeStatus;
	
}
