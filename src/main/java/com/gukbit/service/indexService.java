package com.gukbit.service;

import com.gukbit.domain.Division_S;
import com.gukbit.repository.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class indexService {

    @Autowired
    IndexRepository indexRepository;

    public List<Division_S> selectSlideMenu () {
        return indexRepository.findAll();
    }



}
