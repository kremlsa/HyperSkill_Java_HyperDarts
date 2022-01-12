package dartsgame.repository;

import dartsgame.entity.DartsGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<DartsGame, Long> {
    Optional<List<DartsGame>> findAllByPlayerOne(String playerOne);
    Optional<List<DartsGame>> findAllByPlayerTwo(String playerTwo);
    Optional<DartsGame> findByPlayerTwo(String playerTwo);
    Optional<DartsGame> findByGameId(Long id);
    Optional<DartsGame> findByPlayerOneAndGameStatus(String playerOne, String status);

    List<DartsGame> findAllByOrderByGameIdDesc();
}
