import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Test1 {
    private WebDriver driver;
    private static String SELECTED_CITY = "Минск";
    private static String SEARCH_CITY = "Городок";

    @BeforeMethod(alwaysRun = true)
    @Parameters("Browser")
    public void start(@Optional String browserName){
        if (browserName != null) {
            if (browserName.contentEquals("Chrome")) {
                driver = new ChromeDriver();
            } else {
                driver = new FirefoxDriver();
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                firefoxProfile.setPreference("capability.policy.default.Window.frameElement.get","allAccess");
                firefoxProfile.setPreference("capability.policy.default.Window.QueryInterface", "allAccess");
            }
        } else {
            driver = new ChromeDriver();
        }

        System.setProperty("file.encoding", "UTF-8");
        driver.manage().window().maximize();
        //driver.manage().window().setSize(new Dimension(770,840));
        driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(50, TimeUnit.SECONDS);
        driver.get("https://www.103.by/");
    }


    @Test(priority = 1, groups = {"canReach"})
    public void canReachSite_hardAssert(){
        Assert.assertEquals(driver.getCurrentUrl(),"https://www.103.by/","URL is correct");
        Assert.assertTrue(driver.getTitle().contains("103.by"), "Header contains 103.by");
        driver.getCurrentUrl();
    }

    @Test(priority = 2,groups = {"Check"})
    public void setCity(){
        WebElement city = driver.findElement(By.cssSelector(".DropDown__text"));
        if (SELECTED_CITY.equals(city.getText())){
            city.click();
            List<WebElement> cityElements = driver.findElements(By.cssSelector(".List.CityFilter__modalList .List__item.CityFilter__item"));
            if (cityElements.isEmpty()) {
                Assert.fail("There aren't any city");
            }   else    {
                for (WebElement element: cityElements){

                    if (SEARCH_CITY.equals(element.findElements(By.cssSelector("span")).get(0).getText())){
                      element.click();
                      break;
                    }
                }

            }
        }
        driver.quit();
    }


    @Test(dataProvider = "getDrag",priority = 3, groups = {"canReach","regression"})
    public void CanFindDrug(String dragName) {
        WebElement searchContainer_desctop = driver.findElement(By.className("SearchContainer"));
        WebElement searchContainer_mobile = driver.findElement(By.xpath("//*[contains(@class,'Icon--search')]"));

        if (driver.manage().window().getSize().getWidth() < 768) {
            searchContainer_mobile.click();
        } else {
            searchContainer_desctop.click();
        }
        driver.findElement(By.xpath("//div[@class='Search Search--inModal']//input[@class='Search__input']")).sendKeys(dragName);
        if (driver.findElements(By.xpath("//*[text()='" + dragName + "']")).isEmpty()) {
            Assert.fail("not found");
        }
        driver.findElement(By.xpath("//*[text()='" + dragName + "']")).click();

        WebElement searchFormInput = driver.findElement(By.cssSelector(".searchForm__input"));
        String searchValue = searchFormInput.getAttribute("value");
        Assert.assertEquals(searchValue, dragName, String.format("Search result should contain ", dragName));
        Assert.assertEquals(driver.findElement(By.cssSelector("h1.drugsForm__h1.js-drugsForm__title")).getText().startsWith(dragName), true, "aaa");

        Assert.assertEquals(dragName.equals(driver.findElements(By.cssSelector("h3.drugsForm__description-title"))
                .stream().map(q -> q.getText())
                .collect(Collectors.<String>toList()).get(0)), true, "bbb");

        List<WebElement> listElements = driver.findElements(By.cssSelector("div.drugsForm__list--main div.drugsForm"));
        if (listElements.isEmpty()) {
            Assert.fail("there aren't any results for search string = " + dragName);
        }

        List<WebElement> searchManufactures = null;

        for (WebElement element : listElements) {
            List<WebElement> listSpanElements = element.findElements(By.cssSelector("div.drugsForm__header div.drugsForm__description div.drugsForm__description-text span"));
            if (!listSpanElements.isEmpty()) {
                for (WebElement span : listSpanElements) {
                    if (span.equals(span.getText())) {
                        searchManufactures = element.findElements(By.cssSelector("ul.drugsForm__items li.drugsForm__item"));
                        break;
                    }
                }
            }
        }
        if (searchManufactures != null && !searchManufactures.isEmpty()) {
            searchManufactures.get(0).click();
        } else {
            Assert.fail("not found li elements");
        }
        driver.findElement(By.xpath("/html[@class='is-response']/body[@class='main-page __fixed-cyan  __cyan-bg __new-template']/div[@class='b-main ']/div[@class='b-global-wrap']/div[@class='b-global-offset']/div[@class='b-content_ads-offset __no-offset']/div[@class='b-main']/div[@class='b-main_body __grid-980 group']/div[@class='b-page-inner __main-col']/div[@class='drugsForm__list drugsForm__list--main js-drugsForm__list']/div[@class='drugsForm   free '][1]/ul[@class='drugsForm__items']/li[@class='drugsForm__item sku-holder ']/div[@class='drugsForm__item-left']/div[@class='drugsForm__item-description']/a[@class='drugsForm__item--Link']']")).click();
  //      Assert.assertTrue(driver.findElement(By.className("drugsForm  by free")), "List hear");
    }

    @DataProvider
    public Object[][] getDrag() {
        return new Object[][]{{"Парацетамол"}};
    }

    @AfterMethod
    public void finish(){
        driver.quit();
    }
}
