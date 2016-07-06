import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


public class PostSearch extends JSAHttpModule{

	private String queryParam;
	
	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

	public static void main(String[] args) throws Exception {
		PostSearch bae = new PostSearch();
		bae.setQueryParam("select count(*) from events");
		System.out.println(bae.getSearchId());
	}
	
	public String getSearchId() throws Exception {
		HttpPost request = formPostRequest();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		System.out.println("Going to set Query Param as " + queryParam);
		nameValuePairs.add(new BasicNameValuePair("query_expression", queryParam));  
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = establishConnection(request);
		
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createParser(response.getEntity().getContent());
		while(parser.nextToken() != JsonToken.END_OBJECT){
			if(parser.getText().equals("search_id")){
				parser.nextToken();
				return parser.getText();
			}
			
		}
		return "";
	}

	@Override
	protected String getURI() {
		return "/api/ariel/searches";
	}
	

}
