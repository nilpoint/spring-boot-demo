package com.example;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=ReadinglistApplication.class)
@WebIntegrationTest(randomPort=true)
public class ServerWebSeleniumTest {
  private static ChromeDriver browser;
  
  @Value("${local.server.port}")
  private int port;
  
  @BeforeClass
  public static void openBrowser() {
    browser = new ChromeDriver();
    browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }
  
  @AfterClass
  public static void closeBrowser(){
    browser.quit();
  }
}
