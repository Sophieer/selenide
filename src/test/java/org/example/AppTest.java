package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class AppTest {
    @BeforeAll
    public static void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));

        Configuration.browser = "firefox";
        Configuration.headless = true;
    }

    @Test
    public void userCanLoginByUsername() {
        open("https://parabank.parasoft.com/parabank/index.htm");
        $(By.name("username")).setValue("john");
        $(By.name("password")).setValue("demo");
        $(By.xpath("//input[@value='Log In']")).click();
        $(By.className("title")).shouldHave(text("Accounts Overview"));
    }

    @Test
    public void userCanSwitchToFindTransactionAndSearchByAmount() {
        userCanLoginByUsername();
        $(By.linkText("Find Transactions")).click();
        $(By.className("title")).shouldHave(text("Find Transactions"));
        $(By.id("criteria.amount")).setValue("10");
        $(By.xpath("//input[@id='criteria.amount']/following::button")).click();
        $(By.id("transactionTableq")).shouldBe(visible);
    }

    @AfterEach
    public void closeBrowser() {
        closeWindow();
    }
}
