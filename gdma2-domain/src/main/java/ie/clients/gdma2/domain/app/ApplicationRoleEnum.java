package ie.clients.gdma2.domain.app;

public enum ApplicationRoleEnum {

	APPLICATION("GDMA"), 
	APPLICATION_ADMIN("GDMA_ADMIN"), 
	APPLICATION_ADMIN_SERVER("GDMA_ADMIN_SERVER"), 
	APPLICATION_ADMIN_USER("GDMA_ADMIN_USER"), 
	APPLICATION_ADMIN_CONNECTION("GDMA_ADMIN_CONNECTION");

	private String role;

	private ApplicationRoleEnum(String role) {
		this.role = role;
	}

	public String role() {
		return this.role;
	}

}
