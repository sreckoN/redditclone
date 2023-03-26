package com.srecko.reddit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
class TemplateControllerTest {

  @Autowired
  private MockMvc mockMvc;

  /*@Test
  void index() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api")
            .servletPath("/api"))
        .andExpect(view().name("index.html"));
  }*/
}