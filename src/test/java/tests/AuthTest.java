package tests;

import java.util.Map;
import java.util.HashMap;


import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import clients.GraphQLClient;
import config.ConfigManager;
import constants.Endpoints;
import constants.GraphQLQueries;
import io.restassured.response.Response;
import listeners.TestListener;
import utils.AssertUtils;

@Listeners(TestListener.class)
public class AuthTest {
	static String baseUrl = ConfigManager.get("baseUrl");
	static String refreshToken = null;
	static String accessToken = null;
	static String userId = null;

	@Test(priority = 1)
	public static void login() {
		
		Map<String, Object> loginVars = Map.of("mobile", ConfigManager.get("mobile"), "whatsappConsent", true,
				"loginSource", "MOBILE");

		Response response = GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE,
				GraphQLQueries.LOGIN_MUTATION, loginVars, "login");
		
		AssertUtils.verifyStatusCode(response, 200);
		Boolean success = response.jsonPath().getBoolean("data.login.success");
		AssertUtils.verifyTrue(success, "Login failed");
		AssertUtils.verifyNoGraphQLErrors(response);
		
//		System.out.println(response.asPrettyString());
	}

	@Test(priority = 2)
	public static void verifyOtp() {
		
		Map<String, Object> otpVars = Map.of("mobile", ConfigManager.get("mobile"), "otp", ConfigManager.get("otp"));
		Response response = GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE,
				GraphQLQueries.VERIFY_OTP_MUTATION, otpVars, "verifyOtp");
		
		//System.out.println(response.asPrettyString());
		refreshToken = response.jsonPath().getString("data.verifyOtp.data.refreshToken");
		accessToken = response.jsonPath().getString("data.verifyOtp.data.accessToken");
		userId = response.jsonPath().getString("data.verifyOtp.data.userId");
		
		AssertUtils.verifyStatusCode(response, 200);
		AssertUtils.verifyNotNull(accessToken, "Access token is null");
		AssertUtils.verifyNotNull(refreshToken, "Refresh token is null");
		AssertUtils.verifyNotNull(userId, "UserId is null");
		AssertUtils.verifyNoGraphQLErrors(response);
		

	}

	@Test(priority = 3)
	public static void refreshToken() {
	
	//	Map<String, Object> refreshTokenVars = Map.of("refreshToken", refreshToken, "userId", userId);
		
		Map<String, Object> refreshTokenVars = new HashMap<>();
		refreshTokenVars.put("refreshToken", refreshToken);
		refreshTokenVars.put("userId", userId);
		
		Response response = GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE,
				GraphQLQueries.REFRESH_TOKEN_MUTATION, refreshTokenVars, "refreshToken");
		
		AssertUtils.verifyStatusCode(response, 200);
		AssertUtils.verifyNoGraphQLErrors(response);
	//	System.out.println(response.asPrettyString());
	}

}
