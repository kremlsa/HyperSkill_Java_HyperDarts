package dartsgame.service;

import dartsgame.entity.GameHistory;
import dartsgame.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<GameHistory> findAllByGameId(Long gameId) {
        return Optional.of(historyRepository.findAllByGameIdOrderByMoveAsc(gameId))
                .orElse(new ArrayList<>());
    }

    public void addHistory(GameHistory gameHistory) {
        historyRepository.save(gameHistory);
    }

    public Optional<GameHistory> findByNumber(Long turn, Long id) {
        return historyRepository.findByMoveAndGameId(turn, id);
    }

    public Long findLastTurn(Long gameId) {
        Long number = 0L;
        List<GameHistory> games = Optional.of(historyRepository.findAllByGameIdOrderByMoveAsc(gameId))
                .orElse(new ArrayList<>());
        if (!games.isEmpty()) {
            return games.get(games.size() - 1).getMove();
        }
        return number;
    }

    public void delete(GameHistory ghDel) {
        historyRepository.delete(ghDel);
    }
}