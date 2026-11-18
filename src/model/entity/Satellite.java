package model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Satellite implements SimulableEntity {
    private String id;
    private int x, y;
    private int speed = 2;

    public Satellite(String id, int x, int y, int speed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.speed = Math.max(1, speed);
    }

    @Override
    public void move(int maxX, int maxY) {
        // Déplacement uniquement de gauche à droite
        x += speed;
        if (x > maxX) {
            x = -70;
        }
    }
}