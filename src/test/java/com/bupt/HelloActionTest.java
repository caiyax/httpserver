package com.bupt;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.*;

public class HelloActionTest {
    private static Map<String,ActionEntity> map;
    @Test
    public void test() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        /*map=ActionMap.getMap();
        String url="/hello";
        ActionEntity actionEntity=map.get(url);
        Class cls=actionEntity.getCls();
        AbsAction action= (AbsAction) cls.newInstance();
        Method method = cls.getMethod(actionEntity.getMethod());
        ResponseEntity entity= (ResponseEntity) method.invoke(action);*/
        Class cls=HelloAction.class;
        AbsAction action= (AbsAction) cls.newInstance();
        Method method = cls.getMethod("hello",String.class);
        ResponseEntity entity= (ResponseEntity) method.invoke(action,"heni");
        System.out.println(entity);
    }
}