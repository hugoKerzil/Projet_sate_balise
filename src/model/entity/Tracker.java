package model.entity;

import lombok.Getter;
import lombok.Setter;
import model.activity.Activity;
import model.data.Data;

import java.util.List;

@Getter
@Setter
public class Tracker {
    private String id;
    private int x, y;
    private List<Data> data;
    private Activity activity;
}