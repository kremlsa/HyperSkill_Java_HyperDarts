package dartsgame.dto;

public class RevertDTO {
    private Long gameId;
    private Long move;

    public RevertDTO() {
    }

    public Long getGameId() {
        return gameId;
    }

    public RevertDTO setGameId(Long gameId) {
        this.gameId = gameId;
        return this;
    }

    public Long getMove() {
        return move;
    }

    public RevertDTO setMove(Long move) {
        this.move = move;
        return this;
    }
}
