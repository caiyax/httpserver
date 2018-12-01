package com.bupt;

public class HelloAction extends AbsAction{
    public ResponseEntity action() {
        ResponseEntity entity=new ResponseEntity();
        entity.setMessage("你好");
        entity.setCode("200");
        entity.setBody("age:20");
        return entity;
    }

    public ResponseEntity hello(@Param("str") String str,@Param("age") int age){
        System.out.println(str);
        ResponseEntity entity=new ResponseEntity();
        entity.setMessage("hello");
        entity.setCode("200");
        entity.setBody(str);
        return entity;
    }
}
