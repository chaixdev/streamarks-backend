package be.chaixdev.streamarksbackend.model;

import lombok.Data;
import lombok.Getter;

import java.time.Duration;

@Data
public class Subject {

    private Type type;
    private String uri;
    private String subjectTitle;
    private Duration duration;

}
