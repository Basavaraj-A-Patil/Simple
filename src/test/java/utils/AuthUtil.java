package utils;

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
    
    public static String getAccessToken() {
        if (accessToken == null) {
            generateToken();
        }
        return accessToken;
    }
    public static String getRefreshToken() {
        if (refreshToken == null) {
            generateToken();
        }
        return refreshToken;
    }
    

    private static void generateToken() {

        // Step 1: Login API (trigger OTP)
        Map<String, Object> loginVars = Map.of(
                "mobile", ConfigManager.get("mobile"),
                "whatsappConsent", true,
                "loginSource", "MOBILE"
        );
        
        Map<String, Object> otpVars = Map.of(
                "mobile", ConfigManager.get("mobile"),
                "otp", ConfigManager.get("otp")
        );

        String baseUrl = ConfigManager.get("baseUrl");

     // LOGIN
     GraphQLClient.sendPreAuthRequest(
             baseUrl,
             Endpoints.USER_SERVICE,
             GraphQLQueries.LOGIN_MUTATION,
             loginVars,
             "login"
     );

     // VERIFY OTP
     Response response = GraphQLClient.sendPreAuthRequest(
             baseUrl,
             Endpoints.USER_SERVICE,
             GraphQLQueries.VERIFY_OTP_MUTATION,
             otpVars,
             "verifyOtp"
     );
        accessToken = response.jsonPath()
                .getString("data.verifyOtp.data.accessToken");
        refreshToken = response.jsonPath()
                .getString("data.verifyOtp.data.refreshToken");
        userId = response.jsonPath()
                .getString("data.verifyOtp.data.userId");
        
       // System.out.println("Access Token: " + accessToken);
    }
}