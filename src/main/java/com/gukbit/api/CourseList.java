package com.gukbit.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

import static com.gukbit.api.ApiCommon.*;
import static com.gukbit.api.ApiCommon.getTagValue;

public class CourseList {
    public static Map<Integer,CourseData> courselist(){
        int page = 1;	// 페이지 초기값
        int pageSize = 100; // 한 페이지당 데이터 수
        String startDate="20210415";
        String endDate="20220415";
        Integer key = 0;
        Map<Integer, CourseData> map = new HashMap<>();
        // parsing할 url 지정(API 키 포함해서)
        String url = "https://www.hrd.go.kr/jsp/HRDP/HRDPO00/HRDPOA60/HRDPOA60_1.jsp?authKey="+authKey+"&returnType=XML&outType=1&pageNum="+page+"&pageSize=10&srchTraStDt="+startDate+"&srchTraEndDt="+endDate+"&sort=ASC&sortCol=TOT_FXNUM&srchTraArea1=11&srchKeco1=20";

        int count = 0;
        int totalPage = (int) Math.ceil((double)getTotalPage(url) / pageSize); //전체 페이지수 구하기
        try{
            while(true){

                url = "https://www.hrd.go.kr/jsp/HRDP/HRDPO00/HRDPOA60/HRDPOA60_1.jsp?authKey="+authKey+"&returnType=XML&outType=1&pageNum="+page+"&pageSize="+pageSize+"&srchTraStDt="+startDate+"&srchTraEndDt="+endDate+"&sort=ASC&sortCol=TOT_FXNUM&srchTraArea1=11&srchKeco1=20";
                DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
                Document doc = dBuilder.parse(url);

                // root tag
                doc.getDocumentElement().normalize();

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("scn_list");

                for(int temp = 0; temp < nList.getLength(); temp++){
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){

                        Element eElement = (Element) nNode;
                        CourseData courseData = new CourseData(getTagValue("trainstCstId", eElement),getTagValue("trprId", eElement),getTagValue("ncsCd", eElement),getTagValue("trprDegr", eElement),getTagValue("traStartDate", eElement),getTagValue("traEndDate", eElement),getTagValue("title", eElement));
                        map.put(key++, courseData);
                    }	// for end
                }	// if end
                page += 1;
                if(page > totalPage){
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }	// try~catch end
        return map;
    }
}
