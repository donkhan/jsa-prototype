

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


public class LogCollectorRequestExecutor {
  private CloseableHttpClient httpClient;

  // single copy of connection manager in the system
  private static PoolingHttpClientConnectionManager poolledConnectionManager;

  public LogCollectorRequestExecutor(){
    if (poolledConnectionManager == null) {
      poolledConnectionManager = new PoolingHttpClientConnectionManager();
      // Maximum connection limited to 30 per server port combination, can be tuned based on benchmark
      poolledConnectionManager.setDefaultMaxPerRoute(10);
      poolledConnectionManager.setMaxTotal(40);
      
    }
    httpClient = HttpClientBuilder.create().setConnectionManagerShared(true).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
  }
  
  
  
  public CloseableHttpClient getClient(){
	  return httpClient;
  }

  /**
   * extract the host from the request
   * 
   * @param request
   * @return
   * @throws ClientProtocolException
   */
  private static HttpHost determineTarget(String request) {
    HttpHost target = null;

    final URI requestURI = URI.create(request);
    if (requestURI.isAbsolute()) {
      target = URIUtils.extractHost(requestURI);
    }
    return target;
  }

  /**
   * method to execute the HTTP request, accepts the message body as URLRequest,
   * 
   * @param request
   *          -- The request to be sent as body
   * @param operation
   *          -- HTTP operation to be performed
   * @param headerType
   *          -- Content Types specification
   * @param userName
   *          -- for authentication
   * @param password
   *          -- for authentication
   * @return -- HTTP response, which the consumer need to close after use
   * @throws ECMException
   * @throws ClientProtocolException
TODO: remove the object URLRequest and use HTTPUriRequest interface, there is no need to define new object here
   */
  public CloseableHttpResponse exectureQuery(URLRequest request, HTTPOperation operation, String headerType,
      String userName, String password) {

    if (request != null && operation != null) {
      // create basic authentication scheme here, to force primitive authentication with httpClient
      AuthCache authCache = new BasicAuthCache();
      BasicScheme basicAuth = new BasicScheme();
      HttpClientContext authContext = HttpClientContext.create();
      HttpHost targetHost = determineTarget(request.getUrl());
      authCache.put(targetHost, basicAuth);

      if (userName != null && password != null) {
        // Initialize the authorization context
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
            new UsernamePasswordCredentials(userName, password));
        authContext.setCredentialsProvider(credentialsProvider);
        authContext.setAuthCache(authCache);
      }

      CloseableHttpResponse response = null;

      int statusCode = 0;
      try {
        switch (operation) {
        case POST: {
          HttpPost httpPost = new HttpPost(request.getUrl());
          StringEntity params = new StringEntity(request.getRequestBody());
          httpPost.addHeader(ServerHeaderConstant.CONTENT_TYPE, headerType);
          httpPost.addHeader(ServerHeaderConstant.ACCEPT, headerType);
          httpPost.setEntity(params);
          response = httpClient.execute(targetHost, httpPost, authContext);
          break;
        }
        case GET: {
          HttpGet httpGet = new HttpGet(request.getUrl());
          httpGet.addHeader(ServerHeaderConstant.ACCEPT, headerType);
          response = httpClient.execute(targetHost, httpGet, authContext);
          break;
        }
        case PUT: {
          HttpPut httpPut = new HttpPut(request.getUrl());
          httpPut.addHeader(ServerHeaderConstant.CONTENT_TYPE, headerType);
          httpPut.addHeader(ServerHeaderConstant.ACCEPT, headerType);
          response = httpClient.execute(targetHost, httpPut, authContext);
          break;
        }
        }
      } catch (Exception e) {
        e.printStackTrace();
        
      }
      if (response != null) {
        statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_OK) {
          return response;
        } else {
          closeHttpReponse(response);
        }
      } else {
      }
    }
    return null;
  }

  /**
   * Utility method to close httpReponse, API to close the httpResponse, can be moved to ECMUtils
   * 
   * @param httpResponse
   */
  public static void closeHttpReponse(CloseableHttpResponse httpResponse) {
    if (httpResponse != null) {
      try {
        // close this client for the thread
        httpResponse.close();
      } catch (IOException e) {
      }
    }
  }

}
