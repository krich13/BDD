package netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import netology.data.DataHelper;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement topUpButton = $("[data-test-id=action-deposit]");
    private ElementsCollection cards = $$(".list__item");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";


    public int getFirstCardCreditBalance() {
        val text = cards.get(0).text();
        return extractBalanceFirstCard(text);
    }

    private int extractBalanceFirstCard(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public int getSecondCardCreditBalance() {
        val text = cards.get(1).text();
        return extractBalanceSecondCard(text);
    }

    private int extractBalanceSecondCard(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void clickTopUpButton() { //метод клика на кнопку пополнить
        topUpButton.click();
    }
}
