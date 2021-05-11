package com.example.licenta.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Food implements Comparable<Food>{
    String Aliment;
    double Calorii;
    double Carbohidrati;
    double Lipide;
    double Proteine;
    double Cantitate;

    public Food(){

    }

    public Food(String aliment, double calorii, double carbohidrati, double lipide, double proteine, double cantitate) {
        Aliment = aliment;
        Calorii = calorii;
        Carbohidrati = carbohidrati;
        Lipide = lipide;
        Proteine = proteine;
        Cantitate = cantitate;
    }

    public void setCarbohidrati(double carbohidrati) {
        Carbohidrati = carbohidrati;
    }

    public double getCantitate() {
        return Cantitate;
    }

    public void setCantitate(double cantitate) {
        Cantitate = cantitate;
    }

    public String getAliment() {
        return Aliment;
    }

    public void setAliment(String aliment) {
        Aliment = aliment;
    }

    public double getCalorii() {
        return Calorii;
    }

    public void setCalorii(double calorii) {
        Calorii = calorii;
    }

    public double getCarbohidrati() {
        return Carbohidrati;
    }

    public void setCarbohirati(double carbohidrati) {
        Carbohidrati = carbohidrati;
    }

    public double getLipide() {
        return Lipide;
    }

    public void setLipide(double lipide) {
        Lipide = lipide;
    }

    public double getProteine() {
        return Proteine;
    }

    public void setProteine(double proteine) {
        Proteine = proteine;
    }

    @Override
    public String toString() {
        return "Food{" +
                "Aliment='" + Aliment + '\'' +
                ", Calorii=" + Calorii +
                ", Carbohirati=" + Carbohidrati +
                ", Lipide=" + Lipide +
                ", Proteine=" + Proteine +
                '}';
    }

    public Food deepCopy(){
        Food f = new Food();
        f.setAliment(this.getAliment());
        f.setCalorii(this.getCalorii());
        f.setProteine(this.getProteine());
        f.setLipide(this.getLipide());
        f.setCarbohirati(this.getCarbohidrati());
        f.setCantitate(this.getCantitate());
        return f;
    }


    @Override
    public int compareTo(Food food) {
        String first = this.getAliment();
        String second = food.getAliment();
        return first.compareTo(second);
    }
}
