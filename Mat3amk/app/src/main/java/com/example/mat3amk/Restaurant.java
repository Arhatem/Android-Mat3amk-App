package com.example.mat3amk;

import java.util.List;
import java.util.Map;

public class Restaurant {

private String name;
private float rating;
private String cover;
private String resKey;

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public Categories getCategories() {
        return categories;
    }

    private Categories categories;

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    public Restaurant()
    {}

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public static class Categories{

        private List<String> Additions;
        private List<String> Alexandrian_Hawawshi;
        private List<String> Dessert;
        private List<String> Oriental_Pizza;
        private List<String> Savory_Feteer;
        private List<String> Sawarekh;
        private List<String> Sweet_Feteer;

        public List<String> getAlexandrian_Hawawshi() {
            return Alexandrian_Hawawshi;
        }

        public List<String> getDessert() {
            return Dessert;
        }

        public List<String> getOriental_Pizza() {
            return Oriental_Pizza;
        }

        public List<String> getSavory_Feteer() {
            return Savory_Feteer;
        }

        public List<String> getSawarekh() {
            return Sawarekh;
        }

        public List<String> getSweet_Feteer() {
            return Sweet_Feteer;
        }
        public List<String> getAdditions() {
            return Additions;
        }


    }
}
