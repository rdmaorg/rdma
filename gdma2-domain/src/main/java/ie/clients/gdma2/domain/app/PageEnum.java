package ie.clients.gdma2.domain.app;


public enum PageEnum {

	HOME("/", "Home", ApplicationRoleEnum.APPLICATION.role(), "Main Page", ""),
	ADMIN_SERVER("/server", "Server", ApplicationRoleEnum.APPLICATION_ADMIN_SERVER.role(), "Insert/Edit/Remove Servers", "fa-server"),
	ADMIN_USER("/user", "User", ApplicationRoleEnum.APPLICATION_ADMIN_USER.role(), "Insert/Edit/Remove User Permissions", "fa-user-circle-o"),
	ADMIN_CONNECTIONS("/connections", "Connections", ApplicationRoleEnum.APPLICATION_ADMIN_CONNECTION.role(), "Insert/Edit/Remove Connections", "fa-plug"),
	ADMIN_TABLE("/tables", "Tables", ApplicationRoleEnum.APPLICATION_ADMIN_TABLE.role(), "Insert/Edit/Remove Tables", ""),
	ADMIN_COLUMNS("/columns", "Columns", ApplicationRoleEnum.APPLICATION_ADMIN_COLUMNS.role(), "Insert/Edit/Remove/Configure Columns", ""),
	ERROR("/public/error", "Error", "", "", ""),
	LOGOUT("/logout", "Logout", "", "", ""), 
	CAS_LOGIN("/login/cas", "Login", "", "", ""), 
	CAS_LOGOUT("/logout/cas", "Logout", "", "", "");

	private String path;
	private String description;
	private String role;
	private String details;
	private String iconClass;

	private PageEnum(String path, String description, String role, String details, String iconClass) {
		this.path = path;
		this.description = description;
		this.role = role;
		this.details=details;
		this.iconClass=iconClass;
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
	
	public String details() {
		return details;
	}
	
	public String iconClass() {
		return iconClass;
	}
	
}
