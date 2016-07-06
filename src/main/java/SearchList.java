import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


public class SearchList extends JSAHttpModule{

	public List<String> fetchList() throws Exception{
		HttpResponse response = establishConnection(formGetRequest());
		List<String> searchIdList = new ArrayList<String>();
		
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createParser(response.getEntity().getContent());
		while(parser.nextToken() != JsonToken.END_ARRAY){
			JsonToken token = parser.getCurrentToken();
			if(token == JsonToken.START_ARRAY){
				continue;
			}
			searchIdList.add(parser.getText());
		}
		return searchIdList;
	}
	
	@Override
	protected String getURI() {
		return "/api/ariel/searches";
	}
	
	public static void main(String[] args) throws Exception {
		SearchList bae = new SearchList();
    	List<String> list = bae.fetchList();
    	for(String s : list){
    		if(s.startsWith("9de")){
    			System.out.println(s);
    		}
    	}
    }
}
