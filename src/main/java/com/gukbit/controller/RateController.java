package com.gukbit.controller;

import com.gukbit.dto.RateDto;
import com.gukbit.service.RateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/academy/rate")
public class RateController {

  private final RateService rateService;

  public RateController(RateService rateService) {
    this.rateService = rateService;
  }

  @GetMapping("/review-input")
  public String reviewInputMapping() {
    return "/view/review-input";
  }

  @PostMapping("/review-input")
  public String reviewInput(RateDto rateDto) {
    rateDto.setRid("testRid");
    rateDto.setC_cid("testaa011");
    rateDto.setUser_id("idTest");
    System.out.println(rateDto);
    rateService.saveReview(rateDto);
    return "redirect:/academy";
  }
}
