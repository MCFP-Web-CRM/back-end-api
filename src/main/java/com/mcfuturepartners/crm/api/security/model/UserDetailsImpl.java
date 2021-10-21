package com.mcfuturepartners.crm.api.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final long actorId;
    //changeable accordance to company policy
    private final String username;
    //거의 안바뀌는 값, UserDetails (Spring)이 관리 한다.
    //무조건 토큰 값으로 판단하기 위해 Json으로 들어온 패스워드 값 무시
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities);
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
