package csi.tests;


import csi.tests.MyExceptions.NoLetterException;
import csi.tests.MyExceptions.NoSenderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckSenderSubjectContentTest extends TestSettings {

    @Test
    void senderSubjectContentTest() throws NoSenderException, NoLetterException {
        String textOfMail = "Очень важно!!!";
        String senderOfMail = "Vitaliy Silinenko";
        String subjectOfMail = "Письмо с телефона";

        authorizationPage.authOnMail(ConstValuesForTest.login, ConstValuesForTest.passwd);
        mainPage.showLetterBySender("Vitaliy Silinenko");

        assertAll(
                () -> assertEquals(senderOfMail, mainPage.getSender().getText()),
                () -> assertEquals(textOfMail, mainPage.getTextOfMail().getText()),
                () -> assertEquals(subjectOfMail, mainPage.getSubject().getText())
        );

        mainPage.exitFromMail();

    }
}
