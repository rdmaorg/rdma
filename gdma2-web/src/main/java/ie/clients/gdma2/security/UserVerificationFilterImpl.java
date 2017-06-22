package ie.clients.gdma2.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import ie.clients.gdma2.domain.app.ApplicationRoleEnum;
import ie.clients.gdma2.domain.app.PageEnum;
import ie.clients.gdma2.domain.app.UserAuthDetail;
import ie.clients.gdma2.spi.ServiceFacade;
import ie.clients.gdma2.spi.interfaces.UserVerificationFilter;

@Service
public class UserVerificationFilterImpl extends GenericFilterBean implements UserVerificationFilter {

	private Logger logger = LoggerFactory.getLogger(UserVerificationFilterImpl.class);

	@Value("${application.protocol}")
	private String applicationProtocol;
	@Value("${application.hostname}")
	private String applicationHostname;
	@Value("${application.contextPath}")
	private String applicationContextPath;

	@Autowired
	private ServiceFacade serviceFacade;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Sanity check, will never be called
		if (serviceFacade == null) {
			logger.error("Service facade is not autowired");
			chain.doFilter(req, resp);
			return;
		}

		logger.debug("req.contextPath: " + request.getContextPath() + ", req.RequestURI: " + request.getRequestURI()
				+ ", req.servletPath: " + request.getServletPath());

		// Bypass checks if the url refers to public content or rest services.
		if (request.getRequestURI().contains("/public/") || request.getRequestURI().contains("/rest/")) {
			chain.doFilter(req, resp);
			return;
		}

		// Bypass checks if the url refers to pages where the user should land
		// as part of the checks.
		// if
		// (ApplicationConstants.APP_USER_MANAGEMENT_PAGE_USER_PROFILE.equalsIgnoreCase(request.getServletPath())
		// ||
		// ApplicationConstants.APP_USER_MANAGEMENT_PAGE_CHANGE_PASSWORD.equalsIgnoreCase(request.getServletPath())
		// ||
		// ApplicationConstants.APP_USER_MANAGEMENT_PAGE_USER_APPLICATIONS.equalsIgnoreCase(request.getServletPath())
		// ||
		// ApplicationConstants.APP_USER_MANAGEMENT_PAGE_USER_INFO.equalsIgnoreCase(request.getServletPath()))
		// {
		// chain.doFilter(req, resp);
		// return;
		// }

		logger.debug("UserVerificationFilterImpl called");

		if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (principal instanceof UserAuthDetail) {
				UserAuthDetail u = (UserAuthDetail) principal;

				boolean appRoleAllowed = u.isPermitted(ApplicationRoleEnum.APPLICATION.role());
				logger.debug(ApplicationRoleEnum.APPLICATION.role() + ": " + appRoleAllowed);

				// TODO: Get a fall back url in place
				String url = "";

				if (u.isLocked()) {
					url = getRedirectUrl(url, "01");
					logger.info("Account is locked, redirecting to information page [" + url + "].");
					response.sendRedirect(url);
				}

				if (!u.isActive()) {
					url = getRedirectUrl(url, "02");
					logger.info("Account is not enabled, redirecting to information page [" + url + "].");
					response.sendRedirect(url);
				}

				if (!appRoleAllowed) {
					url = getRedirectUrl(url, "03");
					logger.info("Application access is not allowed, redirecting to information page [" + url + "].");
					response.sendRedirect(url);

				}

				if (!isPagePermitted(u, request.getServletPath())) {
					url = getRedirectUrl(url, "04");
					logger.info("Application Resource access is not allowed, redirecting to information page [" + url
							+ "].");
					response.sendRedirect(url);
				}
			} else {
				logger.info("Principal not of UserAuthDetail type. Type of Principal: " + principal.getClass().getName());
			}

		} else {
			logger.error("Couldn't find principal in securityContext.");
			if(SecurityContextHolder.getContext() == null){
				logger.error("Context object in SecurityContext is null");
			} else if(SecurityContextHolder.getContext().getAuthentication() == null){
				logger.error("Authentication object in Context is null");
			} else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null){
				logger.error("Principal object in Authentication is null");
			} else {
				logger.error("Something is terribly wrong!");
			}
		}

		chain.doFilter(req, resp);
	}

	private String getRedirectUrl(String url, String reasonCode) {
		if (StringUtils.isBlank(url)) {
			url = applicationBaseUrl() + PageEnum.ERROR.path();
		}

		if (url.contains("reasonCode")) {
			url += "&reasonCode=" + reasonCode;
		} else {
			url += "?reasonCode=" + reasonCode;
		}

		return url;
	}

	private String applicationBaseUrl() {
		return applicationProtocol + "://" + applicationHostname + "/" + applicationContextPath;
	}

	private boolean isPagePermitted(UserAuthDetail u, String servletPath) {
		logger.info("servletPath: " + servletPath);
		// prints /user/list
		for (PageEnum p : PageEnum.values()) {
			if (servletPath.equalsIgnoreCase(p.path()) || servletPath.toLowerCase().startsWith(p.path().concat("/"))) {
				logger.debug("looking for permission on " + servletPath);
				if (!StringUtils.isBlank(p.role())) {
					logger.debug("Permission based page, permission available: " + u.isPermitted(p.role()));
					return u.isPermitted(p.role());
				} else {
					return true;
				}
			}
		}
		return false;
	}

}
