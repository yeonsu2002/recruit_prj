package kr.co.sist.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.user.dto.FavoriteCompanyDTO;
import kr.co.sist.user.dto.MyApplicantDTO;
import kr.co.sist.user.dto.MyApplicantSearchDTO;
import kr.co.sist.user.dto.MyPostingDTO;

@Mapper
public interface MyPageMapper {

	
	public List<MyApplicantDTO> selectMyApplicant(String email);
	public List<MyApplicantDTO> selectMyAllApplicant(MyApplicantSearchDTO searchDTO);
	
	public int updateApplicationCancel(int jobApplicationSeq);
	public int deleteApplication(int jobApplicationSeq);
	
	//스크랩한 공고
	public List<MyPostingDTO> selectMyScrapPosting(String email);
	public List<MyPostingDTO> selectMyNextScrapPosting(Map<String, Object> map);
	public int cntMyScrapPosting(String email);
	
	//최근 본 공고
	public List<MyPostingDTO> selectMyRecentPosting(String email);
	public List<MyPostingDTO> selectMyNextRecentPosting(Map<String, Object> map);
	public int cntMyRecentPosting(String email);
	
	//관심 기업
	public List<FavoriteCompanyDTO> selectMyFavoriteCompany(String email);
	public List<FavoriteCompanyDTO> selectMyNextFavoriteCompany(Map<String, Object> map);
	public int cntMyFavoriteCompany(String email);
	
}
