package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.app.UserAuthDetail;

public interface UserContextProvider {

	public String getLoggedInUserName();

	public UserAuthDetail getLoggedInUser();

}
