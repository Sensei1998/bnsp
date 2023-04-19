package bf.bnsp.api.tools.controleForm;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.security.JwtProperties;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenUtils {

    @Autowired
    private AgentService agentService;

    @Autowired
    private CaserneService caserneService;

    public Optional<Agent> getAgentFromToken(String token){
        token = token.substring(7, token.length());
        System.out.println(Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody().get("agentId"));
        Optional<Agent> response = this.agentService.findActiveAgentById((Integer) Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody().get("agentId"));
        return response;
    }

    public Optional<Caserne> getCaserneFromToken(String token){
        token = token.substring(7, token.length());
        System.out.println(Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody().get("caserneId"));
        Optional<Caserne> response = this.caserneService.findActiveCaserneById((Integer) Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(token).getBody().get("caserneId"));
        return response;
    }
}
