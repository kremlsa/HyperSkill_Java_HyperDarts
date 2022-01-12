package dartsgame.controller;

import dartsgame.dto.CancelDTO;
import dartsgame.dto.GameDTO;
import dartsgame.dto.RevertDTO;
import dartsgame.entity.DartsGame;
import dartsgame.entity.GameHistory;
import dartsgame.payloads.CreateDTO;
import dartsgame.payloads.DefaultResponse;
import dartsgame.payloads.ThrowsDTO;
import dartsgame.service.GameService;
import dartsgame.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private Map<String, String> wrongAnswer = Map.of(
            "status", "Not under construction!"
    );

    private final GameService gameService;
    private final HistoryService historyService;

    @Autowired
    public GameController(GameService gameService, HistoryService historyService) {
        this.gameService = gameService;
        this.historyService = historyService;
    }


    @PostMapping("/create")
    public ResponseEntity<Object> createGame(@RequestBody @Valid CreateDTO createDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int field = createDTO.getTargetScore();
        if (field != 101 && field != 301 && field != 501) {
            return new ResponseEntity<>(new DefaultResponse("Wrong target score!"), HttpStatus.BAD_REQUEST);
        }
        if (gameService.findOpenedGames(authentication.getName())) {
            return new ResponseEntity<>(new DefaultResponse("You have an unfinished game!"), HttpStatus.BAD_REQUEST);
        }
        DartsGame dartsGame = new DartsGame();
        dartsGame.setPlayerOne(authentication.getName());
        dartsGame.setPlayerOneScores(createDTO.getTargetScore());
        dartsGame.setPlayerTwoScores(createDTO.getTargetScore());
        dartsGame.setGameStatus("created");
        dartsGame.setTurn(authentication.getName());
        long gamedId = gameService.addGame(dartsGame);
        GameDTO gd = new GameDTO().setGameId(gamedId)
                .setGameStatus(dartsGame.getGameStatus())
                .setPlayerOne(dartsGame.getPlayerOne())
                .setPlayerTwo(dartsGame.getPlayerTwo())
                .setPlayerOneScores(dartsGame.getPlayerOneScores())
                .setPlayerTwoScores(dartsGame.getPlayerTwoScores())
                .setTurn(dartsGame.getPlayerOne());
        return new ResponseEntity<>(gd, HttpStatus.OK);
    }


    @GetMapping("/list")
    public ResponseEntity<Object> listGames() {
        if (gameService.findAll().isEmpty()) {
            return new ResponseEntity<>(gameService.findAll(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(gameService.findAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/join/{gameId}")
    public ResponseEntity<Object> joinGame(@PathVariable String gameId) {
        List<String> openStatus = List.of("created", "started", "playing");
        Long id = Long.valueOf(gameId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<DartsGame> game = gameService.findById(id);
        if (game.isPresent()) {
            DartsGame g = game.get();
            if (g.getGameStatus().equals("created")) {
                if (g.getPlayerOne().equals(authentication.getName())) {
                    return new ResponseEntity<>(new DefaultResponse("You can't play alone!"),
                            HttpStatus.BAD_REQUEST);
                }
                List<DartsGame> games = gameService.findAll();
                if (games.stream()
                        .filter(x -> openStatus.contains(x.getGameStatus()) )
                        .anyMatch(x -> x.getPlayerTwo().equals(authentication.getName())
                                || x.getPlayerOne().equals(authentication.getName()))) {
                    return new ResponseEntity<>(new DefaultResponse("You have an unfinished game!"),
                            HttpStatus.BAD_REQUEST);
                }

                g.setPlayerTwo(authentication.getName());
                g.setGameStatus("started");
                GameHistory gh = new GameHistory().setGameId(g.getGameId())
                        .setMove(0L)
                        .setGameStatus(g.getGameStatus())
                        .setPlayerOne(g.getPlayerOne())
                        .setPlayerTwo(g.getPlayerTwo())
                        .setPlayerOneScores(g.getPlayerOneScores())
                        .setPlayerTwoScores(g.getPlayerTwoScores())
                        .setTurn(g.getPlayerOne());
                gameService.addGame(g);
                historyService.addHistory(gh);
                GameDTO gd = new GameDTO().setGameId(g.getGameId())
                        .setGameStatus(g.getGameStatus())
                        .setPlayerOne(g.getPlayerOne())
                        .setPlayerTwo(g.getPlayerTwo())
                        .setPlayerOneScores(g.getPlayerOneScores())
                        .setPlayerTwoScores(g.getPlayerTwoScores())
                        .setTurn(g.getPlayerOne());
                return new ResponseEntity<>(gd, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DefaultResponse("You can't join the game!"),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new DefaultResponse("Game not found!"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Object> gameStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (gameService.findCurrentGame(authentication.getName()).isPresent()) {
            DartsGame dg = gameService.findCurrentGame(authentication.getName()).get();
            GameDTO gd = new GameDTO().setGameId(dg.getGameId())
                    .setGameStatus(dg.getGameStatus())
                    .setPlayerOne(dg.getPlayerOne())
                    .setPlayerTwo(dg.getPlayerTwo())
                    .setPlayerOneScores(dg.getPlayerOneScores())
                    .setPlayerTwoScores(dg.getPlayerTwoScores())
                    .setTurn(dg.getTurn());
            return new ResponseEntity<>(gd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/throws")
    public ResponseEntity<Object> makeTurn(@RequestBody ThrowsDTO throwsDTO) {
        if (!throwsDTO.getFirst().matches("(none)|(1:25)|(2:25)|([1-3]:20)|([1-3]:[1][0-9])|([1-3]:[0-9])")
                || !throwsDTO.getSecond().matches("(none)|(1:25)|(2:25)|([1-3]:20)|([1-3]:[1][0-9])|([1-3]:[0-9])")
                || !throwsDTO.getThird().matches("(none)|(1:25)|(2:25)|([1-3]:20)|([1-3]:[1][0-9])|([1-3]:[0-9])")) {
            return new ResponseEntity<>(new DefaultResponse("Wrong throws!"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (gameService.findGameInProgress(authentication.getName()).isPresent()) {
            DartsGame dg = gameService.findGameInProgress(authentication.getName()).get();
            if (dg.getGameStatus().equals("started")) {
                dg.setGameStatus("playing");
            }
            GameDTO gd = new GameDTO().setGameId(dg.getGameId())
                    .setGameStatus(dg.getGameStatus())
                    .setPlayerOne(dg.getPlayerOne())
                    .setPlayerTwo(dg.getPlayerTwo())
                    .setPlayerOneScores(dg.getPlayerOneScores())
                    .setPlayerTwoScores(dg.getPlayerTwoScores())
                    .setTurn(dg.getTurn());
            GameHistory gh = new GameHistory().setGameId(dg.getGameId())
                    .setMove(historyService.findLastTurn(dg.getGameId()) + 1L)
                    .setGameStatus(dg.getGameStatus())
                    .setPlayerOne(dg.getPlayerOne())
                    .setPlayerTwo(dg.getPlayerTwo())
                    .setPlayerOneScores(dg.getPlayerOneScores())
                    .setPlayerTwoScores(dg.getPlayerTwoScores())
                    .setTurn(dg.getPlayerOne());
            if (!dg.getTurn().equals(authentication.getName())) {
                return new ResponseEntity<>(new DefaultResponse("Wrong turn!"), HttpStatus.BAD_REQUEST);
            }
            boolean isThrowsOk = true;
            int currentScore = 0;
            if (dg.getPlayerOne().equals(authentication.getName())) {
                currentScore = dg.getPlayerOneScores();
            } else if (dg.getPlayerTwo().equals(authentication.getName())) {
                currentScore = dg.getPlayerTwoScores();
            }

            // First throw
            int thrw = throwsDTO.firstScore();
            if (thrw > currentScore || thrw == currentScore - 1) {
                if (throwsDTO.getSecond().equals("none") && throwsDTO.getThird().equals("none")) {
                    dg.setTurn(changeTurn(dg));
                    gameService.addGame(dg);
                    gd.setTurn(dg.getTurn());
                    gh.setTurn(dg.getTurn());
                    historyService.addHistory(gh);
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new DefaultResponse("Wrong throws!"), HttpStatus.BAD_REQUEST);
                }
            } else if (thrw == currentScore) {
                if (throwsDTO.firstIsDouble()) {
                    if (dg.getPlayerOne().equals(authentication.getName())) {
                        dg.setPlayerOneScores(0);
                    } else if (dg.getPlayerTwo().equals(authentication.getName())) {
                        dg.setPlayerTwoScores(0);
                    }
                    gd.setPlayerOneScores(dg.getPlayerOneScores());
                    gd.setPlayerTwoScores(dg.getPlayerTwoScores());
                    dg.setGameStatus(authentication.getName() + " wins!");
                    gameService.addGame(dg);
                    gd.setGameStatus(dg.getGameStatus());
                    gh.setPlayerOneScores(dg.getPlayerOneScores());
                    gh.setPlayerTwoScores(dg.getPlayerTwoScores());
                    gh.setGameStatus(dg.getGameStatus());
                    historyService.addHistory(gh);
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else if (throwsDTO.getSecond().equals("none") && throwsDTO.getThird().equals("none")) {
                    dg.setTurn(changeTurn(dg));
                    gameService.addGame(dg);
                    gd.setTurn(dg.getTurn());
                    gh.setTurn(dg.getTurn());
                    historyService.addHistory(gh);
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new DefaultResponse("Wrong throws!"), HttpStatus.BAD_REQUEST);
                }
            } else {
                currentScore -= throwsDTO.firstScore();
            }

            // second throw
            thrw = throwsDTO.secondScore();
            if (thrw > currentScore || thrw == currentScore - 1) {
                if (throwsDTO.getThird().equals("none")) {
                    dg.setTurn(changeTurn(dg));
                    gh.setTurn(dg.getTurn());
                    gameService.addGame(dg);
                    historyService.addHistory(gh);
                    gd.setTurn(dg.getTurn());
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new DefaultResponse("Wrong throws!"), HttpStatus.BAD_REQUEST);
                }
            } else if (thrw == currentScore) {
                if (throwsDTO.secondIsDouble()) {
                    if (dg.getPlayerOne().equals(authentication.getName())) {
                        dg.setPlayerOneScores(0);
                    } else if (dg.getPlayerTwo().equals(authentication.getName())) {
                        dg.setPlayerTwoScores(0);
                    }
                    gd.setPlayerOneScores(dg.getPlayerOneScores());
                    gd.setPlayerTwoScores(dg.getPlayerTwoScores());
                    dg.setGameStatus(authentication.getName() + " wins!");
                    gh.setGameStatus(dg.getGameStatus());
                    gh.setPlayerOneScores(dg.getPlayerOneScores());
                    gh.setPlayerTwoScores(dg.getPlayerTwoScores());
                    gameService.addGame(dg);
                    historyService.addHistory(gh);
                    gd.setGameStatus(dg.getGameStatus());
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else if (throwsDTO.getThird().equals("none")) {
                    dg.setTurn(changeTurn(dg));
                    gh.setTurn(dg.getTurn());
                    gameService.addGame(dg);
                    historyService.addHistory(gh);
                    gd.setTurn(dg.getTurn());
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new DefaultResponse("Wrong throws!"), HttpStatus.BAD_REQUEST);
                }
            } else {
                currentScore -= throwsDTO.secondScore();
            }

            // third throw
            thrw = throwsDTO.thirdScore();
            if (thrw > currentScore || thrw == currentScore - 1) {
                dg.setTurn(changeTurn(dg));
                gh.setTurn(dg.getTurn());
                gameService.addGame(dg);
                historyService.addHistory(gh);
                gd.setTurn(dg.getTurn());
                return new ResponseEntity<>(gd, HttpStatus.OK);

            } else if (thrw == currentScore) {
                if (throwsDTO.secondIsDouble()) {
                    if (dg.getPlayerOne().equals(authentication.getName())) {
                        dg.setPlayerOneScores(0);
                    } else if (dg.getPlayerTwo().equals(authentication.getName())) {
                        dg.setPlayerTwoScores(0);
                    }
                    gd.setPlayerOneScores(dg.getPlayerOneScores());
                    gd.setPlayerTwoScores(dg.getPlayerTwoScores());
                    dg.setGameStatus(authentication.getName() + " wins!");
                    gh.setPlayerOneScores(dg.getPlayerOneScores());
                    gh.setPlayerTwoScores(dg.getPlayerTwoScores());
                    gh.setGameStatus(dg.getGameStatus());
                    gameService.addGame(dg);
                    historyService.addHistory(gh);
                    gd.setGameStatus(dg.getGameStatus());
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                } else {
                    dg.setTurn(changeTurn(dg));
                    gameService.addGame(dg);
                    gh.setTurn(dg.getTurn());
                    historyService.addHistory(gh);
                    gd.setTurn(dg.getTurn());
                    return new ResponseEntity<>(gd, HttpStatus.OK);
                }
            } else {
                currentScore -= throwsDTO.thirdScore();
                if (dg.getPlayerOne().equals(authentication.getName())) {
                    dg.setPlayerOneScores(currentScore);
                } else if (dg.getPlayerTwo().equals(authentication.getName())) {
                    dg.setPlayerTwoScores(currentScore);
                }
                dg.setTurn(changeTurn(dg));
                gameService.addGame(dg);
                gh.setPlayerOneScores(dg.getPlayerOneScores());
                gh.setPlayerTwoScores(dg.getPlayerTwoScores());
                gh.setTurn(dg.getTurn());
                historyService.addHistory(gh);
                gd.setPlayerOneScores(dg.getPlayerOneScores());
                gd.setPlayerTwoScores(dg.getPlayerTwoScores());
                gd.setTurn(dg.getTurn());
                return new ResponseEntity<>(gd, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new DefaultResponse("There are no games available!"), HttpStatus.NOT_FOUND);
        }
    }

    private String changeTurn(DartsGame dg) {
        String current = dg.getTurn();
        return current.equals(dg.getPlayerOne()) ? dg.getPlayerTwo() : dg.getPlayerOne();
    }

    @PutMapping("/cancel")
    public ResponseEntity<Object> cancelGame(@RequestBody @Valid CancelDTO cancelDTO) {
        Long gameId = cancelDTO.getGameId();
        String status = cancelDTO.getStatus();

        if (gameService.findById(gameId).isPresent()) {
            DartsGame dg = gameService.findById(gameId).get();
            if (dg.getGameStatus().endsWith(" wins!")) {
                return new ResponseEntity<>(new DefaultResponse("The game is already over!"),
                        HttpStatus.BAD_REQUEST);
            }
            if (!status.equals(dg.getPlayerOne() + " wins!")
                    && !status.equals(dg.getPlayerTwo() + " wins!")
                    && !status.equals("Nobody wins!")) {
                return new ResponseEntity<>(new DefaultResponse("Wrong status!"),
                        HttpStatus.BAD_REQUEST);
            }
            dg.setGameStatus(status);
            gameService.addGame(dg);
            GameDTO gd = new GameDTO().setGameId(dg.getGameId())
                    .setGameStatus(dg.getGameStatus())
                    .setPlayerOne(dg.getPlayerOne())
                    .setPlayerTwo(dg.getPlayerTwo())
                    .setPlayerOneScores(dg.getPlayerOneScores())
                    .setPlayerTwoScores(dg.getPlayerTwoScores())
                    .setTurn(dg.getTurn());
            return new ResponseEntity<>(gd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DefaultResponse("Game not found!"),
                    HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/revert")
    public ResponseEntity<Object> revertGame(@RequestBody @Valid RevertDTO revertDTO) {
        Long gameId = revertDTO.getGameId();
        Long move = revertDTO.getMove();

        if (!historyService.findAllByGameId(gameId).isEmpty()) {
            if (historyService.findLastTurn(gameId).equals(move)) {
                return new ResponseEntity<>(new DefaultResponse("There is nothing to revert!"),
                        HttpStatus.BAD_REQUEST);
            }
            if (historyService.findByNumber(move, gameId).isEmpty()) {
                return new ResponseEntity<>(new DefaultResponse("Move not found!"),
                        HttpStatus.BAD_REQUEST);
            }
            GameHistory gh = historyService.findByNumber(move, gameId).get();
            DartsGame dg = gameService.findById(gameId).get();
            if (dg.getGameStatus().endsWith(" wins!")) {
                return new ResponseEntity<>(new DefaultResponse("The game is over!"),
                        HttpStatus.BAD_REQUEST);
            }
            dg.setGameStatus(gh.getGameStatus());
            dg.setPlayerOne(gh.getPlayerOne());
            dg.setPlayerTwo(gh.getPlayerTwo());
            dg.setTurn(gh.getTurn());
            dg.setPlayerOneScores(gh.getPlayerOneScores());
            dg.setPlayerTwoScores(gh.getPlayerTwoScores());
            gameService.addGame(dg);
            Long i = move + 1L;
            while (i <= historyService.findLastTurn(gameId)) {
                GameHistory ghDel = historyService.findByNumber(i, gameId).get();
                historyService.delete(ghDel);
                i++;
            }
            return new ResponseEntity<>(dg,
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new DefaultResponse("Game not found!"),
                    HttpStatus.NOT_FOUND);
        }
    }

}
