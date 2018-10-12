import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.beans.FeatureDescriptor;
import java.util.concurrent.TimeUnit;

public class Test103 {
    private WebDriver driver;

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
//        Map<String, String> mobileEmulation = new HashMap<String, String>();
//
//        mobileEmulation.put("deviceName", "iPhone X");
//
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
//
//        driver = new ChromeDriver(chromeOptions);
//        driver = new FirefoxDriver();
//        driver.manage().window().maximize();
//        driver.manage().window().setSize(new Dimension(740,840));
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);

        driver.get("https://www.103.by/");
    }

    @AfterMethod(alwaysRun = true)
    public void finish(){
        driver.quit();
    }

    @Test(priority = 4, groups = {"canReach"})
    public void canReachSite_hardAsserts(){
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.103.by/",
                "URL is correct");

        Assert.assertTrue(driver.getTitle().contains("103.by"), "Header contains 103.by");
        driver.getCurrentUrl();
    }

    @Test(priority = 2, groups = {"canReach","regression"})
    public void canReachSite_softAssert(){
        SoftAssert softAssertion= new SoftAssert();

        softAssertion.assertEquals(driver.getCurrentUrl(), "https://www.103.by/",
                "URL is correct");

        softAssertion.assertTrue(driver.getTitle().contains("103.by"),
                "Header contains 103.by");

        softAssertion.assertAll();
    }

    @Test(groups = {"canFind", "regression"})
    public void canFindDrug(){
        WebElement searchContainer = driver.findElement(By.className("SearchContainer"));
        searchContainer.click();

        WebElement searchInput = driver.findElement(By.xpath("//div[contains(@class,'inModal')]//input[@class='Search__input']"));
        searchInput.sendKeys("Парацетамол");

        WebElement searchInFarmacy = driver.findElement(By.className("SearchResults__button"));
        searchInFarmacy.click();

        WebElement searchFormInput = driver.findElement(By.cssSelector(".searchForm__input"));
        String searchValue = searchFormInput.getAttribute("value");

        Assert.assertEquals("Парацетамол", searchValue, "Search result should contain 'Парацетамол'");
    }

    @Test(dataProvider = "getDrag", groups = {"canFind"})
    public void canFind_dataProvider(String dragName){
        WebElement searchContainer_desctop = driver.findElement(By.className("SearchContainer"));
        WebElement searchContainer_mobile = driver.findElement(By.xpath("//*[contains(@class, 'Icon--search')]"));

        if (driver.manage().window().getSize().getWidth()<960){
            searchContainer_mobile.click();
        } else {
            searchContainer_desctop.click();
        }


        WebElement searchInput = driver.findElement(By.xpath("//div[contains(@class,'inModal')]//input[@class='Search__input']"));
        searchInput.sendKeys(dragName);

        WebElement searchInFarmacy = driver.findElement(By.className("SearchResults__button"));
        searchInFarmacy.click();

        WebElement searchFormInput = driver.findElement(By.cssSelector(".searchForm__input"));
        String searchValue = searchFormInput.getAttribute("value");

        Assert.assertEquals(dragName, searchValue, String.format("Search result should contain '%s'", dragName));
    }

    @Test(dataProvider = "getDrag", groups = {"canFind"})
    public void hasSearchResults(String dragName){
        WebElement searchContainer_desctop = driver.findElement(By.className("SearchContainer"));
        WebElement searchContainer_mobile = driver.findElement(By.xpath("//*[contains(@class, 'Icon--search')]"));

        if (driver.manage().window().getSize().getWidth()<960){
            searchContainer_mobile.click();
        } else {
            searchContainer_desctop.click();
        }


        WebElement searchInput = driver.findElement(By.xpath("//div[contains(@class,'inModal')]//input[@class='Search__input']"));
        searchInput.sendKeys(dragName);

        WebElement searchInFarmacy = driver.findElement(By.className("SearchResults__button"));
        searchInFarmacy.click();

//        WebElement searchFormInput = driver.findElement(By.cssSelector(".searchForm__input"));
//        String searchValue = searchFormInput.getAttribute("value");

//        Assert.assertEquals(dragName, searchValue, "Search result should contain '" + dragName + "'");
        Assert.assertTrue(
                driver.findElement(By.cssSelector(".js-drugsForm__title"))
                        .getText().startsWith(dragName),
                "Search result should start with the drug name");
    }

    @DataProvider
    public Object[][] getDrag() {
        return new Object[][]{{"Уголь активированный"}, {"Парацетамол"}, {"Аспирин"}, {"Ибуфен"}, {"Пофигин"}};
    }
}
