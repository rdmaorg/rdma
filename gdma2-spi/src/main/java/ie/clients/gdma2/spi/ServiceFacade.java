package ie.clients.gdma2.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ie.clients.gdma2.spi.interfaces.MetaDataService;

@Component
public class ServiceFacade {

	@Autowired
	private MetaDataService metadataService;

	public MetaDataService getMetadataService() {
		return metadataService;
	}

	public void setMetadataService(MetaDataService metadataService) {
		this.metadataService = metadataService;
	}
	
}
