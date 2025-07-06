package kr.co.sist.user.entity;

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
@Table(name = "LINK")
public class LinkEntity {

	@Id
	@Column(name = "LINK_SEQ")
	private int linkSeq;
	
	@Column(name = "RESUME_SEQ")
	private int resumeSeq;
	
	@Column(name = "GITHUB_URL")
	private String githubUrl;
	@Column(name = "NOTION_URL")
	private String notionUrl;
	@Column(name = "BLOG_URL")
	private String blogUrl;
}
