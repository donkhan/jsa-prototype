
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;


public abstract class JSAHttpModule {

	private String ip,userName,passWord;
	
		
	protected HttpResponse establishConnection(HttpRequestBase request) throws Exception{
		
		final HttpClientContext context = HttpClientContext.create();
		HttpClient client = new LogCollectorRequestExecutor().getClient();
    	appendMandatoryHeaders(request);
    	appendAuthenticationHeader(request);
    	HttpResponse response = client.execute(request, context);
    	return response;
	}
	
	protected HttpGet formGetRequest(){
		System.out.println(getIp());
		HttpGet request = new HttpGet("https://" + getIp() + getURI());
    	return request;
	}
	
	protected HttpPost formPostRequest(){
		
		HttpPost request = new HttpPost("https://" + getIp() +  getURI());
    	return request;
	}
	
	protected HttpDelete formDeleteRequest(){
		HttpDelete request = new HttpDelete("https://" + getIp() + getURI());
    	return request;
	}
	
	private void appendMandatoryHeaders(HttpRequestBase request){
		request.setHeader("Version","3.0");
    	request.setHeader("Accept","application/json");
	}
	
	private void appendAuthenticationHeader(HttpRequestBase request){
		String auth = getUserName() + ":" + getPassword();
    	byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ascii")));
    	String authHeader = "Basic " + new String(encodedAuth);
    	request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
	}
	
	private String getUserName() {
		return userName;
	}

	private String getPassword() {
		return passWord;
	}
	
	private String getIp(){
		return ip;
	}
	
	protected void printResponse(HttpResponse response) throws Exception{
		InputStream in = response.getEntity().getContent();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	String s = "";
    	PrintWriter pw = new PrintWriter("output.log");
    	while((s = reader.readLine()) != null){
    		System.out.println(s);
    		pw.println(s);
    	}
    	in.close();
    	pw.close();
	}
	
	public JSAHttpModule(String ip,String userName,String passWord) {
        super();
        this.ip = ip; this.userName = userName; this.passWord = passWord;
    }
	
	protected abstract String getURI();
    
}
