package com.gukbit.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class AcademyList {
    private static final String authKey = "o9fUZHPTkA3NoKfhmspVZUBNZt802MwO";
    // tag값의 정보를 가져오는 메소드

    private static String getTagValue(String tag, Element eElement) {
        Node nValue=null;

        NodeList x= eElement.getElementsByTagName(tag);
        Node test=x.item(0);
        NodeList t=null;
        if(test!=null) {
            t= test.getChildNodes();
            if((Node)t.item(0)!=null) {nValue=(Node)t.item(0);}
        }
        if(nValue==null) return null;
        return nValue.getNodeValue();
    }

    private static int getTotalPage(String url){
        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        int pageNum = 0;
        try {
            dBuilder = dbFactoty.newDocumentBuilder();
            Document doc = dBuilder.parse(url);
            // root tag
            doc.getDocumentElement().normalize();

            // 파싱할 tag
            NodeList nList = doc.getElementsByTagName("scn_cnt");
            Node nNode = nList.item(0);
            if(nNode.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;
                pageNum = Integer.parseInt(eElement.getTextContent());
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return pageNum;
    }

    public static Map<String,Data> academylist(){
        int page = 1;	// 페이지 초기값
        int pageSize = 20; //페이지 사이즈
        String startDate="20210101";
        String endDate="20220414";
        Map<String,Data> map = new HashMap<>();
        // parsing할 url 지정(API 키 포함해서)
        String url = "https://www.hrd.go.kr/jsp/HRDP/HRDPO00/HRDPOA60/HRDPOA60_1.jsp?authKey="+authKey+"&returnType=XML&outType=1&pageNum="+page+"&pageSize="+pageSize+"&srchTraStDt="+startDate+"&srchTraEndDt="+endDate+"&sort=ASC&sortCol=TOT_FXNUM&srchTraArea1=11&srchKeco1=20&srchKeco2=2001&srchKeco3=200102";

        int totalPage = (int) Math.ceil((double)getTotalPage(url) / pageSize); //전체 페이지수 구하기
        try{
            //System.out.println("[AcademyList 시작=================================]");
            while(true){


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
                        Data data = new Data(getTagValue("subTitle", eElement),getTagValue("trainstCstId", eElement),getTagValue("trprId", eElement),getTagValue("address", eElement));
                        map.put(getTagValue("trainstCstId", eElement),data);
                        //System.out.println("["+count+"]data = " + data);
                        //count++;
                    }	// for end
                }	// if end
                page += 1;
                if(page > totalPage){
                    break;
                }
            }	// while end

            detailList(map);
        } catch (Exception e){
            e.printStackTrace();
        }	// try~catch end
        return map;
    }

    public static void detailList(Map<String,Data> map){
        try{
            for (Map.Entry<String,Data> entry:map.entrySet()){
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
                        //getTagValue("trprId", eElement)
                        entry.getValue().setAddr(getTagValue("addr1", eElement)+getTagValue("addr2", eElement));
                        entry.getValue().setHpAddr(getTagValue("hpAddr", eElement));
                        entry.getValue().setTel(getTagValue("trprChapTel", eElement));
                    }	// for end
                }	// if end
            }	// while end


        } catch (Exception e){
            e.printStackTrace();
        }	// try~catch end
    }

}	// class end

