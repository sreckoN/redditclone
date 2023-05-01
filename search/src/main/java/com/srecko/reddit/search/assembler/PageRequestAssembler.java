package com.srecko.reddit.search.assembler;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * The type Page request assembler.
 */
public class PageRequestAssembler {

  private static final Logger logger = LogManager.getLogger(PageRequestAssembler.class);

  /**
   * Gets page request.
   *
   * @param pageable       the pageable
   * @param availableSorts the available sorts
   * @param defaultSort    the default sort
   * @return the page request
   */
  public static PageRequest getPageRequest(
      Pageable pageable, List<String> availableSorts, Sort defaultSort) {
    logger.info("Checking provided sorting");
    PageRequest pageRequest;
    String providedSort = pageable.getSort().toString().split(":")[0];
    if (availableSorts.stream().anyMatch(s -> s.equals(providedSort))) {
      pageRequest = PageRequest
          .of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
    } else {
      pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
    }
    return pageRequest;
  }
}
