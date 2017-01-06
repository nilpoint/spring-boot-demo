package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class ReadingListController {
  private ReadingListRepository readingListRepository;
  
  @Autowired
  public ReadingListController(ReadingListRepository rlr) {
    this.readingListRepository = rlr;
  }
  
  @RequestMapping(method=RequestMethod.GET)
  public String readersBooks(Reader reader, Model model) {
    List<Book> bookList = this.readingListRepository.findByReader(reader);
    if (bookList != null) {
      model.addAttribute("books", bookList);
    }
    return "readingList";
  }
  
  @RequestMapping(method=RequestMethod.POST)
  public String addToReadingList(Reader reader, Book book){
    book.setReader(reader);
    this.readingListRepository.save(book);
    return "redirect:/";
  }
}
