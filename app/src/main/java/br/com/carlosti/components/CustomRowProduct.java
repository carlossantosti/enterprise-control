package br.com.carlosti.components;

import android.widget.ImageView;

import androidx.annotation.NonNull;

public class CustomRowProduct {
    private String product;
    private String description;
    private String nickname;
    private String productGroup;
    private String productTypeComplement;
    private ImageView image;

    public CustomRowProduct(String product, String description, String nickname, String productGroup, String productTypeComplement, ImageView image) {
        this.product = product;
        this.description = description;
        this.nickname = nickname;
        this.productGroup = productGroup;
        this.productTypeComplement = productTypeComplement;
        this.image = image;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getProductTypeComplement() {
        return productTypeComplement;
    }

    public void setProductTypeComplement(String productTypeComplement) {
        this.productTypeComplement = productTypeComplement;
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
        return this.getProduct() + " - " + this.getProductGroup();
    }
}
