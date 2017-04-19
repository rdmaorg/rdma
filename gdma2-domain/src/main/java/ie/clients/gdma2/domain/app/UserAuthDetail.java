package ie.clients.gdma2.domain.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserAuthDetail extends User {

	private ie.clients.gdma2.domain.User user;

	public UserAuthDetail(String username, String password,
			ie.clients.gdma2.domain.User user,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.user = user;
	}

	public String getId() {
		return (user != null ? (user.getId()+"") : "");
	}
	public String getUserName() {
		return (user != null ? user.getUserName() : "");
	}

	public String getFirstName() {
		return (user != null ? user.getFirstName() : "");
	}

	public String getLastName() {
		return (user != null ? user.getLastName() : "");
	}


	public boolean isEnabled() {
		return (user != null ? user.isActive() : super.isEnabled());
	}

	public boolean isLocked() {
		return (user != null ? user.isLocked() : !super.isAccountNonLocked());
	}


	public List<String> getRoles() {
		List<String> authorities = new ArrayList<String>();
		for (GrantedAuthority auth : getAuthorities()) {
			SimpleGrantedAuthority s = (SimpleGrantedAuthority) auth;
			authorities.add(s.getAuthority());
		}
		return authorities;
	}

	public boolean isPermitted(String role) {
		return getRoles().contains(role);
	}
}
