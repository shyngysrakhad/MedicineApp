package com.a4devspirit.a1.medicineapp;

/**
 * Created by 1 on 09.04.2018.
 */

public class Diagnose_data {
    private String age;
    private String part;
    private String disease_id;
    private String percentage;

    public String getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
