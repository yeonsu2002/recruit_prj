package kr.co.sist.admin.dashboard;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DashboardMapper {
    List<Map<String, Object>> selectUserCnt();
    List<Map<String, Object>> selectCorpCnt();
    List<Map<String, Object>> selectIndCnt();
}