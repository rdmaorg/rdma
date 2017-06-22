package ie.clients.gdma2.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"home"})
public class HomeController {

	@RequestMapping({"","/"})
	public String getHome(ModelMap modelMap){
		return "home";
	}
	
	@RequestMapping({"/{tableId}/{serverId}/{tableName}/{serverName}"})
	public String getHomePage(ModelMap modelMap, @PathVariable("tableId") Integer tableId, 
			@PathVariable("serverId") Integer serverId, 
			@PathVariable("tableName") String tableName,
			@PathVariable("serverName") String serverName){
		modelMap.addAttribute("tableId", tableId);
		modelMap.addAttribute("serverId", serverId);
		modelMap.addAttribute("tableName", tableName);
		modelMap.addAttribute("serverName", serverName);
		return "home";
	}
	
}
