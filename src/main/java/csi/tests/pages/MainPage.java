package csi.tests.pages;

import csi.tests.MyExceptions.NoLetterException;
import csi.tests.MyExceptions.NoSenderException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.logging.Logger;

public class MainPage {

    private WebDriver driver;
    private Logger logger;

    public MainPage(WebDriver driver, Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
        this.driver = driver;
    }

    // Входящие
    private By inbox = By.xpath("//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[2]/div[3]/div/div[1]/div[1]/a[1]");
    // Список писем во Входящие
    private By letters = By.xpath("//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[3]/div[2]/div[5]/div[1]/div/div/div[2]/div/div");
    // Тема письма в открытом письме
    private By subject = By.className("mail-Message-Toolbar-Subject-Wrapper");
    // Отправитель в открытом письме
    private By sender = By.xpath("//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[3]/div[2]/div[5]/div[1]/div/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/span[1]");
    // Текст сообщения в открытом письме
    private By textOfMail = By.className("mail-Message-Body-Content");
    // Настройки пользователя
    private By userName = By.id("recipient-1");
    // Элементы в форме с настройками пользователя
    private By userSettingsElements = By.className("b-mail-dropdown__item");
    // Сообщение, если нет писем в папке "Входящие"
    private By noLettersMessage = By.className("b-messages__placeholder-item");

    // Выбор письма по имени Отправителя
    public void showLetterBySender(String senderName) throws NoSenderException, NoLetterException {
        driver.findElement(inbox).click();
        if (isEnabled(letters)) {
            List<WebElement> lettersList = driver.findElements(letters);
            int countOfLetters = lettersList.size();
            if (countOfLetters > 1) {
                WebElement letter;
                int countOfLetterByOneSender = 0;
                for (int i = 0; i < countOfLetters; i++) {
                    letter = lettersList.get(i);
                    if (letter.findElement(By.className("mail-MessageSnippet-FromText")).getText().equals(senderName)) {
                        countOfLetterByOneSender = countOfLetterByOneSender + 1;
                    }
                }
                if (countOfLetterByOneSender > 1) {
                    logger.info("Всего писем от пользователя с именем \"" + senderName + "\" - " + countOfLetterByOneSender);
                    logger.info("Для проверки будет использоваться первое письмо в списке");
                }
            }
            if (countOfLetters > 0) {
                WebElement letter;
                for (int i = 0; i < countOfLetters; i++) {
                    letter = lettersList.get(i);
                    if (letter.findElement(By.className("mail-MessageSnippet-FromText")).getText().equals(senderName)) {
                        logger.info("Письмо найдено");
                        letter.findElement(By.className("mail-MessageSnippet-Wrapper")).click();
                        return;
                    }
                    if (i == countOfLetters - 1) {
                        logger.severe("Письмо от отправителя с именем \"" + senderName + "\" не найдено");
                        throw new NoSenderException();
                    }
                }
            }
        } else {
            if (isEnabled(noLettersMessage)){
                logger.warning("Писем в папке \"Входящие\" нет");
                throw new NoLetterException();
            } else {
                logger.severe("Неверный локатор элемента");
                throw new WebDriverException();
            }
        }
    }

    // Выход из почты
    public void exitFromMail() {
        if (isEnabled(userName)) {
            driver.findElement(userName).click();
            for (WebElement element : driver.findElements(userSettingsElements)) {
                if (element.getText().equals("Выйти из сервисов Яндекса")) {
                    element.click();
                    logger.info("Выход пользователя выполнен");
                    return;
                }
            }
        } else {
            logger.info("Ошибка при выходе. Неверный локатор элемента");
            throw new WebDriverException();
        }

    }

    private boolean isEnabled(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (WebDriverException ex) {
            return false;
        }
    }

    public WebElement getSubject() {
        return driver.findElement(subject);
    }

    public WebElement getSender() {
        return driver.findElement(sender);
    }

    public WebElement getTextOfMail() {
        return driver.findElement(textOfMail);
    }
}
