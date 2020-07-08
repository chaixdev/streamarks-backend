package be.chaixdev.streamarksbackend.model;

import lombok.Data;

@Data
public class Note {

    private String summary;
    private String body;

    private Interval interval;

    private String dateCreated;
    private String dateModified;
}
