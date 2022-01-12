package dartsgame.service;

import dartsgame.entity.DartsGame;
import dartsgame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GameService {
    private final GameRepository gameRepository;

    List<String> openStatus = List.of("created", "started", "playing");
    List<String> playingStatus = List.of("started", "playing");

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<DartsGame> findAll() {
        return Optional.of(gameRepository.findAllByOrderByGameIdDesc())
                .orElse(new ArrayList<>());
    }

    public Long addGame(DartsGame dartsGame) {
        return gameRepository.save(dartsGame).getGameId();
    }

    public boolean findOpenedGames(String username) {

        List<DartsGame> games = gameRepository.findAllByPlayerOne(username).orElse(new ArrayList<>());
        if (games.isEmpty()) {
            return false;
        }
        return games.stream().anyMatch(x -> openStatus.contains(x.getGameStatus()));

//        return gamesOptional.map(dartsGames -> dartsGames.stream()
//                .anyMatch(x -> openStatus.contains(x.getGameStatus()))).orElse(false);
//        return true;

    }

    public Optional<DartsGame> findCurrentGame(String username) {
        List<DartsGame> games1 = gameRepository.findAllByPlayerOne(username).orElse(new ArrayList<>());
        List<DartsGame> games2 = gameRepository.findAllByPlayerTwo(username).orElse(new ArrayList<>());
        List<DartsGame> games = Stream.concat(games1.stream(), games2.stream())
                .collect(Collectors.toList());
        return games.stream()
                .filter(x -> openStatus.contains(x.getGameStatus()) || x.getGameStatus().endsWith("wins!"))
                .sorted(Comparator.comparingLong(DartsGame::getGameId).reversed())
                .findFirst();
    }

    public Optional<DartsGame> findGameInProgress(String username) {
        List<DartsGame> games1 = gameRepository.findAllByPlayerOne(username).orElse(new ArrayList<>());
        List<DartsGame> games2 = gameRepository.findAllByPlayerTwo(username).orElse(new ArrayList<>());
        List<DartsGame> games = Stream.concat(games1.stream(), games2.stream())
                .collect(Collectors.toList());
        return games.stream()
                .filter(x -> playingStatus.contains(x.getGameStatus()))
                .findFirst();
    }

    public Optional<DartsGame> findById(Long id) {
        return gameRepository.findById(id);
    }
}