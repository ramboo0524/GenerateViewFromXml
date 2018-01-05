package com.example.comlier;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangjianhua on 2018/1/4.
 */

public class RandomIDHolder {

    public int randomId ;

    private Map<String, String> idNameMap = new HashMap<String, String>();

    public int getNextRandomId() {
        randomId += 1 ;
        return randomId ;
    }

    public RandomIDHolder(){
        randomId = new SecureRandom().nextInt(Integer.MAX_VALUE / 2);
    }

    public boolean isIDNameOccupied(String name){
        if( idNameMap.containsKey( name ) ) {
            return true;
        }else{
            idNameMap.put(name, name);
            return false;
        }
    }


}
