package base;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import clients.GraphQLClient;
import config.ConfigManager;
import constants.Endpoints;
import constants.GraphQLQueries;
import io.restassured.response.Response;
import utils.AuthUtil;

public class BaseTest {

	@BeforeSuite
	public void testTokenGeneration() {

		String accessToken = AuthUtil.getAccessToken();
	//	System.out.println("TOKEN: " + accessToken);
		Assert.assertNotNull(accessToken);
	}

	@BeforeClass
	public static void refreshAccessToken() {
		String accessToken = AuthUtil.getAccessToken();
		String refreshToken = AuthUtil.getRefreshToken();

        String baseUrl = ConfigManager.get("baseUrl");

		Map<String, Object> refreshVars = new HashMap<>();
		refreshVars.put("refreshToken", refreshToken);
		//refreshVars.put("userId", userId);

		Response response = GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE + "/refresh-token",
				GraphQLQueries.REFRESH_TOKEN_MUTATION, refreshVars, "refreshToken");

		accessToken = response.jsonPath().getString("data.refreshToken.data.accessToken");
		refreshToken = response.jsonPath().getString("data.refreshToken.data.refreshToken");

		System.out.println("🔄 Token Refreshed");
	}
}