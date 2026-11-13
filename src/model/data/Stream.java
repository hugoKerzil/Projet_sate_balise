package model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stream implements Data {

    private int vitesse;

    private DirectionEnum direction;
}
