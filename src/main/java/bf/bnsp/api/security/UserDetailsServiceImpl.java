package bf.bnsp.api.security;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyTeamMember;
import bf.bnsp.api.account.repository.AgentRepository;
import bf.bnsp.api.account.repository.DailyTeamMemberRepository;
import bf.bnsp.api.tools.dataType.EFonction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private DailyTeamMemberRepository dailyTeamMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Agent> response = this.agentRepository.findByEmailAndHiddenFalse(username);
        Optional<DailyTeamMember> dailyTeamMember;
        Optional<EFonction> fonction = Optional.empty();
        dailyTeamMember = response.isPresent() ? this.dailyTeamMemberRepository.findByAgentAndDateAndHiddenFalse(response.get(), LocalDate.now()) : Optional.empty();
        if(dailyTeamMember.isPresent()) fonction = Optional.of(dailyTeamMember.get().getFonction().getRule());
        Agent user = response.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        UserDetailsImpl userDetails = new UserDetailsImpl(user, fonction);
        return userDetails;
    }
}
