import java.util.List;


public class Main {
	
	public static void main(String args[]) throws Exception{
		System.out.println("Going to Demonstrate Search Work Flow ");
		GetDatabases gd = new GetDatabases();
		List<String> dbs = gd.fetchList();
		System.out.println(dbs);
		PostSearch ps = new PostSearch();
		if(args.length == 0){
			ps.setQueryParam("select count(*) from events");
		}else{
			ps.setQueryParam(args[0]);
		}
		String searchId = ps.getSearchId();
		System.out.println("Got Search Id as " + searchId);
		System.out.println("Testing the status of search Id " + searchId);
		SearchStatus ss = new SearchStatus();
		ss.setSearchID(searchId);
		boolean status = ss.isCompleted();
		if(status){
			System.out.println("Search Id is processed");
			SearchResult sr = new SearchResult();
			sr.setSearchID(searchId);
			System.out.println("Fetch Results for " + searchId);
			sr.printResults();
		}else{
			System.out.println("Search Failed");
		}
		SearchList sl = new SearchList();
		List<String> list = sl.fetchList();
		if(list.contains(searchId)){
			System.out.println("Fired Search Query is still in the Queue");
		}
		DeleteSearchID ds = new DeleteSearchID();
		ds.setSearchID(searchId);
		ds.delete();
		System.out.println("Delete Request Fired");
		list = sl.fetchList();
		if(!list.contains(searchId)){
			System.out.println("Search Id is deleted in the Queue");
		}
	}
}
