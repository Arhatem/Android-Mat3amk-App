package com.example.mat3amk.Model;

public class Body {
    private Result[] results;

    public Result[] getResults() {
        return results;
    }

    public static  class Result
    {
        private POI poi;
        private MyAddress address;

        public POI getPoi() {
            return poi;
        }

        public MyAddress getAddress() {
            return address;
        }

        public static    class POI
        {
            private String name;

            public String getName() {
                return name;
            }
        }

        public  static class MyAddress
        {
            private    String streetName;
            private String municipalitySubdivision;

            public String getStreetName() {
                return streetName;
            }

            public String getMunicipalitySubdivision() {
                return municipalitySubdivision;
            }
        }
    }



}
