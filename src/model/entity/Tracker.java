package model.entity;

import lombok.Getter;
import lombok.Setter;
import model.activity.Activity;
import model.activity.ExplorationCollection;
import model.data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Tracker implements SimulableEntity {
    public static final int DATA_THRESHOLD = 3;

    private String id;
    private int x, y;
    private int vx = 2, vy = -1;
    private List<Data> data = new ArrayList<>();
    private Activity activity;
    private final Random random = new Random();

    public Tracker(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.activity = new ExplorationCollection();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Random getRandom() {
        return random;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int maxX, int maxY) {
        if (random.nextInt(30) == 0) {
            vx = random.nextInt(5) - 2; // entre -2 et 2
            vy = random.nextInt(5) - 2;
            if (vx == 0 && vy == 0) vx = 1;
        }
        x += vx;
        y += vy;
        if (x <= 0) {
            x = 0;
            vx = Math.abs(vx);
        } else if (x >= maxX) {
            x = maxX;
            vx = -Math.abs(vx);
        }

        if (y <= 0) {
            y = 0;
            vy = Math.abs(vy);
        } else if (y >= maxY) {
            y = maxY;
            vy = -Math.abs(vy);
        }
    }

    public boolean hasCollectedEnoughData() {
        return data.size() >= DATA_THRESHOLD;
    }

    public void clearData() {
        data.clear();
    }
}