package csi.tests;

import csi.tests.pages.AuthorizationPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

class TestSettings {

    static Logger logger;
    static {
        try (FileInputStream ins = new FileInputStream("log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            logger = Logger.getLogger(TestSettings.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    static WebDriver driver = new ChromeDriver();

    @BeforeEach
    void setUp() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    static AuthorizationPage open(String url){
        driver.get(url);
        if (!driver.getCurrentUrl().equals(url)){
            logger.info("Страница не загружена.");
            throw new WebDriverException();
        } else {
            logger.info("Страница \"" + url + "\" загружена");
            return new AuthorizationPage(driver,logger);
        }
    }
}
