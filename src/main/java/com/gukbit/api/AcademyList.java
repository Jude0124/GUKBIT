package com.gukbit.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.*;

public class AcademyList {
    // tag값의 정보를 가져오는 메소드

    public static Map<String, AcademyData> academylist(){
        int page = 1;	// 페이지 초기값
        int pageSize = 50; // 한 페이지당 데이터 수
        String startDate=ApiCommon.getStartDate();
        String endDate=ApiCommon.getEndDate();
        Map<String, AcademyData> map = new HashMap<>();
        // parsing할 url 지정(API 키 포함해서)
        String url = "https://www.hrd.go.kr/jsp/HRDP/HRDPO00/HRDPOA60/HRDPOA60_1.jsp?authKey="+ ApiCommon.authKey+"&returnType=XML&outType=1&pageNum="+page+"&pageSize="+pageSize+"&srchTraStDt="+startDate+"&srchTraEndDt="+endDate+"&sort=ASC&sortCol=TOT_FXNUM&srchKeco1=20";

        int totalPage = (int) Math.ceil((double) ApiCommon.getTotalPage(url) / pageSize); //전체 페이지수 구하기
        try{
            while(true){
                url = "https://www.hrd.go.kr/jsp/HRDP/HRDPO00/HRDPOA60/HRDPOA60_1.jsp?authKey="+ ApiCommon.authKey+"&returnType=XML&outType=1&pageNum="+page+"&pageSize="+pageSize+"&srchTraStDt="+startDate+"&srchTraEndDt="+endDate+"&sort=ASC&sortCol=TOT_FXNUM&srchKeco1=20";
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
                        AcademyData academyData = new AcademyData(ApiCommon.getTagValue("subTitle", eElement), ApiCommon.getTagValue("trainstCstId", eElement), ApiCommon.getTagValue("trprId", eElement), ApiCommon.getTagValue("address", eElement));
                        map.put(ApiCommon.getTagValue("trainstCstId", eElement), academyData);
                    }	// for end
                }	// if end
                page += 1;
                if(page > totalPage) {
                    break;
                }
            }	// while end

            detailList(map);
        } catch (Exception e){
            e.printStackTrace();
        }	// try~catch end
        return map;
    }

    public static void detailList(Map<String, AcademyData> map){
        try{
            for (Map.Entry<String, AcademyData> entry:map.entrySet()){
                // parsing할 url 지정(API 키 포함해서)

                String url = "https://www.hrd.go.kr/jsp/HRDP/HRDPO00/HRDPOA60/HRDPOA60_2.jsp?authKey=o9fUZHPTkA3NoKfhmspVZUBNZt802MwO&returnType=XML&outType=2&srchTrprId="+entry.getValue().getTrainingId()+"&srchTrprDegr="+entry.getValue().getDummysession()+"&srchTorgId="+entry.getValue().getAcademyId();
                DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
                Document doc = dBuilder.parse(url);

                // root tag
                doc.getDocumentElement().normalize();

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("inst_base_info");

                for(int temp = 0; temp < nList.getLength(); temp++){
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
                        Element eElement = (Element) nNode;
                        entry.getValue().setAddr(ApiCommon.getTagValue("addr1", eElement)+ ApiCommon.getTagValue("addr2", eElement));
                        entry.getValue().setHpAddr(ApiCommon.getTagValue("hpAddr", eElement));
                        entry.getValue().setTel(ApiCommon.getTagValue("trprChapTel", eElement));
                    }	// for end
                }	// if end
            }	// while end


        } catch (Exception e){
            e.printStackTrace();
        }	// try~catch end
    }

}	// class end

