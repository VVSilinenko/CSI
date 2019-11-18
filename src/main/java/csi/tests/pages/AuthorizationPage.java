package csi.tests.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorizationPage {

    private WebDriver driver;
    private Logger logger;

    public AuthorizationPage(WebDriver driver, Logger logger) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.logger = logger;
    }

    // *Войти в почту* на странице yandex.ru
    @FindBy (xpath = "/html/body/div[1]/div[1]/div/div[1]/div/a[1]")
    private WebElement enterToMail;
    // Поле для ввода логина
    @FindBy (name = "login")
    private WebElement loginField;
    // Кнопка "Войти" на форме ввода логина
    @FindBy (xpath = "//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/div[2]/div/div/div[1]/form/div[3]/button[1]")
    private WebElement enterLogin;
    // Пароль
    @FindBy (name = "passwd")
    private WebElement passwdField;
    // Кнопка "Войти" на форме ввода пароля
    @FindBy (xpath = "//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/div[2]/div/div[1]/form/div[2]/button[1]")
    private WebElement enterPasswd;
    // Ошибка при некорректных логин/пароль
    @FindBy (className = "passp-form-field__error")
    private WebElement errorMessage;
    // Кнопка "Написать"
    @FindBy (className = "mail-ComposeButton-Text")
    private WebElement writeLetter;

    // Проверка правильности загруженной страницы по нахождению на ней элемента
    public void checkURL(String url) {
        if (!driver.getCurrentUrl().equals(url)){
            logger.info("Страница не загружена.");
            throw new WebDriverException();
        } else {
            logger.info("Страница \"" + url + "\" загружена");
        }
    }

    // Авторизация пользователя
    public void authOnMail(String login, String passwd) {
        try {
            enterToMail.click();
            loginField.sendKeys(login);
            enterLogin.click();
            passwdField.sendKeys(passwd);
            enterPasswd.click();
            writeLetter.isEnabled();
            logger.info("Авторизация пользователя успешна");
        } catch (WebDriverException ex) {
            if (isEnabled(errorMessage)){
                logger.log(Level.WARNING, "Ошибка при авторизации. " + errorMessage.getText());
            } else {
                logger.log(Level.WARNING, "Ошибка при авторизации.");
            }
            throw new WebDriverException();
        }
    }

    private boolean isEnabled(WebElement webElement){
        try {
            webElement.isEnabled();
            return true;
        } catch (WebDriverException ex){
            return false;
        }
    }

    public HomePage successAuth(String login, String passwd){
        authOnMail(login,passwd);
        return new HomePage(driver,logger);
    }
}
