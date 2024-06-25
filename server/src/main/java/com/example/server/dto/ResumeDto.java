package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ResumeDto {

    private final String name;
    private final String surname;
    private final List<String> contacts;
    private final List<Education> educations;
    private final List<Experience> experiences;
    private final List<Project> projects;
    private final List<String> programmingLanguages;
    private final List<String> frameworks;
    private final List<String> technologies;
    private final List<String> libraries;


    @AllArgsConstructor
    @Getter
    public static class Education {
        private final String organization;
        private final String degree;
        private final String major;
        private final String location;
        private final String studyPeriod;
    }

    @AllArgsConstructor
    @Getter
    public static class Experience {
        private final String organization;
        private final String workPeriod;
        private final String position;
        private final String location;
        private final List<String> tasks;

    }

    @AllArgsConstructor
    @Getter
    public static class Project {
        private final String projectName;
        private final String workPeriod;
        private final String usedTechnologies;
        private final List<String> tasks;

    }

}
