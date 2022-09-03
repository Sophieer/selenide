package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.WebDriverRunner.*;

public class AppTest {
    private static final String URL = "https://parabank.parasoft.com/parabank";

    @BeforeAll
    public static void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));

        //Configuration.browser = "firefox";
        Configuration.headless = true;
        Configuration.baseUrl = URL;
        Configuration.timeout = 3;
    }

    @Test
    public void userCanGoThroughAllTheMenuOptions() {
        open("/index.htm");
        $(By.className("Solutions")).click();
        assert url().contains(URL + "/index.htm");
        $(By.linkText("About Us")).click();
        assert url().contains(URL + "/about.htm");
        $(By.linkText("Services")).click();
        assert url().contains(URL + "/services.htm");
        $(By.linkText("Products")).click();
        assert url().contains("https://www.parasoft.com/products/");
        back();
        $(By.linkText("Locations")).click();
        assert url().contains("https://www.parasoft.com/solutions/");
        back();
        $(By.linkText("Admin Page")).click();
        assert url().contains(URL + "/admin.htm");
    }

    @Test
    public void userCanRegister() {
        String timestamp = String.valueOf(System.currentTimeMillis());

        open("/register.htm");
        $(By.id("customer.firstName")).setValue("Test");
        $(By.id("customer.lastName")).setValue(timestamp);
        $(By.id("customer.address.street")).setValue("Street test");
        $(By.id("customer.address.city")).setValue("City test");
        $(By.id("customer.address.state")).setValue("State test");
        $(By.id("customer.address.zipCode")).setValue("Zip Code test");
        $(By.id("customer.ssn")).setValue("SSN test");
        $(By.id("customer.username")).setValue("Test" + timestamp);
        $(By.id("customer.password")).setValue("Test123");
        $(By.id("repeatedPassword")).setValue("Test123");
        $(By.xpath("//input[@value='Register']")).click();
        $(By.className("title")).shouldHave(exactText("Welcome Test" + timestamp));
    }

    @Test
    public void userCanFindLoginInfo() {
        open("/lookup.htm");
        $(By.id("firstName")).setValue("Test");
        $(By.id("lastName")).setValue("Test");
        $(By.id("address.street")).setValue("Test");
        $(By.id("address.city")).setValue("Test");
        $(By.id("address.state")).setValue("Test");
        $(By.id("address.zipCode")).setValue("Test");
        $(By.id("ssn")).setValue("Test");
        $(By.xpath("//input[@value='Find My Login Info']")).click();
        $(By.xpath("//div[@id='rightPanel']//p[2]")).shouldHave(text("Username")).shouldHave(text("Password"));
    }

    @Test
    public void userCanLoginByUsername() {
        open("/index.htm");
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
        $(By.id("transactionTable")).shouldBe(visible);
    }

    @AfterEach
    public void closeBrowser() {
        Selenide.closeWindow();
    }
}
