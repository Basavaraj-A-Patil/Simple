package base;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;

import utils.AuthUtil;

public class BaseTest {
	@BeforeSuite
    public void setup() {
        AuthUtil.initialize();
    }

    @AfterClass
    public void refreshToken() {
        AuthUtil.refreshToken();
    }

}