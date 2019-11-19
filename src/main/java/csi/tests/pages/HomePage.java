package csi.tests.pages;

import csi.tests.MyExceptions.NoLetterException;
import csi.tests.MyExceptions.NoSenderException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HomePage {

    private WebDriver driver;
    private Logger logger;

    // Входящие
    @FindBy(xpath = "//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[2]/div[3]/div/div[1]/div[1]/a[1]")
    private WebElement inbox;
    // Список писем во Входящие
    @FindBy(xpath = "//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[3]/div[2]/div[5]/div[1]/div/div/div[2]/div/div")
    private List<WebElement> letters;
    // Тема письма в открытом письме
    @FindBy(className = "mail-Message-Toolbar-Subject-Wrapper")
    private WebElement subject;
    // Отправитель в открытом письме
    @FindBy(xpath = "//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[3]/div[2]/div[5]/div[1]/div/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/span[1]")
    private WebElement sender;
    // Текст сообщения в открытом письме
    @FindBy(className = "mail-Message-Body-Content")
    private WebElement textOfMail;
    // Настройки пользователя
    @FindBy(id = "recipient-1")
    private WebElement userName;
    // Элементы в форме с настройками пользователя
    @FindBy(className = "b-mail-dropdown__item")
    private List<WebElement> userSettingsElements;
    // Сообщение, если нет писем в папке "Входящие"
    @FindBy(className = "b-messages__placeholder-item")
    private WebElement noLettersMessage;

    public HomePage(WebDriver driver, Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
        this.driver = driver;
    }

    // Выбор письма по имени Отправителя
    public void showLetterBySender(String senderName) throws NoSenderException, NoLetterException {
        try {
            inbox.click();
        } catch (WebDriverException ex) {
            logger.severe("Ошибка при проверке письма");
            throw new WebDriverException();
        }
        List<WebElement> lettersList = letters;
        int countOfLetters = lettersList.size();
        if (countOfLetters == 0) {
            if (isEnabled(noLettersMessage)) {
                logger.warning("Писем в папке \"Входящие\" нет");
                throw new NoLetterException();
            } else {
                logger.severe("Ошибка при проверке локатора");
                throw new WebDriverException();
            }
        } else if (countOfLetters == 1) {
            WebElement letter = lettersList.get(0);
            if (letter.findElement(By.className("mail-MessageSnippet-FromText")).getText().equals(senderName)) {
                logger.info("Всего писем от пользователя с именем \"" + senderName + "\" - " + countOfLetters);
                letter.findElement(By.className("mail-MessageSnippet-Wrapper")).click();
            } else {
                logger.severe("Письмо от отправителя с именем \"" + senderName + "\" не найдено");
                throw new NoSenderException();
            }
        } else {
            int countOfLetterFromOneSender = 0;
            ArrayList<WebElement> lettersFromSender = new ArrayList<>();
            for (WebElement letter: lettersList) {
                if (letter.findElement(By.className("mail-MessageSnippet-FromText")).getText().equals(senderName)){
                    countOfLetterFromOneSender = countOfLetterFromOneSender + 1;
                    lettersFromSender.add(letter);
                }
            }
            if (countOfLetterFromOneSender == 0){
                logger.severe("Письмо от отправителя с именем \"" + senderName + "\" не найдено");
                throw new NoSenderException();
            } else if (countOfLetterFromOneSender == 1){
                logger.info("Всего писем от пользователя с именем \"" + senderName + "\" - " + countOfLetterFromOneSender);
                lettersFromSender.get(0).findElement(By.className("mail-MessageSnippet-Wrapper")).click();
            } else {
                logger.info("Всего писем от пользователя с именем \"" + senderName + "\" - " + countOfLetterFromOneSender);
                logger.info("Для проверки будет использоваться первое письмо в списке");
                lettersFromSender.get(0).click();
            }
        }
    }

    // Выход из почты
    public void exitFromMail() {
        logger.info("Проверки закончены");
        try {
            userName.click();
            for (WebElement element : userSettingsElements) {
                if (element.getText().equals("Выйти из сервисов Яндекса")) {
                    element.click();
                    logger.info("Выход пользователя выполнен");
                    return;
                }
            }
            throw new WebDriverException();
        } catch (WebDriverException ex){
            logger.severe("Ошибка при выходе.");
            throw new WebDriverException();
        }
    }

    public WebElement getSubject() {
        return subject;
    }

    public WebElement getSender() {
        return sender;
    }

    public WebElement getTextOfMail() {
        return textOfMail;
    }

    private boolean isEnabled(WebElement webElement) {
        try {
            webElement.isEnabled();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

}
