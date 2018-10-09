import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.TimeUnit;

/**
 * Created by student on 04.10.2018.
 */
public class Test2 {
    private WebDriver driver;

    @BeforeSuite
    public void beforeSuite(){
        System.out.println("BeforeSuite");
    }

    @BeforeTest
    public void beforeTest(){
        System.out.printf("BeforeTest");
    }


    @BeforeClass
    public void beforeClass(){
        System.out.printf("BeforeClass");
    }


    @BeforeGroups
    public void beforeGroups(){
        System.out.printf("BeforeGroups");
    }

    @BeforeMethod
    public void start() {
        System.out.printf("BeforeMethod");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        driver.get("https://www.103.by/");
    }


    @Test
    public void canReachSite_hardAssert() {

        //       Assert.assertTrue(driver.getCurrentUrl().contentEquals("ttps://www.103.by/"),"URL is correct");
        Assert.assertEquals(driver.getCurrentUrl(), "ttps://www.103.by/", "URL is correct");
        Assert.assertTrue(driver.getTitle().contains("103.by"), "Header contains 103.by");
        //       Assert.assertTrue(driver.getTitle(),"104.by","Header contains 103.by");
        driver.getCurrentUrl();


        driver.quit();
    }

    @Test
    public void canReachSite_softAssert() {

        SoftAssert softAssertion = new SoftAssert();

        softAssertion.assertAll();
        ;

        //       Assert.assertTrue(driver.getCurrentUrl().contentEquals("ttps://www.103.by/"),"URL is correct");
        softAssertion.assertEquals(driver.getCurrentUrl(), "4ttps://www.103.by/", "URL is correct");
        softAssertion.assertTrue(driver.getTitle().contains("104.by"), "Header contains 103.by");
        softAssertion.assertAll();

        driver.getCurrentUrl();


        driver.quit();
    }

    @Test(priority = 1)
    public void canFindDrug() {

        driver.findElement(By.className("SearchContainer")).findElement(By.tagName("div")).click();

        driver.quit();
    }

    @AfterMethod
    public void finish() {
        driver.quit();
    }
}
