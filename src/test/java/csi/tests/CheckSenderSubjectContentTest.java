package csi.tests;


import csi.tests.MyExceptions.NoLetterException;
import csi.tests.MyExceptions.NoSenderException;
import csi.tests.pages.AuthorizationPage;
import csi.tests.pages.HomePage;
import org.junit.jupiter.api.Test;

import static csi.tests.ConstValuesForTest.login;
import static csi.tests.ConstValuesForTest.passwd;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckSenderSubjectContentTest extends TestSettings {

    @Test
    void senderSubjectContentTest() throws NoSenderException, NoLetterException {
        AuthorizationPage authPage = new AuthorizationPage(driver, logger);
        HomePage homePage = new HomePage(driver,logger);

        String textOfMail = "Test";
        String senderOfMail = "Виталий Силиненко";
        String subjectOfMail = "For test";

        authPage.successAuth(login, passwd).showLetterBySender("Виталий Силиненко");

        assertAll(
                () -> assertEquals(senderOfMail, homePage.getSender().getText()),
                () -> assertEquals(textOfMail, homePage.getTextOfMail().getText()),
                () -> assertEquals(subjectOfMail, homePage.getSubject().getText())
        );

        homePage.exitFromMail();

    }
}
