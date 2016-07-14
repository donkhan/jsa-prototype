import java.io.File;


public class URLRequest {

	private String url;
	private String requestBody;
	private File attachedFile;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public File getAttachedFile() {
		return attachedFile;
	}

	public void setAttachedFile(File attachedFile) {
		this.attachedFile = attachedFile;
	}


}
