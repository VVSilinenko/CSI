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

    public HomePage(WebDriver driver, Logger logger) {
        PageFactory.initElements(driver, this);
        this.logger = logger;
        this.driver = driver;
    }

    // Входящие
    @FindBy (xpath = "//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[2]/div[3]/div/div[1]/div[1]/a[1]")
    private WebElement inbox;
    // Список писем во Входящие
    @FindBy (xpath = "//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[3]/div[2]/div[5]/div[1]/div/div/div[2]/div/div")
    private List<WebElement> letters;
    // Тема письма в открытом письме
    @FindBy (className = "mail-Message-Toolbar-Subject-Wrapper")
    private WebElement subject;
    // Отправитель в открытом письме
    @FindBy (xpath = "//*[@id=\"nb-1\"]/body/div[2]/div[5]/div/div[3]/div[3]/div[2]/div[5]/div[1]/div/div[2]/div[1]/div[2]/div[3]/div[1]/div[1]/span[1]")
    private WebElement sender;
    // Текст сообщения в открытом письме
    @FindBy (className = "mail-Message-Body-Content")
    private WebElement textOfMail;
    // Настройки пользователя
    @FindBy (id = "recipient-1")
    private WebElement userName;
    // Элементы в форме с настройками пользователя
    @FindBy (className = "b-mail-dropdown__item")
    private List<WebElement> userSettingsElements;
    // Сообщение, если нет писем в папке "Входящие"
    @FindBy (className = "b-messages__placeholder-item")
    private WebElement noLettersMessage;

    // Выбор письма по имени Отправителя
    public void showLetterBySender(String senderName) throws NoSenderException, NoLetterException {
        inbox.click();
        if (isEnabled(noLettersMessage)){
            logger.warning("Писем в папке \"Входящие\" нет");
            throw new NoLetterException();
        }
        List<WebElement> lettersList = letters;
        int countOfLetters = lettersList.size();

        if (countOfLetters == 1){
            WebElement letter = lettersList.get(0);
            if (letter.findElement(By.className("mail-MessageSnippet-FromText")).getText().equals(senderName)) {
                logger.info("Всего писем от пользователя с именем \"" + senderName + "\" - " + countOfLetters);
                letter.findElement(By.className("mail-MessageSnippet-Wrapper")).click();
                return;
            }
        }
        if (countOfLetters > 1){
            int countOfLetterByOneSender = 0;
            ArrayList<WebElement> lettersFromSender = new ArrayList<>();
            int a = 0;
            for (int i = 0; i < countOfLetters; i++) {
                if (lettersList.get(i).findElement(By.className("mail-MessageSnippet-FromText")).getText().equals(senderName)) {
                    countOfLetterByOneSender = countOfLetterByOneSender + 1;
                    lettersFromSender.add(a,lettersList.get(i));
                    a = a + 1;
                }
            }
            logger.info("Всего писем от пользователя с именем \"" + senderName + "\" - " + countOfLetterByOneSender);
            if (countOfLetterByOneSender == 1) {
                lettersFromSender.get(0).click();
                return;
            }
            if (countOfLetterByOneSender > 1) {
                logger.info("Для проверки будет использоваться первое письмо в списке");
                lettersFromSender.get(0).click();
                return;
            }
        }
        logger.severe("Письмо от отправителя с именем \"" + senderName + "\" не найдено");
        throw new NoSenderException();
    }

    // Выход из почты
    public void exitFromMail() {
        if (isEnabled(userName)) {
            userName.click();
            for (WebElement element : userSettingsElements) {
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

    public WebElement getSubject() {
        return subject;
    }

    public WebElement getSender() {
        return sender;
    }

    public WebElement getTextOfMail() {
        return textOfMail;
    }

    private boolean isEnabled(WebElement webElement){
        try {
            webElement.isEnabled();
            return true;
        } catch (NoSuchElementException ex){
            return false;
        }
    }

}
