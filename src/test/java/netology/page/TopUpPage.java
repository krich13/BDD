package netology.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import netology.data.DataHelper;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TopUpPage {
    private SelenideElement topUpButton = $("[data-test-id=action-transfer]");
    private SelenideElement transferAmount = $("[data-test-id=amount] .input__control");
    private SelenideElement fromWhatCardShouldBeTransfer = $("[data-test-id=from] .input__control");
    private SelenideElement cancelButton = $("[data-test-id=action-cancel]");
    private SelenideElement bannerError = $("[data-test-id=error-notification]");

    public TopUpPage() {
        $(Selectors.byText("Пополнение карты")).shouldBe(appear);
    }

    public void fillOutFormForPositiveTransferToFirstCardFromSecondCard(int amountToBeTransferred) { //метод заполнения формы коректными данными карты
        transferAmount.setValue(Integer.toString(amountToBeTransferred));
        fromWhatCardShouldBeTransfer.setValue(DataHelper.getSecondCreditCard().getCreditCard());
    }

    public void fillOutFormForNegativeTransferToFirstCardFromSecondCard(int amountToBeTransferredMoreThanExistOnCards) { //метод заполнения формы коректными данными карты
        transferAmount.setValue(Integer.toString(amountToBeTransferredMoreThanExistOnCards));
        fromWhatCardShouldBeTransfer.setValue(DataHelper.getSecondCreditCard().getCreditCard());
    }

    public void fillOutFormForPositiveTransferFromWrongCard(int amountToBeTransferred) { //метод заполнения формы некоректными данными карты
        transferAmount.setValue(Integer.toString(amountToBeTransferred));
        fromWhatCardShouldBeTransfer.setValue(DataHelper.getWrongCreditCard().getCreditCard());
    }

    public void clickTopUpButton() { //метод клика на кнопку пополнить
        topUpButton.click();
    }

    public void clickCancelButton() { //метод клика на кнопку отменить
        cancelButton.click();
    }

    public void shouldAppearBannerWrongCard() {
        bannerError.shouldBe(visible);
    }

}



