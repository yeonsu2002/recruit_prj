package kr.co.sist.admin.dashboard;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<Map<String, Object>> getUserCountByDate();
    List<Map<String, Object>> getCorpCountByResume();
    List<Map<String, Object>> getCorpCountByIndustry();
}