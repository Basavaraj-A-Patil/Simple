package utils;

import org.testng.Assert;

import io.restassured.response.Response;

public class AssertUtils {
	public static void verifyStatusCode(Response response, int expectedStatus) {
        Assert.assertEquals(response.getStatusCode(), expectedStatus,
                "Status code mismatch");
    }
	
	public static void verifyTrue(Boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }
	
	 public static void verifyNotNull(Object object, String message) {
	        Assert.assertNotNull(object, message);
	    }
	 public static void verifyNoGraphQLErrors(Response response) {
		    Object errors = response.jsonPath().get("errors");
		    Assert.assertTrue(errors == null || errors.toString().equals("[]"),
		            "❌ GraphQL errors present: " + errors);
		}
}
