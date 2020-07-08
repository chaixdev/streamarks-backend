package be.chaixdev.streamarksbackend.rest.dto;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.model.Subject;

import java.util.List;
import java.util.Set;

public class TopicDto {

    private final String type = "Topic";
    private String subjectId;
    private Set<String> tag;
    private List<String> folderPath;



}
