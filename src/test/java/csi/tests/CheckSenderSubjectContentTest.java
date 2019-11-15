package csi.tests;

import csi.tests.MyExceptions.NoLetterException;
import csi.tests.MyExceptions.NoSenderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckSenderSubjectContentTest extends TestsSettings {

    @Test
    void senderSubjectContentTest() throws NoSenderException, NoLetterException {
        String textOfMail = "Очень важно!!!";
        String senderOfMail = "Виталий Силиненко";
        String subjectOfMail = "Письмо с телефона";

        authorizationPage.authOnMail(ConfigForTest.login, ConfigForTest.passwd);

        mainPage.showLetterBySender("Виталий Силиненко4");
        assertEquals(senderOfMail, mainPage.getSender().getText());
        assertEquals(textOfMail, mainPage.getTextOfMail().getText());
        assertEquals(subjectOfMail, mainPage.getSubject().getText());

        mainPage.exitFromMail();

    }
}
