package ie.clients.gdma2.domain.app;


public enum PageEnum {

	HOME("/", "Home", ApplicationRoleEnum.APPLICATION.role()),
	ADMIN_SERVER("/server", "Server", ApplicationRoleEnum.APPLICATION_ADMIN_SERVER.role()),
	ADMIN_USER("/user", "User", ApplicationRoleEnum.APPLICATION_ADMIN_SERVER.role()),
	LOGOUT("/logout", "Logout", ""), 
	CAS_LOGIN("/login/cas", "Login", ""), 
	CAS_LOGOUT("/logout/cas", "Logout", "");

	private String path;
	private String description;
	private String role;

	private PageEnum(String path, String description, String role) {
		this.path = path;
		this.description = description;
		this.role = role;
	}

	public String description() {
		return description;
	}

	public String path() {
		return path;
	}

	public String role() {
		return role;
	}
}
