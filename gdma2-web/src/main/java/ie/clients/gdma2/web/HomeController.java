package ie.clients.gdma2.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"home"})
public class HomeController {

	@RequestMapping({"","/"})
	public String getHome(ModelMap modelMap){
		return "home";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String getHomePage(ModelMap modelMap, @RequestParam("tableDetailsArray[]") String[] tableDetailsArray){
		
		modelMap.addAttribute("tableId", tableDetailsArray[0]);
		modelMap.addAttribute("serverId", tableDetailsArray[1]);
		modelMap.addAttribute("tableName", tableDetailsArray[2]);
		modelMap.addAttribute("serverName", tableDetailsArray[3]);
		return "home";
	}
	
}
