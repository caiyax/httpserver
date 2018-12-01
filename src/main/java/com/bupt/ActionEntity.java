package com.bupt;

public class ActionEntity {
    private String url;
    private Class cls;
    private String method;

    public ActionEntity(){}
    public ActionEntity(String url, Class cls, String method) {
        this.url = url;
        this.cls = cls;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
