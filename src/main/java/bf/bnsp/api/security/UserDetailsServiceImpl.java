package bf.bnsp.api.security;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.account.model.FonctionKeys;
import bf.bnsp.api.account.repository.AgentRepository;
import bf.bnsp.api.account.repository.FonctionRepository;
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
    private FonctionRepository fonctionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Agent> response = this.agentRepository.findByEmailAndHiddenFalse(username);
        Optional<Fonction> tmpFunction;
        Optional<EFonction> fonction = Optional.empty();
        tmpFunction = response.isPresent() ? this.fonctionRepository.findByKeys(new FonctionKeys(LocalDate.now(), response.get())) : Optional.empty();
        if(tmpFunction.isPresent()) fonction = Optional.of(tmpFunction.get().getFunction().getRule());
        Agent user = response.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        UserDetailsImpl userDetails = new UserDetailsImpl(user, fonction);
        return userDetails;
    }
}
