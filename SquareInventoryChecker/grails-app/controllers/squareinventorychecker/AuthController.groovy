package squareinventorychecker

class AuthController {
	
	def squareConnectService

    def index() {
		def application_id = squareConnectService.getApplicationID();
		def token = squareConnectService.getToken();
		def lastRenewed = squareConnectService.getLastRenewed();
		
		[application_id:application_id, token:token, lastRenewed:lastRenewed.toString()]
	}
	
	def authorize(){
		String client_id = squareConnectService.getApplicationID();

		//Get a code
		response.setHeader("client_id", client_id)
		redirect(url: "https://connect.squareup.com/oauth2/authorize?client_id=" + client_id)
	}
	
	def renewToken(){
		squareConnectService.rewnewToken();
		redirect(action: "index")
	}
	
	def finishAuth(){
		[code:params.code]
	}
	
	def obtainToken(){
		String newToken = squareConnectService.retrieveNewToken(params.code);
		[newToken:newToken]
	}
}
