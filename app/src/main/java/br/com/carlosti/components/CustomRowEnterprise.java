package br.com.carlosti.components;


import android.widget.ImageView;

import androidx.annotation.NonNull;

public class CustomRowEnterprise {
    private Integer empresa;
    private String name;
    private String city;
    private ImageView image;

    public CustomRowEnterprise(Integer empresa, String name, String city, ImageView image) {
        this.empresa = empresa;
        this.name = name;
        this.city = city;
        this.image = image;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName() + " - " + this.getCity();
    }
}
