package com.objects;

public class TouchSensor {
    private String code;
    private long timestamp;
    
    public TouchSensor()
    {
    }

    public boolean checkCode( String newTest)
    {
        return code.equals(newTest);
    }

    public void setCode(String code)
    {   
        this.code = code;
    }

    public long getTimestamp() 
    {
        return timestamp = System.currentTimeMillis();
    }


    public String getCode()
    {
        return code;
    }

}
