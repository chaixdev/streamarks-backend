package be.chaixdev.streamarksbackend.model;

import lombok.Data;

@Data
public class Note {
    private String id;

    private String title;
    private String body;
    private Interval interval;
}
