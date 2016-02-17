package squareinventorychecker;

public class InventoryEntry {

	String variation_id;
	int quantity_on_hand;
	
	
	public String getVariation_id() {
		return variation_id;
	}
	public void setVariation_id(String variation_id) {
		this.variation_id = variation_id;
	}
	public int getQuantity_on_hand() {
		return quantity_on_hand;
	}
	public void setQuantity_on_hand(int quantity_on_hand) {
		this.quantity_on_hand = quantity_on_hand;
	}
}
