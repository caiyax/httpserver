package com.bupt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ActionMap {
    private static Map<String,ActionEntity > map= new HashMap();

    public static Map<String,ActionEntity> getMap(){
        ActionEntity pageEntity=new ActionEntity("/",PageAction.class,"action");
        map.put("/",pageEntity);

        ActionEntity helloEntity=new ActionEntity("/hello",HelloAction.class,"hello");
        map.put("/hello",helloEntity);


        return map;
    }
}
