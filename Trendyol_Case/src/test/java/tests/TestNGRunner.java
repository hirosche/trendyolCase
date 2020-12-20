package tests;

import com.gargoylesoftware.htmlunit.javascript.host.intl.DateTimeFormat;
import com.opencsv.CSVReader;
import org.openqa.selenium.WebElement;
import support.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mehmet Yıldırım on 18.12.2020.
 */
public class TestNGRunner {

    @BeforeTest
    @Parameters({ "browser" })
    public void Setup(String browser) {
        if (browser.contains("Chrome")) {
            System.setProperty("webdriver.chrome.driver","webdrivers\\chromedriver.exe");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("--disable-web-sercurity");
            chromeOptions.addArguments("--allow-running-insecure-content");
            Driver.driver = new ChromeDriver(chromeOptions);
        }
        if (browser.contains("Firefox")) {
            System.setProperty("webdriver.gecko.driver", "webdrivers\\geckodriver.exe");
            Driver.driver=new FirefoxDriver();
        }
        Driver.driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
        Driver.driver.manage().window().maximize();
        Driver.driver.navigate().to("https://www.trendyol.com/");

    }
    @Test
    public void butikLinksAndResponseCodes() throws IOException, InterruptedException {
        Boutique_URLs.findBoutiqueURLsandResponseCode();
    }
    @Test
    public void imgLinksLoadTimeAndResponseCodes() throws IOException, InterruptedException {
        Boutique_URLs.findImgURLsLoadTimeandResponseCode();
    }

    @Test
    public void loginSenaryolari() throws FileNotFoundException, AWTException {
        Login.openLoginPage();
        CSVReader reader = new CSVReader(new FileReader("CSVFiles\\LoginKullaniciAdiveSifre.csv"));
        String[] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                for (String token : nextLine) {
                    String[] attributes = token.split(",");
                    Login.putEmailPass(attributes[0], attributes[1]);
                    Login.loginButton();
                    String hataMesaji = Driver.driver.findElement(By.xpath("//*[@id='error-box-wrapper']/span[2]")).getText();
                    if(!hataMesaji.isEmpty()) {
                        System.out.println(hataMesaji);
                        long zaman = System.currentTimeMillis();
                        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                        ImageIO.write(image, "png", new File("/screenshot"+zaman+".png"));
                        Driver.driver.navigate().to("https://www.trendyol.com/");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @AfterTest()
    public void afterTest() throws IOException {
        Driver.driver.quit();
    }


}
