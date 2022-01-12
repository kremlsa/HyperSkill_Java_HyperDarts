package dartsgame.dto;

public class GameDTO {
    private long gameId;
    private String playerOne = "Player One";
    private String playerTwo = "Player Two";
    private String gameStatus = "Created";
    private int playerOneScores = 501;
    private int playerTwoScores = 501;
    private String turn;

    public GameDTO() {
    }

    public long getGameId() {
        return gameId;
    }

    public GameDTO setGameId(long gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public GameDTO setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
        return this;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public GameDTO setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
        return this;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public GameDTO setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }

    public int getPlayerOneScores() {
        return playerOneScores;
    }

    public GameDTO setPlayerOneScores(int playerOneScores) {
        this.playerOneScores = playerOneScores;
        return this;
    }

    public int getPlayerTwoScores() {
        return playerTwoScores;
    }

    public GameDTO setPlayerTwoScores(int playerTwoScores) {
        this.playerTwoScores = playerTwoScores;
        return this;
    }

    public String getTurn() {
        return turn;
    }

    public GameDTO setTurn(String turn) {
        this.turn = turn;
        return this;
    }
}
