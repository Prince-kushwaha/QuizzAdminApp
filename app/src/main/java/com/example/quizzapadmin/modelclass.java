package com.example.quizzapadmin;

public class modelclass  {

    private String  url;
    private String name;

    private int sets;

    public int getSets() {

        return sets;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public modelclass(){
        //
    }

    public modelclass(String url, String name, int sets) {
        this.url = url;
        this.name = name;
        this.sets = sets;
    }



}
