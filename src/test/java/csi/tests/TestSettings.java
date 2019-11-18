package csi.tests;

import csi.tests.pages.AuthorizationPage;
import csi.tests.pages.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class TestSettings {

    private static Logger logger;
    static {
        try (FileInputStream ins = new FileInputStream("log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            logger = Logger.getLogger(TestSettings.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    private WebDriver driver = new ChromeDriver();
    AuthorizationPage authorizationPage = new AuthorizationPage(driver, logger);
    MainPage mainPage = new MainPage(driver, logger);

    @BeforeEach
    void setUp() throws Exception {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(ConstValuesForTest.URL);
        authorizationPage.checkURL(ConstValuesForTest.URL);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
