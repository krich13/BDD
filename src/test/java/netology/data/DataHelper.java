package netology.data;

import lombok.Value;

public class DataHelper {
    private DataHelper() {
    } //в параметре класса у нас есть пустой объект

    @Value
    public static class AuthInfo {  //создаем вложенный класс
        private String login;
        private String password;
    }

    public static AuthInfo getCorrectAuthInfo() { //создаем метод внутри вложенного класса, чтобы получить инфу для входа
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getWrongAuthInfo() {
        return new AuthInfo("vasya", "12345");
    }

    @Value
    public static class VerificationCode { //создаем еще один вложенный класс
        private String code;
    }


    public static VerificationCode getValidVerificationCodeFor(AuthInfo authInfo) { //создаем метод внутри вложенного класса, чтобы получить верфикационный код
        return new VerificationCode("12345");
    }

    public static VerificationCode getInvalidVerificationCode(AuthInfo authInfo) {
        return new VerificationCode("123");
    }

    @Value
    public static class CreditCard { //создаем еще один вложенный класс, чтобы были данные карты
        private String creditCard;
        private int creditCardBalance;
    }

    public static CreditCard getFirstCreditCard() { //метод класса, чтобы получать поля карты
        return new CreditCard("5559 0000 0000 0001", 10000);

    }

    public static CreditCard getSecondCreditCard() {
        return new CreditCard("5559 0000 0000 0002", 10000);
    }

    public static CreditCard getWrongCreditCard() {
        return new CreditCard("5559 0000 0000 4444", 10000);
    }
}