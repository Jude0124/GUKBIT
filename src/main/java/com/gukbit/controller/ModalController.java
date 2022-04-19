package com.gukbit.controller;

import com.gukbit.dto.AcademyDto;
import com.gukbit.service.AcademyService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/community")
public class ModalController {

    private final AcademyService academyService;

    public ModalController(AcademyService academyService) {
        this.academyService = academyService;
    }

    @PostMapping("/modal")
//    @ResponseBody
    public List<AcademyDto> modalReturn(@RequestParam(value = "SearchValue") String searchValue) { // 500 error - java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
//  public String searchAcademy(@RequestParam(value = "keyword") String keyword, Model model) { // 400 error
//        List<AcademyDto> academyDtoList = academyService.searchAcademyModal(searchValue);
        System.out.println("searchValue = " + searchValue);
        List<AcademyDto> academyDtoList = academyService.searchAcademy(searchValue);
        System.out.println(academyDtoList.get(0).getName());
        System.out.println(academyDtoList.get(0).getCode());
        return academyDtoList;
    }
}
