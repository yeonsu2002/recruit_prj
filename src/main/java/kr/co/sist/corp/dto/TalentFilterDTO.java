package kr.co.sist.corp.dto;

import lombok.Data;
import lombok.NoArgsConstructor; // 기본 생성자 추가 (Lombok @Data는 기본 생성자를 포함하지만 명시적으로 추가해도 좋음)

@Data
@NoArgsConstructor 
public class TalentFilterDTO {
    private String keyword;
    private String gender;
    private String career;
    private Integer minCareerMonths;
    private Integer maxCareerMonths;
    private String address;
    private String education;
    private String techStack;
    private String position;

    private String sortBy;
    private String order;
    private int offset;
    private int size;
    private long corpNo; 
    private String age;
    private Integer minAge;
    private Integer maxAge;

    public void setAge(String age) {
        this.age = age;
        if (age != null && !age.isEmpty()) {
            if (age.contains("-")) {
                String[] parts = age.split("-");
                try {
                    this.minAge = Integer.parseInt(parts[0]);
                    this.maxAge = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    this.minAge = null;
                    this.maxAge = null;
                }
            } else if (age.equals("51+")) {
                this.minAge = 51;
                this.maxAge = null;
            }
        } else {
            this.minAge = null;
            this.maxAge = null;
        }
    }

    
    public void setCareer(String career) {
        this.career = career;
        if (career != null && !career.isEmpty()) {
            String[] parts = career.split("-");
            if (parts.length == 2) {
                try {
                    this.minCareerMonths = Integer.parseInt(parts[0]);
                    this.maxCareerMonths = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    this.minCareerMonths = null;
                    this.maxCareerMonths = null;
                    System.err.println("Invalid career format: " + career);
                }
            } else if (parts.length == 1 && career.equals("0-0")) {
                this.minCareerMonths = 0;
                this.maxCareerMonths = 0;
            }
        } else {
            this.minCareerMonths = null;
            this.maxCareerMonths = null;
        }
    }
}