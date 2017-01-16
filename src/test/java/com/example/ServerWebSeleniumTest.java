package com.example;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ssl.BrowserCompatHostnameVerifier;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
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
  
  @Test
  public void addBookToEmptyList() {
    String baseUrl = "http://localhost:" + port;
    
    // The very first thing that the test method does is use the FirefoxDriver to perform a
    // GET request for the reading list’s home page.
    browser.get(baseUrl);
    
    // It then looks for a <div> element on the page and asserts that its text indicates that 
    // no books are in the list.
    assertEquals("You have no books in your book list", browser.findElementByTagName("div").getText());
    
    /*
     * The next several lines look for the fields in the form and use the driver’s 
     * sendKeys() method to simulate keystroke events on those field elements 
     * (essentially filling in those fields with the given values). Finally, 
     * it looks for the <form> element and submits it.
     */
    browser.findElementByName("title")
      .sendKeys("BOOK TITLE");
    browser.findElementByName("author")
      .sendKeys("BOOK AUTHOR");
    browser.findElementByName("isbn")
      .sendKeys("1234567890");
    browser.findElementByName("description")
      .sendKeys("DESCRIPTION");
    browser.findElementByTagName("form")
      .submit();
    
    // After the form submission is processed, the browser should land on a page with the
    // new book in the list. So the final few lines look for the <dt> and <dd> elements in that
    // list and assert that they contain the data that the test submitted in the form.
    
    WebElement dl = browser.findElementByCssSelector("dt.bookHeadline");
    assertEquals("BOOK TITLE by BOOK AUTHOR (ISBN: 1234567890)", dl.getText());
    WebElement dt = browser.findElementByCssSelector("dd.bookDescription");
    assertEquals("DESCRIPTION", dt.getText());
  }
}
