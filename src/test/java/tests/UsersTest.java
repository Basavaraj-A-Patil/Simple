package tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.BaseTest;
import clients.GraphQLClient;
import constants.Endpoints;
import constants.GraphQLQueries;
import io.restassured.response.Response;
import listeners.TestListener;
import utils.AuthUtil;

@Listeners(TestListener.class)
public class UsersTest extends BaseTest{

	@Test
	public static void getUsersDetailsTest() {
		Map<String, Object> userDetailsVar = new HashMap<String, Object>();
		userDetailsVar.put("userId", AuthUtil.getUserId());

		Response response = GraphQLClient.sendRequest(Endpoints.USER_SERVICE, GraphQLQueries.GET_USER_DETAILS_QUERY, userDetailsVar, "getUserDetails");
	}
	@Test
	public static void getUsersDetailsTest1() {
		Map<String, Object> userDetailsVar = new HashMap<String, Object>();
		userDetailsVar.put("getUserDetailsId", AuthUtil.getUserId());
		
		Response response = GraphQLClient.sendRequest(Endpoints.USER_SERVICE, GraphQLQueries.GET_USER_DETAILS_QUERY, userDetailsVar, "getUserDetails");
		System.out.println(response);
	}
}
