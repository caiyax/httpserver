package com.bupt;

public class PageAction extends AbsAction{
    public ResponseEntity action() {
        ResponseEntity entity=new ResponseEntity();
        entity.setCode("300");
        entity.setMessage("主页测试");
        return entity;
    }
}
