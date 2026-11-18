package model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Turbidity  implements Data{

    private int value;

    @Override
    public String getType() {
        return "";
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public Object getValue() {
        return "";
    }
}
