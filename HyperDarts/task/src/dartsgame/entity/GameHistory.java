package dartsgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table()
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private long gameId;
    @Column
    private long move;
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

    public GameHistory() {
    }

    @JsonIgnore
    public long getId() {
        return id;
    }

    public GameHistory setId(long id) {
        this.id = id;
        return this;
    }

    public long getGameId() {
        return gameId;
    }

    public long getMove() {
        return move;
    }

    public GameHistory setMove(long number) {
        this.move = number;
        return this;
    }

    public GameHistory setGameId(long gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public GameHistory setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
        return this;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public GameHistory setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
        return this;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public GameHistory setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }

    public int getPlayerOneScores() {
        return playerOneScores;
    }

    public GameHistory setPlayerOneScores(int playerOneScores) {
        this.playerOneScores = playerOneScores;
        return this;
    }

    public int getPlayerTwoScores() {
        return playerTwoScores;
    }

    public GameHistory setPlayerTwoScores(int playerTwoScores) {
        this.playerTwoScores = playerTwoScores;
        return this;
    }

    public String getTurn() {
        return turn;
    }

    public GameHistory setTurn(String turn) {
        this.turn = turn;
        return this;
    }
}
