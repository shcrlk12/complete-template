package com.unison.scada.availability.global;

public class Util {
    public static String ifNullToEmptyString(Object o){
        if(o == null)
            return "";
        return o.toString();
    }
}
