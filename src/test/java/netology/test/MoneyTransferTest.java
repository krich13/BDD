package netology.test;

import netology.data.DataHelper;
import netology.page.DashboardPage;
import netology.page.LoginPage;
import netology.page.TopUpPage;
import netology.page.VerificationPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {
    private final int amountToBeTransferred = 1000;
    private final int amountToBeTransferredMoreThanExistOnCards = 20000;

    @Test
    void shouldNotLoginWithInvalidData() { //тест на невалидный логин
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getWrongAuthInfo();
        loginPage.validLogin(authInfo);
        loginPage.invalidLoginBannerAppear();
    }

    @Test
    void shouldNotLoginWithInvalidVerificationCode() { //тест на невалидный верификационный код
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getCorrectAuthInfo();
        loginPage.validLogin(authInfo);
        VerificationPage user = new VerificationPage();
        DataHelper.VerificationCode verificationCode = DataHelper.getInvalidVerificationCode(authInfo);
        user.validVerify(verificationCode);
        user.invalidVerifyBannerAppear();
    }

    @Test
    void shouldOpenDashboardPageAfterClickOnCancel() { //при нажатии кнопки отмена, переход на дашборд и баланс не меняется
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getCorrectAuthInfo();
        loginPage.validLogin(authInfo);
        VerificationPage user = new VerificationPage();
        DataHelper.VerificationCode verificationCode = DataHelper.getValidVerificationCodeFor(authInfo);
        user.validVerify(verificationCode);
        DashboardPage dashboardPage = new DashboardPage();
        int balanceFirstCard = dashboardPage.getFirstCardCreditBalance();
        int balanceSecondCard = dashboardPage.getSecondCardCreditBalance();
        dashboardPage.clickTopUpButton();
        TopUpPage balancePage = new TopUpPage();
        String chosenCard = String.valueOf(DataHelper.getFirstCreditCard());
        balancePage.fillOutForm(amountToBeTransferred, chosenCard);
        balancePage.clickCancelButton();
        int balanceFirstCardAfterTransfer = dashboardPage.getFirstCardCreditBalance();
        int balanceSecondCardAfterTransfer = dashboardPage.getSecondCardCreditBalance();
        Assertions.assertEquals(balanceFirstCard, balanceFirstCardAfterTransfer);
        Assertions.assertEquals(balanceSecondCard, balanceSecondCardAfterTransfer);
    }

    @Test
    void shouldFailWithTransferToWrongCreditCard() { //не дает сделать перевод c неверной карты, при этом баланс карты, на которую должен быть перевод, не меняется
        open("http://localhost:9999");
        var loginPage = new LoginPage(); //создаем новый объект страницы логина
        DataHelper.AuthInfo authInfo = DataHelper.getCorrectAuthInfo(); //создаем новый объект класса DataHelper.AuthInfo, называем его и инициализируем
        loginPage.validLogin(authInfo); //вызываем метод нового объекта и передаем туда данные нового объекта
        VerificationPage user = new VerificationPage(); //создаем новый объект страницы верификации
        DataHelper.VerificationCode verificationCode = DataHelper.getValidVerificationCodeFor(authInfo); //создаем новый объект класса DataHelper.VerificationCode, называем его и инициализируем
        user.validVerify(verificationCode); //вызываем метод нового объекта и передаем туда данные кода
        DashboardPage dashboardPage = new DashboardPage(); //создаем новый объект страницы дашборда
        int balanceFirstCard = dashboardPage.getFirstCardCreditBalance();
        int balanceSecondCard = dashboardPage.getSecondCardCreditBalance();
        dashboardPage.clickTopUpButton();
        TopUpPage balancePage = new TopUpPage(); //для работы со страницей баланса создаю новый объект
        String chosenCard = String.valueOf(DataHelper.getWrongCreditCard());
        balancePage.fillOutForm(amountToBeTransferred, chosenCard);
        balancePage.clickTopUpButton(); //кликаю на кнопку пополнить
        balancePage.shouldAppearBannerWrongCard();
        balancePage.clickCancelButton();
        int balanceFirstCardAfterTransfer = dashboardPage.getFirstCardCreditBalance();
        int balanceSecondCardAfterTransfer = dashboardPage.getSecondCardCreditBalance();
        Assertions.assertEquals(balanceFirstCard, balanceFirstCardAfterTransfer);
        Assertions.assertEquals(balanceSecondCard, balanceSecondCardAfterTransfer);
    }

    @Test
    void shouldOpenDashboardAfterSuccessfulTransfer() { // тест при котором при успешном переводе, вы вернётесь назад на страницу со списком карт + баланс меняется после перевода
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getCorrectAuthInfo();
        loginPage.validLogin(authInfo);
        VerificationPage user = new VerificationPage();
        DataHelper.VerificationCode verificationCode = DataHelper.getValidVerificationCodeFor(authInfo);
        user.validVerify(verificationCode);
        DashboardPage dashboardPage = new DashboardPage();
        int balanceFirstCard = dashboardPage.getFirstCardCreditBalance();
        int balanceSecondCard = dashboardPage.getSecondCardCreditBalance();
        dashboardPage.clickTopUpButton();
        TopUpPage balancePage = new TopUpPage();
        String chosenCard = String.valueOf(DataHelper.getSecondCreditCard());
        balancePage.fillOutForm(amountToBeTransferred, chosenCard);
        balancePage.clickTopUpButton();
        int balanceFirstCardAfterTransfer = dashboardPage.getFirstCardCreditBalance();
        int balanceSecondCardAfterTransfer = dashboardPage.getSecondCardCreditBalance();
        Assertions.assertEquals(balanceFirstCard + amountToBeTransferred, balanceFirstCardAfterTransfer);
        Assertions.assertEquals(balanceSecondCard - amountToBeTransferred, balanceSecondCardAfterTransfer);
    }


    @Test
    void shouldFailIfTheTransferredAmountMoreThanShouldBe() { // тест, что при переводе большей суммы, чем есть на балансе, выходит ошибка (тут баг - так как минусовый баланс)
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        DataHelper.AuthInfo authInfo = DataHelper.getCorrectAuthInfo();
        loginPage.validLogin(authInfo);
        VerificationPage user = new VerificationPage();
        DataHelper.VerificationCode verificationCode = DataHelper.getValidVerificationCodeFor(authInfo);
        user.validVerify(verificationCode);
        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.clickTopUpButton();
        TopUpPage balancePage = new TopUpPage();
        String chosenCard = String.valueOf(DataHelper.getSecondCreditCard());
        balancePage.fillOutForm(amountToBeTransferredMoreThanExistOnCards, chosenCard);
        balancePage.clickTopUpButton();
        int balanceSecondCardAfterTransfer = dashboardPage.getSecondCardCreditBalance();
        Assertions.assertTrue(balanceSecondCardAfterTransfer > 0);
    }
}
