package ie.clients.gdma2.rest;

import org.springframework.beans.factory.annotation.Autowired;

import ie.clients.gdma2.spi.ServiceFacade;

public abstract class BaseResource {
	@Autowired
	protected ServiceFacade serviceFacade;
}
