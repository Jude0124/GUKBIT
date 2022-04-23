package com.gukbit.etc;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component //싱글톤
public class PopularSearchTerms {
    private Map<String, Integer> popularSearchTerms = new ConcurrentHashMap<>(); //여러 클라이언트에서 검색을 대비하여 Thread Safe한 Map 사용

    public void insert(String term) {
        int count = popularSearchTerms.containsKey(term) ? popularSearchTerms.get(term) : 0;
        popularSearchTerms.put(term, count + 1);
    }

    public int size(){
        return popularSearchTerms.size();
    }

    public List<String> getTopFive() {
        List<String> list = new ArrayList<>();

        List<Map.Entry<String, Integer>> listEntries = new ArrayList<>(popularSearchTerms.entrySet());
        Collections.sort(listEntries, new Comparator<Map.Entry<String, Integer>>() {
            // compare로 값을 비교
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2)
            {
                // 내림 차순으로 정렬
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });

        int indexMax = listEntries.size()>5?5:listEntries.size();
        for (int i = 0; i < indexMax; i++) {
            list.add(listEntries.get(i).getKey());
        }

        return list;
    }

    public void print() {
        for (String s : popularSearchTerms.keySet()) {
            System.out.println(s + "/" + popularSearchTerms.get(s));
        }
    }
}

