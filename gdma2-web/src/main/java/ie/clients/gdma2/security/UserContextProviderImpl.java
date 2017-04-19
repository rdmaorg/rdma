package ie.clients.gdma2.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ie.clients.gdma2.domain.app.UserAuthDetail;
import ie.clients.gdma2.spi.interfaces.UserContextProvider;

@Service
public class UserContextProviderImpl implements UserContextProvider {

	public String getLoggedInUserName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserAuthDetail) {
			return ((UserAuthDetail) principal).getUsername();
		} else if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		return principal.toString();
	}

	@Override
	public UserAuthDetail getLoggedInUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserAuthDetail) {
			return (UserAuthDetail) principal;
		}
		return null;
	}

}
