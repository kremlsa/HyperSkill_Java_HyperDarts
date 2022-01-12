package dartsgame.dto;

public class CancelDTO {
    private Long gameId;
    private String status;

    public CancelDTO() {
    }

    public Long getGameId() {
        return gameId;
    }

    public CancelDTO setGameId(Long gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CancelDTO setStatus(String status) {
        this.status = status;
        return this;
    }
}
