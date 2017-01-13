package com.example;

import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReadinglistApplication.class)
@WebAppConfiguration // Enables web context testing
public class MockMvcWebTests {
  @Autowired
  private WebApplicationContext weContext;
  
  private MockMvc mockMvc;
  
  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(weContext)
        .apply(springSecurity())
        .build();
  }
  
  @Test
  public void homePageTest() throws Exception {
    mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name("readingList"))
      .andExpect(model().attributeExists("books"))
      .andExpect(model().attribute("books", is(empty())));
  }
  
  @Test
  public void postBook() throws Exception {
    mockMvc.perform(post("/")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("title", "BOOK TITLE")
        .param("author", "AUTHOR")
        .param("isbn", "9876543210")
        .param("description", "DESCRIPTION")
        .param("reader", "john"))
      .andExpect(status().is3xxRedirection())
      .andExpect(header().string("Location", "/"));
    
    Reader reader = new Reader();
    reader.setUsername("john");
    
    Book expectedBook = new Book();
    expectedBook.setId(1L);
    expectedBook.setReader(reader);
    expectedBook.setTitle("BOOK TITLE");
    expectedBook.setAuthor("AUTHOR");
    expectedBook.setIsbn("9876543210");
    expectedBook.setDescription("DESCRIPTION");
    
    mockMvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name("readingList"))
      .andExpect(model().attributeExists("books"))
      .andExpect(model().attribute("books", hasSize(1)))
      .andExpect(model().attribute("books", contains(samePropertyValuesAs(expectedBook))));
  }
}
