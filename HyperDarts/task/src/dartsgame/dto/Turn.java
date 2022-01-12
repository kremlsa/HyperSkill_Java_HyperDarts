package dartsgame.dto;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private String first = "";
    private String second = "";
    private String third = "";
    private String playerName;
    private List<Turn> history = new ArrayList<>();

    public Turn() {
    }

    @Override
    public String toString() {
        return "Turn{" +
                ", playerName='" + playerName + '\'' +
                "first='" + first + '\'' +
                ", second='" + second + '\'' +
                ", third='" + third + '\'' +
                '}';
    }

    public Turn(String first) {
        this.first = first;
    }

    public Turn(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public Turn(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getFirst() {
        return first;
    }

    public Turn setFirst(String first) {
        this.first = first;
        return this;
    }

    public String getSecond() {
        return second;
    }

    public Turn setSecond(String second) {
        this.second = second;
        return this;
    }

    public String getThird() {
        return third;
    }

    public Turn setThird(String third) {
        this.third = third;
        return this;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Turn setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    @JsonIgnore
    public List<Turn> getHistory() {
        return history;
    }

    @JsonIgnore
    public Turn setHistory(List<Turn> history) {
        this.history = history;
        return this;
    }
}
