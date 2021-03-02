package com.bakar.chatyychat;

import java.util.regex.Pattern;

public class GetdbVal {
    public String name;
    public String email;
    public GetdbVal(String value) {

        String[] valToArr = value.split("=");
        String[] unameArr = valToArr[2].split(",");
        name = unameArr[0];
        email = valToArr[3].replaceAll(Pattern.quote("}"), "");
    }

}
