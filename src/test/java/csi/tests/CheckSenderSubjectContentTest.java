package csi.tests;


import csi.tests.MyExceptions.AuthException;
import csi.tests.MyExceptions.NoLetterException;
import csi.tests.MyExceptions.NoSenderException;
import csi.tests.pages.HomePage;
import org.junit.jupiter.api.Test;

import static csi.tests.ConstValuesForTest.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckSenderSubjectContentTest extends TestSettings {

    @Test
    void senderSubjectContentTest() throws NoSenderException, NoLetterException, AuthException {

        String textOfMail = "Очень важно!!!";
        String senderOfMail = "Vitaliy Silinenko";
        String subjectOfMail = "Письмо с телефона";

        open(URL).successAuth(login, passwd).showLetterBySender("Vitaliy Silinenko");

        HomePage homePage = new HomePage(driver,logger);
        assertAll(
                () -> assertEquals(senderOfMail, homePage.getSender().getText()),
                () -> assertEquals(textOfMail, homePage.getTextOfMail().getText()),
                () -> assertEquals(subjectOfMail, homePage.getSubject().getText())
        );

        homePage.exitFromMail();

    }
}
