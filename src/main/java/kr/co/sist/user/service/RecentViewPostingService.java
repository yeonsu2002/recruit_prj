package kr.co.sist.user.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.user.dto.RecentViewPostingDTO;
import kr.co.sist.user.mapper.RecentViewPostingMapper;

/*
 * @Service public class RecentViewPostingService {
 * 
 * @Autowired private RecentViewPostingMapper rvpm;
 * 
 * public String insertRecentViewed(String email, Integer jobPostingSeq) {
 * 
 * RecentViewPostingDTO dto=new RecentViewPostingDTO(); dto.setEmail(email);
 * dto.setJobPostingSeq(jobPostingSeq);
 * 
 * String now=LocalDateTime.now().format(DateTimeFormatter.
 * ofPattern("yyyy-MM-dd HH:mm:ss")); dto.setOpenedAt(now);
 * 
 * //return rvpm.insertRecentViewed(dto);
 * 
 * }
 * 
 * }
 */