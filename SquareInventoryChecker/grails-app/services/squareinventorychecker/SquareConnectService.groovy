package squareinventorychecker

import grails.transaction.Transactional

import javax.annotation.PostConstruct
import org.codehaus.groovy.grails.web.json.JSONArray


@Transactional
class SquareConnectService {
	
	SquareConnectAPI squareApi;
	def application_id="IwpuOLAM-EcVNrDEeMfPmg";
	def token = "gIR8WBpCf3x5Ph1mjDTLzg";
	def application_secret = "k5PnMXh2CiFpXutTbI1dARowj1PvKF-wCBj922vkyno";
	def expiration_date = new Date(2020,12,1);
	
	@PostConstruct
	def init() {
	  println "Initializing service"
	  squareApi = new SquareConnectAPI(application_id, application_secret, token, expiration_date);
	}
	
	public rewnewToken(){
		return squareApi.renewAuth();
	}
	
	public retrieveNewToken(String code){
		return squareApi.retrieveNewToken(code);
	}
	
	def getMerchant(){
		return squareApi.getMerchant();
	}
	
	String getApplicationID(){
		return squareApi.getApplicationID();
	}
	
	String getToken(){
		return squareApi.getToken();
	}
	
	Date getLastRenewed(){
		return squareApi.getLastRenewed();
	}
	
	InventoryEntry[] getInventoryEntries(){
		return squareApi.getInventoryEntries();
	}
	
	JSONArray getItems(){
		return squareApi.getItems();
	}
}
