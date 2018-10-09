import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test1 {
    private WebDriver driver;

    @BeforeMethod
    public void start(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        driver.get("https://www.103.by/");
    }

    @Test
    public void canReachSite_hardAssert(){
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.103.by/","URL is correct");
        Assert.assertTrue(driver.getTitle().contains("103.by"), "Header contains 103.by");
        driver.getCurrentUrl();
    }

    @Test
    public void CanFindDrug(){
        String searchString = "Парацетамол";
        String manufacturer = "Борщаговский ХФЗ";

        driver.findElement(By.className("Search__inputWrapper")).click();
        driver.findElement(By.xpath("//div[@class='Search Search--inModal']//input[@class='Search__input']")).sendKeys(searchString);
        if( driver.findElements(By.xpath("//*[text()='"+searchString+"']")).isEmpty()){
            Assert.fail("not found");
        }
        driver.findElement(By.xpath("//*[text()='"+searchString+"']")).click();



        List<WebElement> listElements =  driver.findElements(By.cssSelector("div.drugsForm__list--main div.drugsForm"));
        if(listElements.isEmpty()){
            Assert.fail("there aren't any results for search string = "+searchString);
        }

        List<WebElement> searchManufactures = null;

        for(WebElement element : listElements){
            List<WebElement> listSpanElements = element.findElements(By.cssSelector("div.drugsForm__header div.drugsForm__description div.drugsForm__description-text span"));
            if(!listSpanElements.isEmpty()){
                for(WebElement span : listSpanElements){
                    if(manufacturer.equals(span.getText())){
                        searchManufactures = element.findElements(By.cssSelector("ul.drugsForm__items li.drugsForm__item"));
                        break;
                    }
                }
            }
        }
        if(searchManufactures != null && !searchManufactures.isEmpty()){
            searchManufactures.get(0).click();
        }else{
            Assert.fail("not found li elements");
        }
      //  driver.findElement(By.xpath("/html[@class='is-response']/body[@class='main-page __fixed-cyan  __cyan-bg __new-template']/div[@class='b-main ']/div[@class='b-global-wrap']/div[@class='b-global-offset']/div[@class='b-content_ads-offset __no-offset']/div[@class='b-main']/div[@class='b-main_body __grid-980 group']/div[@class='b-page-inner __main-col']/div[@class='drugsForm__list drugsForm__list--main js-drugsForm__list']/div[@class='drugsForm   free '][1]/ul[@class='drugsForm__items']/li[@class='drugsForm__item sku-holder ']/div[@class='drugsForm__item-left']/div[@class='drugsForm__item-description']/a[@class='drugsForm__item--Link']']")).click();
      //  Assert.assertT(driver.findElement(By.className("drugsForm  by free")),"List hear");
    }
    @AfterMethod
    public void finish(){
        driver.quit();
    }
}
