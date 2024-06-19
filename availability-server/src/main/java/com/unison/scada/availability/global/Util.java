package com.unison.scada.availability.global;

public class Util {
    public static String ifNullToEmptyString(Object o){
        if(o == null)
            return "";
        return o.toString();
    }

    public static boolean isEmpty(Object o){
        if(o == null){
            return true;
        }
        else if(o == ""){
            return true;
        }
        return false;
    }
}
