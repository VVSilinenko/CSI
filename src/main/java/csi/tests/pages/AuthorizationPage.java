package csi.tests.pages;

import org.openqa.selenium.*;
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
    private By enterToMail = By.xpath("/html/body/div[1]/div[1]/div/div[1]/div/a[1]");
    // Логотип
    private By logo = By.className("Logo");
    // Форма для ввода УЗ
    private By authForm = By.className("passp-page-overlay");
    // Поле для ввода логина
    private By loginField = By.name("login");
    // Кнопка "Войти" на форме ввода логина
    private By enterLogin = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/div[2]/div/div/div[1]/form/div[3]/button[1]");
    // Пароль
    private By passwdField = By.name("passwd");
    // Кнопка "Войти" на форме ввода пароля
    private By enterPasswd = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div/div/div[3]/div[2]/div/div[1]/form/div[2]/button[1]");
    // Ошибка при некорректных логин/пароль
    private By errorMessage = By.className("passp-form-field__error");
    // Кнопка "Написать"
    private By writeLetter = By.className("mail-ComposeButton-Text");

    // Проверка правильности загруженной страницы по нахождению на ней элемента
    public void checkURL(String url) throws Exception {
        if (!driver.getCurrentUrl().equals(url)){
            logger.info("Страница не загружена.");
            throw new Exception();
        } else {
            logger.info("Страница \"" + url + "\" загружена");
        }
    }

    // Авторизация пользователя
    public void authOnMail(String login, String passwd) {
        try {
            click(enterToMail);
            fill(loginField, login);
            click(enterLogin);
            fill(passwdField, passwd);
            click(enterPasswd);
            driver.findElement(writeLetter).isEnabled();
            logger.info("Авторизация пользователя успешна");
        } catch (NoSuchElementException ex) {
            if (isEnabled(errorMessage)){
                logger.log(Level.WARNING, "Ошибка при авторизации. " + driver.findElement(errorMessage).getText());
            } else {
                logger.log(Level.WARNING, "Ошибка при авторизации.");
            }
            throw new java.util.NoSuchElementException();
        }
    }

    private void click(By by) {
        try {
            driver.findElement(by).click();
        } catch (NoSuchElementException ex) {
            logger.severe("Неверный локатор элемента \"" + by.toString() + "\"");
            throw new java.util.NoSuchElementException();
        }
    }

    private void fill(By by, String text){
        try {
            driver.findElement(by).clear();
            driver.findElement(by).sendKeys(text);
        } catch (NoSuchElementException ex) {
            logger.severe("Неверный локатор элемента \"" + by.toString() + "\"");
            throw new java.util.NoSuchElementException();
        }
    }

    private boolean isEnabled(By by){
        try {
            driver.findElement(by);
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
