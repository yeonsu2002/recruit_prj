package kr.co.sist.corp.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TalentsDTO {
    private Long id;
    private String name;
    private String experience;
    private String skills;
    private String contact;
}