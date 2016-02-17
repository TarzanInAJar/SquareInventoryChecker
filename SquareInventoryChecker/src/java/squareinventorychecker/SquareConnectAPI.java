package squareinventorychecker;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.groovy.grails.web.json.JSONArray;
import org.codehaus.groovy.grails.web.json.JSONException;
import org.codehaus.groovy.grails.web.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;


public class SquareConnectAPI {

	
	//base
	private String baseUrl = "https://connect.squareup.com";
	
	//authorization
	private String baseAuthPath = baseUrl + "/oauth2";
	private String authorizationPath = baseAuthPath + "/authorize";
	private String obtainTokenPath = baseAuthPath + "/token"; 
	private String renewTokenPath = baseAuthPath + "/clients/%s/access-token/renew"; //format in the application_id
	private String revokePath = baseAuthPath + "/revoke";
	
	
	//inventory
	private String itemsPath = baseUrl + "/v1/me/items";
	private String inventoryPath = baseUrl + "/v1/me/inventory";
	
	private String merchantPath = baseUrl + "/v1/me";
	private String authToken;
	private Date tokenExpirationDate;
	private Date lastRenewed;
	
	private String application_id;
	private String application_secret;
	
	private boolean enabled = false;
	private String lastError;
	
	public SquareConnectAPI(String application_id, String application_secret){
		this(application_id, application_secret, null, null);
	}
	
	public SquareConnectAPI(String application_id, String application_secret, String authToken, Date tokenExpirationDate){
		this.authToken = authToken;
		this.application_id = application_id;
		this.application_secret = application_secret;
		if (authToken != null){
			Date today = new Date();
			if (tokenExpirationDate.before(today)){
				System.out.println("automatically renewing authorization");
				renewAuth();
			}
		} else {
			enabled = false;
		}
	}
	
	public JSONArray getItems(){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(itemsPath);
		//add necessary headers
		request.addHeader("Authorization", "Bearer " + authToken);
		request.addHeader("Content-Type", "application/json");
		
		
		CloseableHttpResponse response = null;
		try{
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONArray responseJSON = null;
		if (response != null){
			try {
				responseJSON = new JSONArray(EntityUtils.toString(response.getEntity()));
			} catch (JSONException e){
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return responseJSON;
	}
	
	public List<InventoryEntry> getInventoryEntries(){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(inventoryPath);
		//add necessary headers
		request.addHeader("Authorization", "Bearer " + authToken);
		request.addHeader("Content-Type", "application/json");
		
		CloseableHttpResponse response = null;
		try{
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONArray responseJSON = null;
		if (response != null){
			try {
				responseJSON = new JSONArray(EntityUtils.toString(response.getEntity()));
			} catch (JSONException e){
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		List<InventoryEntry> entries = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			entries = mapper.readValue(responseJSON.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class,  
					   InventoryEntry.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return entries;
	}
	
	public String getApplicationID(){
		return this.application_id;
	}
	
	public String getToken(){
		return this.authToken;
	}
	
	public Date getLastRenewed(){
		return this.lastRenewed;
	}
	
	public String getApplicationSecret(){
		return this.application_secret;
	}
	
	public String retrieveNewToken(String code){
		if (StringUtils.isWhitespace(code)){
			//can't do anything;
			return null;
		}
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost request = new HttpPost(obtainTokenPath);
		//add necessary headers
		request.addHeader("Content-Type", "application/json");
		//create JSON request
		JSONObject requestParams = new JSONObject();
		requestParams.put("client_id", application_id);
		requestParams.put("client_secret", application_secret);
		requestParams.put("code", code);
		String requestparamsString = requestParams.toString();
		request.setEntity(new StringEntity(requestparamsString, "UTF8"));

		CloseableHttpResponse response = null;
		try{
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (response != null){
			try {
				JSONObject responseJSON = new JSONObject(EntityUtils.toString(response.getEntity()));
				String newToken = responseJSON.getString("access_token");
				this.authToken = newToken;
				String expDate = responseJSON.getString("expires_at");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); //ISO 8601 "2016-03-11T05:14:12Z"
				this.tokenExpirationDate = df.parse(expDate.replaceAll("Z$", "+0000")); //fix UTC designator so java is happy
				this.lastRenewed = new Date();
				setEnabled();
			} catch (JSONException e){
				setDisabled("Failure getting access token from retrieve new token response!");
			} catch (Exception e) {
				setDisabled("Error processing retrieveNewToken response: " + e.getMessage());
			}  finally {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return this.authToken;
	}
	
	public void renewAuth(){
		if (authToken == null){
			//can't do anything
			return;
		}
		CloseableHttpClient client = HttpClients.createDefault();
		//setup URL
		String requestUrl = String.format(renewTokenPath, this.application_id);
		HttpPost request = new HttpPost(requestUrl);
		//add necessary headers
		request.addHeader("Authorization", "Client " + this.application_secret);
		request.addHeader("Content-Type", "application/json");
		//create JSON request
		JSONObject requestParams = new JSONObject();
		requestParams.put("access_token", authToken);
		String requestparamsString = requestParams.toString();
		request.setEntity(new StringEntity(requestparamsString, "UTF8"));
		
		CloseableHttpResponse response = null;
		try{
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if (response != null){
			try {
				JSONObject responseJSON = new JSONObject(EntityUtils.toString(response.getEntity()));
				String newToken = responseJSON.getString("access_token");
				this.authToken = newToken;
				String expDate = responseJSON.getString("expires_at");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); //ISO 8601 "2016-03-11T05:14:12Z"
				this.tokenExpirationDate = df.parse(expDate.replaceAll("Z$", "+0000")); //fix UTC designator so java is happy
				this.lastRenewed = new Date();
				setEnabled();
			} catch (JSONException e){
				setDisabled("Failure getting access token from renew response!");
			} catch (Exception e) {
				setDisabled("Error processing renewToken response: " + e.getMessage());
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
	}
	
	public String getMerchant(String authToken){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(merchantPath);

		// add request header
		request.addHeader("Authorization", "Bearer " + authToken);
		request.addHeader("Accept", "application/json");
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "done";
	}
	
	
	private void setEnabled(){
		this.enabled = true;
	}
	
	private void setDisabled(String error){
		System.out.println(error);
		this.enabled = false;
		this.lastError = error;
	}
}
