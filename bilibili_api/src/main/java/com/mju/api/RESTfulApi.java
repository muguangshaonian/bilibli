package com.mju.api;

import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RESTfulApi {

    private final Map<Integer,Map<String,Object>> dataMap;
    public RESTfulApi(){
        dataMap = new HashMap<>();
        for (int i = 1; i < 3; i++) {
            Map<String,Object> data = new HashMap<>();
            data.put("id",i);
            data.put("name","name"+i);
            dataMap.put(i,data);
        }
    }

    @GetMapping("/objects/{id}")
    public Map<String,Object> getData(@PathVariable("id") Integer id){
        return dataMap.get(id);
    }
    @DeleteMapping("/objects/{id}")
    public String deleteData(@PathVariable("id") Integer id){
        dataMap.remove(id);
        return "delete success";
    }
    @PostMapping("/objects")
    public String postData(@RequestBody Map<String,Object> data){
        int size = dataMap.size();
        Integer nextId = size+1;
        dataMap.put(nextId,data);
        return "post success";

    }
    @PutMapping("/objects")
    public String putData(@RequestBody Map<String,Object> data){
        Integer id = Integer.valueOf(String.valueOf(data.get("id")));
        Map<String, Object> containedMap = dataMap.get(id);
        if (containedMap==null){
            int size = dataMap.size();
            Integer nextId = size+1;
            dataMap.put(nextId,data);
        }else {
            dataMap.put(id,data);
        }
        return "put success";
    }
}
