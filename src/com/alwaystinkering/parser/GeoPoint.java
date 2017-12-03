package com.alwaystinkering.parser;

public class GeoPoint {

    private double lat;
    private double lon;
    private double alt; //AGL?

    public GeoPoint(double lat, double lon, double alt) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public boolean isValid() {
        return (lat != 0.0 && lon != 0.0);
    }
    @Override
    public String toString() {
        return lon + "," + lat + "," + alt + "\n";
        //return lat + ", " + lon + ", " + alt + "\n";

//    public double distanceTo(final GeoPoint destination) {
//        try {
//            float[] result = new float[1];
//            Location.distanceBetween(this.lat, this.lon,
//                    destination.getLat(),
//                    destination.getLon(), result);
//            return result[0];
//        } catch (IllegalArgumentException iae) {
//            return Double.NaN;
//        }
    }
}
