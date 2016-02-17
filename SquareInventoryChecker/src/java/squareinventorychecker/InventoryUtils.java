package squareinventorychecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryUtils {
	
	public static enum ShirtSize{
		SMALL,
		MEDIUM,
		LARGE,
		XLARGE,
		XXLARGE,
		XXXLARGE,
		XXXXLARGE,
		XXXXXLARGE,
		WSMALL,
		WMEDIUM,
		WLARGE,
		WXLARGE,
		WXXLARGE
	}
	
	
	public static Inventory getInventory(List<InventoryEntry> inventoryEntries, Map<ShirtSize, String> typeToIDMap){
		Map<ShirtSize, Integer> shirtCounts = inventoryToShirtCounts(inventoryEntries, typeToIDMap);
		Inventory inventory = new Inventory();
		
		inventory.setSmall(shirtCounts.get(ShirtSize.SMALL));
		inventory.setMedium(shirtCounts.get(ShirtSize.MEDIUM));
		inventory.setLarge(shirtCounts.get(ShirtSize.LARGE));
		inventory.setXlarge(shirtCounts.get(ShirtSize.XLARGE));
		inventory.setXxlarge(shirtCounts.get(ShirtSize.XXLARGE));
		inventory.setXxxlarge(shirtCounts.get(ShirtSize.XXXLARGE));
		inventory.setXxxxlarge(shirtCounts.get(ShirtSize.XXXXLARGE));
		inventory.setXxxxxlarge(shirtCounts.get(ShirtSize.XXXXXLARGE));

		inventory.setWsmall(shirtCounts.get(ShirtSize.WSMALL));
		inventory.setWmedium(shirtCounts.get(ShirtSize.WMEDIUM));
		inventory.setWlarge(shirtCounts.get(ShirtSize.WLARGE));
		inventory.setWxlarge(shirtCounts.get(ShirtSize.WXLARGE));
		inventory.setWxxlarge(shirtCounts.get(ShirtSize.WXXLARGE));

		return inventory;
	}
	
	private static Map<ShirtSize, Integer> inventoryToShirtCounts(List<InventoryEntry> inventoryEntries, Map<ShirtSize, String> typeToIDMap){
		Map<ShirtSize, Integer> shirtCounts = new HashMap<ShirtSize, Integer>(ShirtSize.values().length);
		Map<String, Integer> countMap = inventoryEntriesToCountMap(inventoryEntries);
		
		shirtCounts.put(ShirtSize.SMALL, countMap.get(typeToIDMap.get(ShirtSize.SMALL)));
		shirtCounts.put(ShirtSize.MEDIUM, countMap.get(typeToIDMap.get(ShirtSize.MEDIUM)));
		shirtCounts.put(ShirtSize.LARGE, countMap.get(typeToIDMap.get(ShirtSize.LARGE)));
		shirtCounts.put(ShirtSize.XLARGE, countMap.get(typeToIDMap.get(ShirtSize.XLARGE)));
		shirtCounts.put(ShirtSize.XXLARGE, countMap.get(typeToIDMap.get(ShirtSize.XXLARGE)));
		shirtCounts.put(ShirtSize.XXXLARGE, countMap.get(typeToIDMap.get(ShirtSize.XXXLARGE)));
		shirtCounts.put(ShirtSize.XXXXLARGE, countMap.get(typeToIDMap.get(ShirtSize.XXXXLARGE)));
		shirtCounts.put(ShirtSize.XXXXXLARGE, countMap.get(typeToIDMap.get(ShirtSize.XXXXXLARGE)));
		
		shirtCounts.put(ShirtSize.WSMALL, countMap.get(typeToIDMap.get(ShirtSize.WSMALL)));
		shirtCounts.put(ShirtSize.WMEDIUM, countMap.get(typeToIDMap.get(ShirtSize.WMEDIUM)));
		shirtCounts.put(ShirtSize.WLARGE, countMap.get(typeToIDMap.get(ShirtSize.WLARGE)));
		shirtCounts.put(ShirtSize.WXLARGE, countMap.get(typeToIDMap.get(ShirtSize.WXLARGE)));
		shirtCounts.put(ShirtSize.WXXLARGE, countMap.get(typeToIDMap.get(ShirtSize.WXXLARGE)));


		return shirtCounts;
	}
	
	
	private static Map<String, Integer> inventoryEntriesToCountMap(List<InventoryEntry> inventoryEntries){
		Map<String, Integer> countMap = new HashMap<String,Integer>(inventoryEntries.size());
		
		for (InventoryEntry entry : inventoryEntries){
			countMap.put(entry.variation_id, entry.quantity_on_hand);
		}
		
		return countMap;
	}
}
