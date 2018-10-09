import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class FirstClass {
    public static void main(String[] args){
        WebDriver driver=new ChromeDriver();
//        driver.manage().window().setSize(new Dimension(1500,1000));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.103.by/");
        driver.findElement(By.className("Search__inputWrapper")).click();
        driver.findElement(By.xpath("//div[@class='Search Search--inModal']//input[@class='Search__input']")).sendKeys("Hello world!");
        driver.findElement(By.xpath("//*[text()='Искать в аптеках']")).click();
            driver.quit();
    }
}
