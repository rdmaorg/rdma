package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.User;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/column")
public class ColumnResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(ServerResource.class);

	@RequestMapping(value = "list")
	public List<Column> getAllColumns(){
		logger.debug("getAllColumns()");
		return serviceFacade.getMetadataService().getAllColumns();
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public List<Column> saveColumns(@RequestBody List<Column> columnList){
		logger.debug("saveColumns");
		return serviceFacade.getMetadataService().saveColumns(columnList);
		
	}
	
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public void deleteColumn(@PathVariable("id") Integer columnId){
		logger.debug("deleteColumn " + columnId);
		serviceFacade.getMetadataService().deleteColumn(columnId);
	}
}
