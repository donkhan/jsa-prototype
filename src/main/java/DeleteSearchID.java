

public class DeleteSearchID extends JSAHttpModule{
	
	private String searchID;
	
	
	public String getSearchID() {
		return searchID;
	}

	public void setSearchID(String searchID) {
		this.searchID = searchID;
	}

	@Override
	protected String getURI() {
		return "/api/ariel/searches/" + getSearchID();
	}

	public void delete() throws Exception{
		establishConnection(formDeleteRequest());
	}
}
