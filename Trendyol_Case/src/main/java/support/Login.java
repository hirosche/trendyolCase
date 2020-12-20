package support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Mehmet Yıldırım on 20.12.2020.
 */
public class Login {

    public static void openLoginPage(){
        WebElement girisYapButonu = Driver.driver.findElement(By.className("login-register-button-container"));
        girisYapButonu.click();
    }

    public static void putEmailPass(String emailAddress, String pass){
        WebElement email = Driver.driver.findElement(By.xpath("//input[@id=’login-email‘]"));
        email.sendKeys(emailAddress);
        WebElement password = Driver.driver.findElement(By.xpath("//input[@id=’login-password-input‘]"));
        password.sendKeys(pass);
    }

    public static void loginButton(){
        WebElement girisYapButonu = Driver.driver.findElement(By.xpath("//*[@id=\"login-register\"]/div[3]/div[1]/form/button"));
        girisYapButonu.click();
    }
}
