package dartsgame.entity;

import javax.persistence.*;

@Entity
@Table(name = "dartsGame")
public class DartsGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gameId;
    @Column
    private String playerOne = "";
    @Column
    private String playerTwo = "";
    @Column
    private String gameStatus;
    @Column
    private int playerOneScores = 501;
    @Column
    private int playerTwoScores = 501;
    @Column
    private String turn;

    public DartsGame() {
    }

    public long getGameId() {
        return gameId;
    }

    public DartsGame setGameId(long gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public DartsGame setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
        return this;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public DartsGame setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
        return this;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public DartsGame setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }

    public int getPlayerOneScores() {
        return playerOneScores;
    }

    public DartsGame setPlayerOneScores(int playerOneScores) {
        this.playerOneScores = playerOneScores;
        return this;
    }

    public int getPlayerTwoScores() {
        return playerTwoScores;
    }

    public DartsGame setPlayerTwoScores(int playerTwoScores) {
        this.playerTwoScores = playerTwoScores;
        return this;
    }

    public String getTurn() {
        return turn;
    }

    public DartsGame setTurn(String turn) {
        this.turn = turn;
        return this;
    }
}
