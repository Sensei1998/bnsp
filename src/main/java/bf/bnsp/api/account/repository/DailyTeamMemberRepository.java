package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyTeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

public interface DailyTeamMemberRepository extends JpaRepository<DailyTeamMember, Long> {

    @Transactional
    Optional<DailyTeamMember> findByIdAndHiddenFalse(long id);

    @Transactional
    Optional<DailyTeamMember> findByAgentAndDateAndHiddenFalse(Agent agent, LocalDate date);
}
