package support;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

/**
 * Created by Mehmet Yıldırım on 17.12.2020.
 */
public class Boutique_URLs {


    private static final String USER_AGENT = "Mozilla/5.0";

    public  static void findBoutiqueURLsandResponseCode() throws InterruptedException, IOException {
        popUpKapat();
        Thread.sleep(2000);
        scrollDownToTheEnd();
        getInfo();
        System.out.println(Driver.driver.findElements(By.xpath("//article/a")).size());
        createCsvFileForButicLinkvsResponseCode();
    }

    public  static void findImgURLsLoadTimeandResponseCode() throws InterruptedException, IOException {
        popUpKapat();
        Thread.sleep(2000);
        scrollDownToTheEnd();
        getInfo();
        System.out.println(Driver.driver.findElements(By.xpath("//article/a")).size());
        createCsvFileForButicImgLinkTimevsResponseCode();
    }

    public static void popUpKapat(){
        new WebDriverWait(Driver.driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='Close']")));
        WebElement closeButton = Driver.driver.findElement(By.xpath("//a[@title='Close']"));
        closeButton.click();

    }

    public static void scrollDownToTheEnd() throws InterruptedException {
        int kacTane = 0;
        while(kacTane < 30){//support.driver.findElements(By.xpath("//article/a")).size()) {
            kacTane = Driver.driver.findElements(By.xpath("//article/a")).size();
            JavascriptExecutor jse = (JavascriptExecutor) Driver.driver;
            jse.executeScript("window.scrollBy(0,document.body.scrollHeight)");
            Thread.sleep(1000);
            System.out.println(Driver.driver.findElements(By.xpath("//article/a")).size());
        }
    }

    public static void createCsvFileForButicLinkvsResponseCode() throws IOException {
        File file = new File("CSVFiles\\ButikLinkResponseCode.csv");
        FileWriter output = new FileWriter(file);
        PrintWriter out = new PrintWriter(output);
        out.print("Butik Linki");
        out.print(" , ");
        out.print("ResponseCode");
        out.print("\n");
        for(WebElement we : Driver.driver.findElements(By.xpath("//article/a"))) {
            URL obj = new URL(we.getAttribute("href"));
            HttpsURLConnection con = (HttpsURLConnection)  obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            String responseCode = String.valueOf(con.getResponseCode());
            out.print(we.getAttribute("href"));
            out.print(" , ");
            out.print(responseCode);
            out.print("\n");
            if (responseCode.equals(String.valueOf(HttpsURLConnection.HTTP_OK))) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(we.getAttribute("href")+" , "+responseCode);
            } else {
                System.out.println("GET request not worked");
            }
        }
        out.flush();
        out.close();
        output.close();

    }

    public static void createCsvFileForButicImgLinkTimevsResponseCode() throws IOException {
        File file = new File("CSVFiles\\ButikImgLinkTimeResponseCode.csv");
        FileWriter output = new FileWriter(file);
        PrintWriter out = new PrintWriter(output);
        out.print("Img Load Time");
        out.print(" , ");
        out.print("ResponseCode");
        out.print("\n");
        for(WebElement we : Driver.driver.findElements(By.xpath("//article/a/span/img"))) {
            URL obj = new URL(we.getAttribute("src"));
            HttpsURLConnection con = (HttpsURLConnection)  obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            long startTime = System.currentTimeMillis();
            String responseCode = String.valueOf(con.getResponseCode());
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime ;
            out.print(elapsedTime);
            out.print(" , ");
            out.print(responseCode);
            out.print("\n");
            if (responseCode.equals(String.valueOf(HttpsURLConnection.HTTP_OK))) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(we.getAttribute("src")+" , "+elapsedTime+" , "+responseCode);
            } else {
                System.out.println("GET request not worked");
            }
        }
        out.flush();
        out.close();
        output.close();

    }

    public static void getInfo(){
        JavascriptExecutor jse = (JavascriptExecutor) Driver.driver;
        String timing = String.valueOf(jse.executeScript("var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;"));
        System.out.println(timing);

    }



}
