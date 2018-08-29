package com.nocson.eDepot;

public class EzWebServiceParam {
    private String key;
    private Object value;

    public EzWebServiceParam(String key,Object value)
    {
            this.key = key;
            this.value = value;
    }

    public String GetKey(){
        return  key;
    }

    public Object GetValue(){
        return  value;
    }

}
