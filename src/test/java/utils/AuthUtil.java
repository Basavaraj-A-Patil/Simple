package utils;

import java.util.HashMap;
import java.util.Map;

import clients.GraphQLClient;
import config.ConfigManager;
import constants.Endpoints;
import constants.GraphQLQueries;
import io.restassured.response.Response;

public class AuthUtil {

	private static String accessToken;
	private static String refreshToken;
	private static String userId;

	static String baseUrl = ConfigManager.get("baseUrl");

	public static void initialize() {
		if (accessToken == null) {
			generateToken();
		}
	}

	public static void generateToken() {
		Map<String, Object> loginVars = Map.of("mobile", ConfigManager.get("mobile"), "whatsappConsent", true,
				"loginSource", "MOBILE");

		GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE, GraphQLQueries.LOGIN_MUTATION, loginVars,
				"login");

		Map<String, Object> otpVars = Map.of("mobile", ConfigManager.get("mobile"), "otp", ConfigManager.get("otp"));
		Response response = GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE,
				GraphQLQueries.VERIFY_OTP_MUTATION, otpVars, "verifyOtp");

		// System.out.println(response.asPrettyString());
		refreshToken = response.jsonPath().getString("data.verifyOtp.data.refreshToken");
		accessToken = response.jsonPath().getString("data.verifyOtp.data.accessToken");
		userId = response.jsonPath().getString("data.verifyOtp.data.userId");

	}

	// Refresh after each class
	public static void refreshToken() {
		Map<String, Object> refreshTokenVars = new HashMap<>();
		refreshTokenVars.put("refreshToken", refreshToken);
		refreshTokenVars.put("userId", userId);

		Response response = GraphQLClient.sendPreAuthRequest(baseUrl, Endpoints.USER_SERVICE,
				GraphQLQueries.REFRESH_TOKEN_MUTATION, refreshTokenVars, "refreshToken");
		boolean success = response.jsonPath().getBoolean("data.refreshToken.success");
		if (success) {
			refreshToken = response.jsonPath().getString("data.refreshToken.data.refreshToken");
			accessToken = response.jsonPath().getString("data.refreshToken.data.accessToken");
		} else {
			generateToken();
		}
	}

	public static String getAccessToken() {
		return accessToken;
	}

	public static String getUserId() {
		return userId;
	}

}