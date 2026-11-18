package model.entity;

import lombok.Getter;
import lombok.Setter;
import model.activity.Activity;
import model.data.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Tracker implements SimulableEntity {
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
    }

    @Override
    public void move(int maxX, int maxY) {
        // Variation aléatoire pour bouger dans toutes les directions
        if (random.nextInt(30) == 0) {
            vx = random.nextInt(5) - 2; // entre -2 et 2
            vy = random.nextInt(5) - 2;
            if (vx == 0 && vy == 0) vx = 1;
        }

        x += vx;
        y += vy;

        // Confinement strict dans la vue océan (0..maxX, 0..maxY)
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
}