package com.unison.scada.availability.global;

public class NullUtil {
    public static String ifNullEmptyString(Object o){
        if(o == null)
            return "";
        return o.toString();
    }

    public static boolean ifNullIsEmpty(Object o){
        if(o == null){
            return true;
        }
        else if(o == ""){
            return true;
        }
        return false;
    }
}