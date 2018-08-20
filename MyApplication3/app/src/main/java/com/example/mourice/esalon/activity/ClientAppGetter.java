package com.example.mourice.esalon.activity;

/**
 * Created by Marcos on 5/31/2017.
 */

public class ClientAppGetter {
        private String Title,Price,Image,Client_name,Phone,Date,Time;

        public ClientAppGetter(){

        }

    public ClientAppGetter(String Title, String Price, String Image, String Client_name, String Phone, String Date, String Time){
            this.Title = Title;
            this.Price = Price;
            this.Image = Image;
            this.Client_name = Client_name;
            this.Phone = Phone;
            this.Date = Date;
            this.Time = Time;


        }
        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone_no) {
            Phone = phone_no;
        }

        public String getName() {
            return Client_name;
        }

        public void setName(String name_user) {
            Client_name = name_user;
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

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


}