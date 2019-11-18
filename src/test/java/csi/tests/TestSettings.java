package csi.tests;

import csi.tests.pages.AuthorizationPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class TestSettings {

    protected static Logger logger;
    static {
        try (FileInputStream ins = new FileInputStream("log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            logger = Logger.getLogger(TestSettings.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    protected WebDriver driver = new ChromeDriver();

    @BeforeEach
    void setUp() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(ConstValuesForTest.URL);
        new AuthorizationPage(driver,logger).checkURL(ConstValuesForTest.URL);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
