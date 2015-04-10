package com.umpay.enum1;
public enum EnumDemo2 {

    Man("lxy","male"),
    Woman("lf","female");
    
    private final String name;
    private final String value;
    EnumDemo2(String name,String value){
        this.name = name;
        this.value = value;
    };
    
    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
     
    public static void main(String[] args){
        System.out.println(Man.getName()+":"+Man.getValue());
        System.out.println(Woman.getName()+":"+Woman.getValue());
        System.out.println(EnumTest.te);
    }
     
}