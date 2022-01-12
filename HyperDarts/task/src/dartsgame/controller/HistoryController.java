package dartsgame.controller;

import dartsgame.dto.GameDTO;
import dartsgame.entity.DartsGame;
import dartsgame.entity.GameHistory;
import dartsgame.payloads.DefaultResponse;
import dartsgame.service.GameService;
import dartsgame.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final GameService gameService;
    private final HistoryService historyService;

    @Autowired
    public HistoryController(GameService gameService, HistoryService historyService) {
        this.gameService = gameService;
        this.historyService = historyService;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Object> getHistory(@PathVariable String gameId) {

        Long id;
        try {
            id = Long.valueOf(gameId);
        } catch (Exception e) {
            return new ResponseEntity<>(new DefaultResponse("Wrong request!"),
                    HttpStatus.BAD_REQUEST);
        }
        if (id < 0) {
            return new ResponseEntity<>(new DefaultResponse("Wrong request!"),
                    HttpStatus.BAD_REQUEST);
        }
        List<GameHistory> histories = historyService.findAllByGameId(id);
        if (!histories.isEmpty()) {
            return new ResponseEntity<>(histories, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DefaultResponse("Game not found!"),
                    HttpStatus.NOT_FOUND);
        }
    }
}
