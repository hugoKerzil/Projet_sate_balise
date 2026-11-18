package model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stream implements Data {

    private int vitesse;

    private DirectionEnum direction;

    @Override
    public String getType() {
        return "";
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
