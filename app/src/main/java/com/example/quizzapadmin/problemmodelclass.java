package com.example.quizzapadmin;

public class problemmodelclass {

    private  String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private  String optionD;
    private String correctoption;
    private int setNo;
    private String id;

    public problemmodelclass( String id,String correctoption, String optionA, String optionB, String optionC, String optionD, String question,  int setNo) {

        this.correctoption = correctoption;
this.id=id;

        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.question = question;
        this.optionD = optionD;
        this.setNo = setNo;
    }

    public problemmodelclass() {
        //
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public int getSetNo() {
        return setNo;
    }

    public void setSetNo(int setNo) {
        this.setNo = setNo;
    }

    public String getCorrectoption() {
        return correctoption;
    }

    public void setCorrectoption(String correctoption) {
        this.correctoption = correctoption;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
