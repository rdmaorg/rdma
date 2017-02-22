package ie.clients.gdma2.domain.app;

public enum ApplicationRoleEnum {

	APPLICATION("GDMA"), APPLICATION_ADMIN("GDMA_ADMIN"), APPLICATION_ADMIN_SERVER("GDMA_ADMIN_SERVER");

	private String role;

	private ApplicationRoleEnum(String role) {
		this.role = role;
	}

	public String role() {
		return this.role;
	}

}
