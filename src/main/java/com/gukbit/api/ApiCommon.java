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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApiCommon {
    static final String authKey = "o9fUZHPTkA3NoKfhmspVZUBNZt802MwO";

    private static String[] DateBuilder(){
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String formatedNow = now.format(formatter);
        String yyyy = formatedNow.substring(0,4);
        String MM = formatedNow.substring(4,6);
        String[] yyyyMM = {yyyy,MM};
        return yyyyMM;
    }

    //현재 달의 15일 전후 6개월의 데이터를 받아오기 위함
    public static String getStartDate(){
        String[] yyyyMM = DateBuilder();
        String yyyy = yyyyMM[0];
        String MM = yyyyMM[1];
        if(Integer.parseInt(MM) - 6 <= 0) {
            yyyy = (Integer.parseInt(yyyy) - 1) + "";
            MM = (Integer.parseInt(MM) + 6) + "";
        }else{
            MM = (Integer.parseInt(MM) - 6) + "";
        }
        return yyyy + MM + "15";
    }
    //현재 달의 15일 전후 6개월의 데이터를 받아오기 위함
    public static String getEndDate(){
        String[] yyyyMM = DateBuilder();
        String yyyy = yyyyMM[0];
        String MM = yyyyMM[1];
        if(Integer.parseInt(MM) + 6 > 12) {
            yyyy = (Integer.parseInt(yyyy) + 1) + "";
            MM = (Integer.parseInt(MM) - 6) + "";
        }else{
            MM = (Integer.parseInt(MM) + 6) + "";
        }
        return yyyy + MM + "15";
    }

    static String getTagValue(String tag, Element eElement) {
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

    //scn_cnt 태그가 있어야 동작함
    static int getTotalPage(String url){
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
}
