package com.srecko.reddit.assembler;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PageRequestAssemblerTest {

  @Test
  public void getPageRequest_WithProvidedSort_ReturnsPageRequestWithProvidedSort() {
    // given
    List<String> availableSorts = Arrays.asList("id", "name", "createdDate");
    Sort defaultSort = Sort.by("createdDate");
    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

    // when
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, availableSorts, defaultSort);

    // then
    assertEquals(pageable.getPageNumber(), pageRequest.getPageNumber());
    assertEquals(pageable.getPageSize(), pageRequest.getPageSize());
    assertEquals(pageable.getSort(), pageRequest.getSort());
  }

  @Test
  public void getPageRequest_WithUnavailableSort_ReturnsPageRequestWithDefaultSort() {
    // given
    List<String> availableSorts = Arrays.asList("id", "name", "createdDate");
    Sort defaultSort = Sort.by("createdDate");
    Pageable pageable = PageRequest.of(0, 10, Sort.by("updatedDate"));

    // when
    PageRequest pageRequest = PageRequestAssembler.getPageRequest(pageable, availableSorts, defaultSort);

    // then
    assertEquals(pageable.getPageNumber(), pageRequest.getPageNumber());
    assertEquals(pageable.getPageSize(), pageRequest.getPageSize());
    assertEquals(defaultSort, pageRequest.getSort());
  }
}