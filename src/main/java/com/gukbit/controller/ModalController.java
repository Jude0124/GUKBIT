package com.gukbit.controller;

import com.gukbit.dto.AcademyDto;
import com.gukbit.service.AcademyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController //@ResponseBody 포함
@RequestMapping("/community")
public class ModalController {

    private final AcademyService academyService;

    public ModalController(AcademyService academyService) {
        this.academyService = academyService;
    }

    @PostMapping("/modal")
    public List<AcademyDto> modalReturn(String searchValue) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(searchValue);
        return academyDtoList;
    }
}
