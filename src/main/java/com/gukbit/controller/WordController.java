package com.gukbit.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.gukbit.service.IWordAnalysisService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("WordController")
public class WordController {

    private Logger log = Logger.getLogger(this.getClass());

    @Resource(name="WordAnalysisService")
    private IWordAnalysisService wordAnalysisService;

    @RequestMapping(value = "word/analysis")
    @ResponseBody
    public Map<String, Integer> analysis() throws Exception {

        log.info(this.getClass().getName() + ".inputForm !");

        //분석할 문장
        String text = "아침에 밥을 꼭 먹고 점심엔 점심 밥을 꼭 먹고 저녁엔 저녁 밥을 꼭 먹자!";

        //신조어 및 새롭게 생겨난 가수 및 그룹명은 제대로 된 분석이 불가능합니다.
        // 새로운 명사 단어들은 어떻게 데이터를 처리해야 할까?? => 데이터사전의 주기적인 업데이트

        Map<String, Integer> rMap = new HashMap<>();



        rMap.putAll(wordAnalysisService.doWordAnalysis(text));

        if(rMap == null) {
            rMap = new HashMap<String, Integer>();
        }


        return rMap;
    }
}