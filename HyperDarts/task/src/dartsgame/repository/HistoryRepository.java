package dartsgame.repository;

import dartsgame.entity.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<GameHistory, Long> {
    List<GameHistory> findAllByGameIdOrderByMoveAsc(Long gameId);
    Optional<GameHistory> findByMoveAndGameId(Long number, Long gameId);
}
