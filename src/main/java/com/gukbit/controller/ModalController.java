package com.gukbit.controller;

import com.gukbit.domain.Board;
import com.gukbit.dto.AcademyDto;
import com.gukbit.service.AcademyService;
import com.gukbit.service.BoardService;
import org.springframework.data.domain.Page;
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
    public List<AcademyDto> modalReturn(@RequestParam(value = "SearchValue") String searchValue) {
        List<AcademyDto> academyDtoList = academyService.searchAcademy(searchValue);
        return academyDtoList;
    }

}
