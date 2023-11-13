package com.objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum CoffeTypes{
    CT_SHORT(0, "Short"),
    CT_MEDIUM(1, "Medium"),
    CT_LONG(2, "long"),
    CT_GINSENG(3, "Ginseng"),
    CT_SBURAT(4,"Macchiato"),
    CT_TE(5, "Te"),
    CT_CAMOMILLE(6, "Camomil"),
    CT_CIUCULAT(7,"Ciuculat"),
    CT_COUNT(8, "\0");
    
    private int value;
    private String name;
    private static Map map = new HashMap<>();

    private CoffeTypes(int value, String name) {
        this.value = value;
        this.name = name;
    }

    static {
        for (CoffeTypes pageType : CoffeTypes.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static CoffeTypes valueOf(int pageType) {
        return (CoffeTypes) map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}



public class CoffeMachine {
    private List<Integer> coffes;
    private int total;
    public CoffeMachine()
    {
        coffes = new ArrayList<Integer>(CoffeTypes.CT_COUNT.getValue());
        for(int i = 0; i < coffes.size(); i++)
            coffes.add(0);
        total = 0;
    }

    public int getCount(CoffeTypes type)
    {
        return coffes.get(type.getValue());
    }

    public int getTotal()
    {
        total = 0;
        for(int i = 0; i < coffes.size(); i++)
            total += coffes.get(i);
        return total;
    }

    public void increaseCoffe(CoffeTypes type)
    {
        increaseCoffe(type,1);
    }


    public void increaseCoffe(CoffeTypes type, int val)
    {
        coffes.set(type.getValue(), coffes.get(type.getValue()) + val);
    }

    public void increaseCoffe(String value)
    {
        increaseCoffe(CoffeTypes.valueOf(value).getValue());
    }

    public void increaseCoffe(int type)
    {
        coffes.set(type, coffes.get(type) + 1);
    }
    
}
