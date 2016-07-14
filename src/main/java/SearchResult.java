import org.apache.http.client.methods.HttpGet;


public class SearchResult extends JSAHttpModule{
	
	public SearchResult(String ip, String userName, String passWord) {
		super(ip, userName, passWord);
	}

	public static void main(String[] args) throws Exception {
    	String sampleId = "46208635-4929-4897-9b8c-f08ff4ad98ee";
    	SearchResult id = new SearchResult("10.207.99.77","admin","juniper");
    	id.setSearchID(sampleId);
    }
	private String searchID;
	
	
	public String getSearchID() {
		return searchID;
	}

	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}

	public void printResults() throws Exception{
		HttpGet request = formGetRequest();
		//request.setHeader("Range","items=1-2");
		printResponse(establishConnection(request));
	}

	@Override
	protected String getURI() {
		return "/api/ariel/searches/" + getSearchID() + "/results";
	}
}
