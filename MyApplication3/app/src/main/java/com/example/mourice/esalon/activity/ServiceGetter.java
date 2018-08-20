package com.example.mourice.esalon.activity;

/**
 * Created by Marcos on 5/7/2017.
 */

public class ServiceGetter {
    private String Title,Desc,Price,Image,name,phone;

    public ServiceGetter(){

    }
    public ServiceGetter(String Title,String Desc,String Price,String Image,String name,String phone){
        this.Title = Title;
        this.Desc =Desc;
        this.Price = Price;
        this.Image = Image;
        this.name = name;
        this.phone = phone;


    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone_no) {
        phone = phone_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name_user) {
        name = name_user;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
