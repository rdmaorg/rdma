package ie.clients.gdma2.spi;

import ie.clients.gdma2.spi.interfaces.DataModuleService;
import ie.clients.gdma2.spi.interfaces.MetaDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceFacade {

	@Autowired
	private MetaDataService metadataService;

	@Autowired
	private DataModuleService dataModuleService;
	
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
	
	
	
	
}
