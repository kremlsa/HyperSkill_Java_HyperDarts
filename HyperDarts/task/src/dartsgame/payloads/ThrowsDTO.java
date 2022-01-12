package dartsgame.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class ThrowsDTO {
    private String first;
    private String second;
    private String third;

    public ThrowsDTO() {
    }

    public boolean firstIsDouble() {
        return Objects.equals(this.first.split(":")[0], "2");
    }

    public boolean secondIsDouble() {
        return Objects.equals(this.second.split(":")[0], "2");
    }

    public boolean thirdIsDouble() {
        return Objects.equals(this.third.split(":")[0], "2");
    }

    public int firstScore() {
        return Integer.parseInt(this.first.split(":")[0]) * Integer.parseInt(this.first.split(":")[1]);
    }

    public int secondScore() {
        return Integer.parseInt(this.second.split(":")[0]) * Integer.parseInt(this.second.split(":")[1]);
    }

    public int thirdScore() {
        return Integer.parseInt(this.third.split(":")[0]) * Integer.parseInt(this.third.split(":")[1]);
    }

    public String getFirst() {
        return first;
    }

    public ThrowsDTO setFirst(String first) {
        this.first = first;
        return this;
    }

    public String getSecond() {
        return second;
    }

    public ThrowsDTO setSecond(String second) {
        this.second = second;
        return this;
    }

    public String getThird() {
        return third;
    }

    public ThrowsDTO setThird(String third) {
        this.third = third;
        return this;
    }
}
