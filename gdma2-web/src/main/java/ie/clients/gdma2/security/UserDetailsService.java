package ie.clients.gdma2.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.app.ApplicationRoleEnum;
import ie.clients.gdma2.domain.app.UserAuthDetail;
import ie.clients.gdma2.spi.ServiceFacade;

@Service
public class UserDetailsService implements AuthenticationUserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private ServiceFacade serviceFacade;

	@Override
	public UserDetails loadUserDetails(Authentication authentication)
			throws UsernameNotFoundException {

		if (serviceFacade == null) {
			logger.error("ServiceFacade is not autowired");
		}

		String userName = (String) authentication.getPrincipal();
		logger.info("+++++++++++++++++== USER: " + userName);

		List<GrantedAuthority> authorities = new ArrayList<>();
		User user = null;

		// Call ms-auth to fetch authorities
		// call ms-auth to set user details into u
		if (serviceFacade != null) {
			List<User> users=
			serviceFacade.getMetadataService().findByUserNameIgnoreCase(userName);
			//Assume single user returned
			if(users!=null && !users.isEmpty()){
				user = users.get(0);
				if(user!=null){
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION.role()));
				}
				if(user!=null && user.isAdmin()){
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION_ADMIN.role()));
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION_ADMIN_USER.role()));
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION_ADMIN_CONNECTION.role()));
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION_ADMIN_SERVER.role()));
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION_ADMIN_TABLE.role()));
					authorities.add(new SimpleGrantedAuthority(ApplicationRoleEnum.APPLICATION_ADMIN_COLUMNS.role()));
				}
				logger.info(user.getUserName() + " has Logged In.");
			}

		}

		UserAuthDetail u = new UserAuthDetail(""
				+ authentication.getPrincipal(), "", user, authorities);

		return u;
	}
}
