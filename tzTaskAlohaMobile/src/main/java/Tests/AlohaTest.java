package Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;

public class AlohaTest {
    private static WebDriver driver;

    @BeforeClass
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void smokeTest_CloseTab() {
        driver.get("https://www.google.com");
        String originalHandle = driver.getWindowHandle();

        // Открываем новую вкладку через JS
        ((JavascriptExecutor) driver).executeScript("window.open('https://www.example.com', '_blank');");

        // Ждем, пока количество вкладок станет 2
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> driver.getWindowHandles().size() == 2);

        // Получаем список вкладок
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());

        // Переключаемся на новую вкладку
        driver.switchTo().window(tabs.get(1));

        // Проверяем, что загрузился нужный URL
        Assert.assertTrue(driver.getCurrentUrl().contains("example.com"));

        // Закрываем вкладку
        driver.close();

        // Возвращаемся к оригинальной вкладке
        driver.switchTo().window(originalHandle);

        // Проверяем, что осталась одна вкладка
        Assert.assertEquals(driver.getWindowHandles().size(), 1);
    }

    @AfterClass
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
