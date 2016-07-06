
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
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;


public abstract class JSAHttpModule {

	protected HttpResponse establishConnection(HttpRequestBase request) throws Exception{
		final HttpClientContext context = HttpClientContext.create();
    	HttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    	appendMandatoryHeaders(request);
    	appendAuthenticationHeader(request);
    	HttpResponse response = client.execute(request, context);
    	return response;
	}
	
	protected HttpGet formGetRequest(){
		HttpGet request = new HttpGet("https://10.207.97.77" + getURI());
    	return request;
	}
	
	protected HttpPost formPostRequest(){
		HttpPost request = new HttpPost("https://10.207.97.77" + getURI());
    	return request;
	}
	
	protected HttpDelete formDeleteRequest(){
		HttpDelete request = new HttpDelete("https://10.207.97.77" + getURI());
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
		return "admin";
	}

	private String getPassword() {
		return "juniper";
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
	
	public JSAHttpModule() {
        super();
    }
	
	protected abstract String getURI();
    
}
