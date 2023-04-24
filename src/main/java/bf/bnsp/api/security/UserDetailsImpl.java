package bf.bnsp.api.security;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.tools.dataType.EFonction;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Agent user;

    private Optional<EFonction> fonctionFromDailyProgram;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(fonctionFromDailyProgram.isPresent()) authorities.add(new SimpleGrantedAuthority(fonctionFromDailyProgram.get().name()));
        else authorities.add(new SimpleGrantedAuthority(this.user.getDefaultFonction().getRule().name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
