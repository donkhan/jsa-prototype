import org.apache.http.HttpResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


public class SearchStatus extends JSAHttpModule{
	
	public static void main(String[] args) throws Exception {
    	String sampleId = "46208635-4929-4897-9b8c-f08ff4ad98ee";
    	SearchStatus id = new SearchStatus();
    	id.setSearchID(sampleId);
    	System.out.println(id.isCompleted());
    	
    }
	private String searchID;
	
	
	public String getSearchID() {
		return searchID;
	}

	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}

	public boolean isCompleted() throws Exception{
		int i = 0;
		while(i < 10){
			System.out.println("Checking the status " + (i+1) + " time");
			HttpResponse response = establishConnection(formGetRequest());
			
			JsonFactory factory = new JsonFactory();
			JsonParser parser = factory.createParser(response.getEntity().getContent());
			while(parser.nextToken() != JsonToken.END_OBJECT){
				if(parser.getText().equals("status")){
					parser.nextToken();
					String status = parser.getText();
					if(status.equals("COMPLETED")){
						return true;
					}else{
						break;
					}
				}
			}
			i++;
		}
		return false;
	}

	@Override
	protected String getURI() {
		return "/api/ariel/searches/" + getSearchID();
	}
}
