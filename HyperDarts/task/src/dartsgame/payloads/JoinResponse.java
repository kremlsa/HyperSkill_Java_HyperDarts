package dartsgame.payloads;

public class JoinResponse {
    private String status;

    public JoinResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public JoinResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}



