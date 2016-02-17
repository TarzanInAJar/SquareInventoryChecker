package squareinventorychecker

import java.util.Map;

import org.codehaus.groovy.grails.web.json.JSONArray
import squareinventorychecker.InventoryUtils.ShirtSize

class HomeController {

	def squareConnectService;
	def grailsApplication;
	
	
	Map<ShirtSize, Integer> shirtSizeIds;
	
	def init(){
		shirtSizeIds = getShirtSizeIDs();
	}

	
    def index() {
	}
	
	def tshirts() {
		List<InventoryEntry> inventoryEntries = squareConnectService.getInventoryEntries();
		Inventory inventory = InventoryUtils.getInventory(inventoryEntries, shirtSizeIDs);
		Date lastRefreshed = new Date();
		[inventory:inventory, lastRefreshed:lastRefreshed]
	}
	
	def items() {
		
		JSONArray items = squareConnectService.getItems();
		render(contentType: "application/json") {
			items;
		}
	}
	
	
	
	
	private Map<ShirtSize, String> getShirtSizeIDs(){
		Map<ShirtSize, String> shirtSizeIDs = new HashMap<ShirtSize, String>(ShirtSize.values().length);
		
		shirtSizeIDs.put(ShirtSize.SMALL, grailsApplication.config.shirt.inventory.small.id);
		shirtSizeIDs.put(ShirtSize.MEDIUM, grailsApplication.config.shirt.inventory.medium.id);
		shirtSizeIDs.put(ShirtSize.LARGE, grailsApplication.config.shirt.inventory.large.id);
		shirtSizeIDs.put(ShirtSize.XLARGE, grailsApplication.config.shirt.inventory.xlarge.id);
		shirtSizeIDs.put(ShirtSize.XXLARGE, grailsApplication.config.shirt.inventory.xxlarge.id);
		shirtSizeIDs.put(ShirtSize.XXXLARGE, grailsApplication.config.shirt.inventory.xxxlarge.id);
		shirtSizeIDs.put(ShirtSize.XXXXLARGE, grailsApplication.config.shirt.inventory.xxxxlarge.id);
		shirtSizeIDs.put(ShirtSize.XXXXXLARGE, grailsApplication.config.shirt.inventory.xxxxxlarge.id);
		
		shirtSizeIDs.put(ShirtSize.WSMALL, grailsApplication.config.shirt.inventory.wsmall.id);
		shirtSizeIDs.put(ShirtSize.WMEDIUM, grailsApplication.config.shirt.inventory.wmedium.id);
		shirtSizeIDs.put(ShirtSize.WLARGE, grailsApplication.config.shirt.inventory.wlarge.id);
		shirtSizeIDs.put(ShirtSize.WXLARGE, grailsApplication.config.shirt.inventory.wxlarge.id);
		shirtSizeIDs.put(ShirtSize.WXXLARGE, grailsApplication.config.shirt.inventory.wxxlarge.id);
		
		return shirtSizeIDs;
	}
}
