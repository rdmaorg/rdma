package ie.clients.gdma2.spi;

import ie.clients.gdma2.spi.interfaces.DataModuleService;
import ie.clients.gdma2.spi.interfaces.MetaDataService;
import ie.clients.gdma2.spi.interfaces.UserContextProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceFacade {

	@Autowired
	private MetaDataService metadataService;

	@Autowired
	private DataModuleService dataModuleService;
	
	@Autowired
	private UserContextProvider userContextProvider;
	
	public MetaDataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(MetaDataService metadataService) {
		this.metadataService = metadataService;
	}

	public DataModuleService getDataModuleService() {
		return dataModuleService;
	}

	public void setDataModuleService(DataModuleService dataModuleService) {
		this.dataModuleService = dataModuleService;
	}

	public UserContextProvider getUserContextProvider() {
		return userContextProvider;
	}

	public void setUserContextProvider(UserContextProvider userContextProvider) {
		this.userContextProvider = userContextProvider;
	}
	
	
	
	
}
