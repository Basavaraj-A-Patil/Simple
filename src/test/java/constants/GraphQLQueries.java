package constants;

public class GraphQLQueries {
	public static final String LOGIN_MUTATION = "mutation Login($mobile: String!, $whatsappConsent: Boolean!, $loginSource: loginSource) {"
			+ " login(mobile: $mobile, whatsappConsent: $whatsappConsent, loginSource: $loginSource) {"
			+ " success message errors { errorCode message statusCode } } }";

	public static final String VERIFY_OTP_MUTATION = "mutation VerifyOtp($mobile: String!, $otp: String!) {"
			+ " verifyOtp(mobile: $mobile, otp: $otp) {" + " success message data { accessToken refreshToken userId } "
			+ " errors { errorCode message statusCode } } }";

	public static final String REFRESH_TOKEN_MUTATION = "mutation RefreshToken($refreshToken: String!, $userId: String!) {"
			+ " refreshToken(refreshToken: $refreshToken, user_id: $userId) {"
			+ " success message data { accessToken refreshToken } " + " errors { errorCode message statusCode } } }";

	public static final String GET_USER_DETAILS_QUERY = "query GetUserDetails($getUserDetailsId: ID) {"
			+ "  getUserDetails(id: $getUserDetailsId) { success message"
			+ "  data {userId id  firstname lastname  name  email mobile   primaryVin   profilePicture"
			+ "  signupSource  emailVerified  whatsappConsent   pincode  createdAt   updatedAt  }" 
			+ "  errors { errorCode  message  statusCode } }}";
}
