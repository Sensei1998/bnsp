package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.model.DailyTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface DailyTeamRepository extends JpaRepository<DailyTeam, Long> {

    @Query("SELECT dtm.id FROM DailyTeam dt JOIN dt.members dtm WHERE dt.id = ?1 AND dtm.agent.id = ?2 AND dtm.hidden = false")
    Optional<Integer> findActiveDailyMemberIdByDailyTeamIdAndAgentId(long dailyTeamId, int agentId);

    @Query("SELECT dtm FROM DailyTeam dt JOIN dt.members dtm WHERE dt.id = ?1 AND dtm.agent.id = ?2 AND dtm.hidden = false")
    Optional<DailyTeamMember> findActiveDailyMemberByDailyTeamIdAndAgentId(long dailyTeamId, int agentId);

    @Transactional
    Optional<DailyTeam> findByIdAndHiddenFalse(long id);
}
