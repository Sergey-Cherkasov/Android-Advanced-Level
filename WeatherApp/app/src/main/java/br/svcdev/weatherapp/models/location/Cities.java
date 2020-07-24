package br.svcdev.weatherapp.models.location;

import com.google.gson.annotations.SerializedName;

public class Cities {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String codeCountry;
    @SerializedName("coord")
    private CoordinatesLocation coord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCodeCountry() {
        return codeCountry;
    }

    public void setCodeCountry(String codeCountry) {
        this.codeCountry = codeCountry;
    }

    public CoordinatesLocation getCoord() {
        return coord;
    }

    public void setCoord(CoordinatesLocation coord) {
        this.coord = coord;
    }
}
