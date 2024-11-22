package ICar.support;
//package org.zelle.support;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//import io.restassured.RestAssured;
//import io.restassured.config.ConnectionConfig;
//import io.restassured.config.RestAssuredConfig;
//import io.restassured.http.Method;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//
//public class RestAssuredAPI {
//
//
//	/**setBaseURL method sets baseURL
//	 * @param baseURL
//	 *              as url string
//	 */
//	public static void setBaseURL(String baseURL)
//	{
//		try
//		{
//			if(!baseURL.isEmpty()||!baseURL.contains(null))
//			{
//				RestAssured.baseURI = baseURL;
//			}}catch (NullPointerException e) {
//				Log.message("Base URL is not set : - "+e);
//			}	
//	}
//
//	/**
//	 * Returns response of GET API method execution
//	 * 
//	 * @param baseURL
//	 *              as base url
//	 * @param headers
//	 *              as Map
//	 * @param username
//	 *              as authentication username for basic/digest based/ can be left blank
//	 * @param password
//	 *              as authentication password for basic/digest based/ can be left blank
//	 * @param contentType
//	 *              as content can be of type text,json,xml
//	 * @param url
//	 *              as service url
//	 * @return Response of GET command
//	 */
//	public static Response GET(String baseURL,Map<String, String> headers,String username,String password,String contentType, String url) 
//	{
//		setBaseURL(baseURL);
//		Log.message("Sending GET Request");
//		Response resp = RestAssured.given()
//				.auth().basic(username, password)
//				.headers(headers)
//				.contentType(contentType).get(url);
//
//		Log.message("Executed URL \n" + url);
//		Log.message("Status Code \n" + resp.getStatusCode());		
//		Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//		return resp;
//	}
//	
//	/**
//	 * Returns response of GET API method execution
//	 * 
//	 * @param URL
//	 * @return Response
//	 */
//	public static Response GET(String URL) {
//		//Log.event("Sending GET request");
//		Response resp = RestAssured.given().get(URL);
////		Log.event("Executed URL " + URL);
////		Log.event("Status Code:" + resp.getStatusCode());
////		Log.event("Time taken to get response is " + resp.getTime() + " milli second");
//		return resp;
//	}
//
//	/**
//	 * Returns response of POST API method execution
//	 * 
//	 * @param baseURL
//	 *              as base url
//	 * @param headers
//	 *              as Map
//	 * @param username
//	 *              as authentication username for basic/digest based/ can be left blank
//	 * @param password
//	 *              as authentication password for basic/digest based/ can be left blank
//	 * @param contentType
//	 *              as content can be of type text,json,xml
//	 * @param body
//	 *            as content which can be sent via post request
//	 * @param url
//	 *              as service url
//	 * @return Response of POST command
//	 */
//	public static Response POST(String url,Map<String, String> headers,String contentType) {
//		Log.message("Sending POST command");
//		Response resp = RestAssured.given()
//				.headers(headers)
//				.contentType(contentType)				
//				.post(url)
//				.andReturn();
//		
//		Log.message("Executed URL \n" + url);
//		Log.message("Status Code \n" + resp.getStatusCode());
//		Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//		return resp;
//	}
//
//
//	/**
//	 * The Publisher APIs require File to be uploaded, so the given API is
//	 * specific to POST Publisher APIs
//	 * @param baseURL
//	 *              as base url
//	 * @param headers
//	 *              as Map
//	 * @param username
//	 *              as authentication username for basic/digest based/ can be left blank
//	 * @param password
//	 *              as authentication password for basic/digest based/ can be left blank
//	 * @param contentType
//	 *              as content can be of type text,json,xml
//	 * @param path
//	 *            as path of content
//	 * @param url
//	 *              as service url
//	 * @return Response of API execution
//	 * @throws Exception 
//	 */
//	public static Response POSTPublisher(String baseURL,Map<String, String> headers,String username,String password,
//			String contentType, String path, String url) throws Exception {
//		setBaseURL(baseURL);
//		Response resp = null;
//		try {
//			File file = new File(path);
//			Log.message("Sending POST Publish command");
//			resp = RestAssured.given().headers(headers)
//					.auth().basic(username, password)
//					.headers(headers)
//					.multiPart("files", file).contentType(contentType)
//					.when().post(url);
//			
//			Log.message("Executed URL \n" + url);
//			Log.message("Status Code \n" + resp.getStatusCode());
//			Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//		} catch (Exception e) {
//			Log.exception(e);
//			Log.message("something went wrong while running POSTPublisher");
//		}
//
//		return resp;
//	}
//
//
//	/**
//	 * Returns response of PUT API method execution
//	 * @param baseURL
//	 *              as base url
//	 * @param headers
//	 *              as Map
//	 * @param username
//	 *              as authentication username for basic/digest based/ can be left blank
//	 * @param password
//	 *              as authentication password for basic/digest based/ can be left blank
//	 * @param contentType
//	 *              as content can be of type text,json,xml
//	 * @param body
//	 *              as content
//	 * @param url
//	 *              as service url
//	 * @return
//	 *             as response
//	 */
//	public static Response PUT(String baseURL,Map<String, String> headers,String username,String password,
//			String contentType, String body, String url) {
//		setBaseURL(baseURL);
//		Log.message("Sending PUT command");
//		Response resp = RestAssured.given()
//				.headers(headers)
//				.auth().basic(username, password)
//				.contentType(contentType)
//				.body(body)
//				.put(url)
//				.andReturn();
//		
//		Log.message("Executed URL \n" + url);
//		Log.message("Status Code \n" + resp.getStatusCode());
//		Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//
//		return resp;
//
//	}
//
//	/**
//	 * Returns response of DELETE API method execution
//	 * @param baseURL
//	 *              as base url
//	 * @param headers
//	 *              as Map
//	 * @param username
//	 *              as authentication username for basic/digest based/ can be left blank
//	 * @param password
//	 *              as authentication password for basic/digest based/ can be left blank
//	 * @param contentType
//	 *              as content can be of type text,json,xml
//	 * @param url
//	 *              as service
//	 * @return
//	 *             as response
//	 */
//	public static Response DELETE(String baseURL,Map<String, String> headers,String username,String password,
//			String contentType, String url) {
//		setBaseURL(baseURL);
//		Log.message("Running DELETE command");
//		Response resp = RestAssured.given().headers(headers)
//				.delete(url);
//
//		Log.message("Executed URL \n" + url);
//		Log.message("Status Code \n" + resp.getStatusCode());
//		Log.event("Header \n" + resp.getHeaders().toString());
//		Log.event("Response body \n" + resp.getBody().toString());
//		Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//
//		return resp;
//
//	}
//
//	/**
//     * Returns response of DELETE API method execution
//     * @param baseURL
//     *              as base url
//     * @param headers
//     *              as Map
//     * @param username
//     *              as authentication username for basic/digest based
//     * @param password
//     *              as authentication password for basic/digest based
//     * @param contentType
//     *              as content can be of type text,json,xml
//     * @param body
//     *              as body of the request
//     * @param url
//     *              as service
//     * @return
//     *             as response
//     */
//	public static Response DELETE(String baseURL,Map<String, String> headers,String username,String password,
//            String contentType, String body, String url) {
//     setBaseURL(baseURL);
//     Log.message("Running DELETE command");
//    
//     Response resp = RestAssured.given()
//                  .auth().basic(username, password)
//                  .headers(headers)
//                  .contentType(contentType)                      
//                  .body(body)
//                  .delete(url)
//                  .andReturn();
//    
//     Log.message("Executed URL \n" + url);
//     Log.message("Status Code \n" + resp.getStatusCode());
//     Log.event("Header \n" + resp.getHeaders().toString());
//     Log.event("Response body \n" + resp.getBody().toString());
//     Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//
//     return resp;
//
//}
//	
//	/**Returns response of POST API method execution
//	 * @param baseURL
//	 *             as base url
//	 * @param headers
//	 *             as Map
//	 * @param inputData
//	 *             as form parameters
//	 * @param url
//	 *             as service
//	 * @return
//	 *             as response
//	 */
//	public static Response POST(String baseURL,Map<String, String> headers,
//			Map<String, String> inputData, String url) {
//		setBaseURL(baseURL);
//		Log.message("Running POST command");
//		Response resp = RestAssured.given().headers(headers)
//				.formParams(inputData).post(url).andReturn();
//		
//		Log.message("Executed URL \n" + url);
//		Log.message("Status Code \n" + resp.getStatusCode());
//		Log.event("Header \n" + resp.getHeaders().toString());
//		Log.event("Response body \n" + resp.getBody().toString());
//		Log.message("Time taken to get response is \n" + resp.getTime()+" milli second");
//
//		return resp;
//	}	
//
//	/**closeConnection method would be closing idle Connection
//	 * 
//	 */
//	public static void closeConnection()
//	{
//		RestAssured.config = RestAssuredConfig.newConfig().connectionConfig(ConnectionConfig.connectionConfig().closeIdleConnectionsAfterEachResponse());
//	}
//	
//	/**
//	 * To upload a file using multipart and PUT API
//	 * 
//	 * @param baseURL
//	 * @param filePath
//	 * @param headers
//	 * @param fileUploadUrl
//	 * @return Response
//	 */
//	public static Response uploadFile(String baseURL, String filePath, Map<String, String> headers, String fileUploadUrl) {
//		setBaseURL(baseURL);
//		File fileToBeUploaded = new File(filePath);
//		Response resp = null;
//		if (fileToBeUploaded.exists()) {
//			Log.message("Sending PUT request to upload file");
//			resp = RestAssured.given().multiPart(fileToBeUploaded).headers(headers).when().put(fileUploadUrl);
//			Log.message("Executed URL " + fileUploadUrl);
//			Log.message("Status Code \n" + resp.getStatusCode());
//			Log.message("Time taken to get response is " + resp.getTime() + " milli second");
//		} else {
//			Log.message("File " + filePath + " does not exist");
//		}
//		return resp;
//	}
//
//	/**
//	 * Get the jessionID and failover value for any realize user[Admin/Teacher/Student] from any environment 
//	 * 
//	 * @param baseURL base realize url of any environment � Example: https://cert-www.realizedev.com
//	 * @param rumbaURL rumba URL of the environment
//	 * @param userName can be teacher / student/ admin � Example: realize_pa_user
//	 * @return jsessionID and failover valve
//	 * @throws Exception
//	 */
//    public static String getJSessionIDAccess(String baseURL, String rumbaURL, String userName) throws Exception {
//        // Query Params and Headers - Declaration
//        Map<String, String> inputData1Login = new HashMap<String, String>();
//        Map<String, String> inputData2Auth1 = new HashMap<String, String>();
//        Map<String, String> inputData2Auth2 = new HashMap<String, String>();
//
//        Map<String, String> headers1Login = new HashMap<String, String>();
//        Map<String, String> headers1Auth1 = new HashMap<String, String>();
//        Map<String, String> headers1Auth2 = new HashMap<String, String>();
//
//        // Query Params for Login
//        inputData1Login.put("profile", "realize");
//        inputData1Login.put("k12int", "true");
//        inputData1Login.put("service", "" + baseURL + "/community/j_spring_cas_security_check");
//
//        // Query Params for Auth1
//        inputData2Auth1.put("execution", "e1s1");
//        inputData2Auth1.put("profile", "realize");
//        inputData2Auth1.put("k12int", "true");
//        inputData2Auth1.put("service", "" + baseURL + "/community/j_spring_cas_security_check");
//
//        // Making Login GET Call
//        Response respLogin = get(rumbaURL, headers1Login, inputData1Login, "/sso/login");
//        
//        // Extracting Response Cookies - JSESSION and BIGipServerrumba-int-cluster-01
//        Map<String, String> cookies = respLogin.cookies();
//        StringBuffer cookieHeader = new StringBuffer();
//        for (String cookie : cookies.keySet()) {
//            cookieHeader.append(cookie + "=" + cookies.get(cookie) + ";");
//        }
//
//        // Setting Request header cookie for Auth1 POST Call
//        headers1Auth1.put("Cookie", cookieHeader.toString());
//        headers1Auth1.put("Content-Type", "application/x-www-form-urlencoded");
//        String LoginPayload = "username=" + userName + "&password=" + "testing123$" + "&_eventId=submit";
//
//        // Making Auth1 POST Call
//        Response respAuth1 = post(rumbaURL, headers1Auth1, inputData2Auth1, LoginPayload, "/sso/login");
//        
//        // Extracting CASTGC for Auth 2 POST call
//        String castgc = respAuth1.getCookie("CASTGC");
//
//        // Extracting Location for Auth 2 POST call
//        String baseURLforAuth2 = respAuth1.getHeader("Location").split("\\?")[0].split(".com")[0] + ".com";
//        String endPointURLforAuth2 = respAuth1.getHeader("Location").split("\\?")[0].split("\\.com")[1];
//        String ticketParam = respAuth1.getHeader("Location").split("\\?")[1].split("=")[1];
//        inputData2Auth2.put("ticket", ticketParam);
//
//        // Setting Query params for Auth2 POST Call
//        headers1Auth2.put("Cookie", cookieHeader.toString() + "CASTGC=" + castgc);
//
//        // Setting Request Cookies for Auth2 POST Call
//        headers1Auth2.put("Content-Type", "application/x-www-form-urlencoded");
//
//        Response respAuth2 = post(baseURLforAuth2, headers1Auth2, inputData2Auth2, LoginPayload,
//                endPointURLforAuth2);
//        Log.message(">>>> Auth 2 " + userName + " Cookies: \nJSESSIONID="
//                + respAuth2.getCookies().get("JSESSIONID") + ";__failover=" + respAuth2.getCookies().get("__failover"));
//        return "JSESSIONID=" + respAuth2.getCookies().get("JSESSIONID") + ";__failover="
//                + respAuth2.getCookies().get("__failover");
//    }
//    
//    /**
//     * Standard GET with baseUrl, headers, query parameters, endpoint 
//     */
//    public static Response get(String baseUrl, Map<String, String> headers, Map<String, String> queryParams,
//            String endPoint) {
//        return RestAssured.given().baseUri(baseUrl).headers(headers).queryParams(queryParams).get(endPoint);
//    }
//
//    /**
//     * Standard POST with baseUrl, headers, query parameters, payLoad, endpoint 
//     */
//    public static Response post(String baseUrl, Map<String, String> headers, Map<String, String> queryParams,
//            String payload, String endPoint) {
//        return RestAssured.given().baseUri(baseUrl).headers(headers).queryParams(queryParams).body(payload)
//                .post(endPoint);
//    }
//    
//    /**
//	 * Standard PUT with baseUrl, headers, cookies, payLoad, endpoint
//	 */
//	public static Response PUT(String baseUrl, Map<String, String> headers, String payload, String endPoint,
//			Map<String, String> cookies) {
//		return RestAssured.given().baseUri(baseUrl).headers(headers).cookies(cookies).body(payload).put(endPoint);
//	}
//}