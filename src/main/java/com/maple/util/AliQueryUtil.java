package com.maple.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Date;

/**
 * Created by Maple.Ran on 2017/11/17.
 */
public class AliQueryUtil {


    public static String getJsonStr(Date startDate, Date endDate) {
        System.setProperty("webdriver.chrome.driver", "/Users/Maple.Ran/IdeaProjects/maple/src/main/resources/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("https://auth.alipay.com/login/index.htm");

        driver.findElement(By.xpath("//*[@id=\"J-loginMethod-tabs\"]/li[2]")).click();
        sleep(500);

        driver.findElement(By.id("J-input-user")).clear();

        WebElement username = driver.findElement(By.id("J-input-user"));
        slowInput(username,"shilihe001@163.com");
        sleep(600);
//        获取密码输入框
        driver.findElement(By.id("password_rsainput")).clear();
        WebElement password = driver.findElement(By.id("password_rsainput"));
        slowInput(password,"901901jj");


        WebElement button = driver.findElement(By.id("J-login-btn"));
        sleep(1000);
        button.click();

        sleep(500);
        String startStr = DateTimeUtil.dateToStr(startDate, "yyyy-MM-dd HH:ss:mm");
        String endStr = DateTimeUtil.dateToStr(endDate, "yyyy-MM-dd HH:ss:mm");

        org.openqa.selenium.Cookie ctoken = driver.manage().getCookieNamed("ctoken");
        ((JavascriptExecutor) driver).executeScript("function post(URL, PARAMS) {var temp = document.createElement(\"form\");temp.action = URL;temp.method = \"post\";temp.style.display = \"none\";for (var x in PARAMS) {var opt = document.createElement(\"textarea\");opt.name = PARAMS[x].name;opt.value = PARAMS[x].value;temp.appendChild(opt);}document.body.appendChild(temp);temp.submit();return temp;}post(\"https://mbillexprod.alipay.com/enterprise/fundAccountDetail.json\",[{name:\"queryEntrance\",value:\"1\"},{name:\"billUserId\",value:\"2088721226451734\"},{name:\"showType\",value:\"0\"},{name:\"type\",value:\"\"},{name:\"precisionQueryKey\",value:\"tradeNo\"},{name:\"startDateInput\",value:\""+startStr+"\"},{name:\"endDateInput\",value:\""+endStr+"\"},{name:\"pageSize\",value:\"500\"},{name:\"pageNum\",value:\"1\"},{name:\"total\",value:\"13\"},{name:\"sortTarget\",value:\"tradeTime\"},{name:\"order\",value:\"descend\"},{name:\"sortType\",value:\"0\"},{name:\"_input_charset\",value:\"gbk\"},{name:\"ctoken\",value:\""+ctoken.getValue()+"\"},])");
        String text = driver.findElement(By.xpath("/html/body/pre")).getText();
        driver.close();
        return text;
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void slowInput(WebElement element, String string) {
        for (int i =0; i<string.length();i++) {
            element.sendKeys(string.substring(i,i+1));
            sleep(100);
        }
    }
}
