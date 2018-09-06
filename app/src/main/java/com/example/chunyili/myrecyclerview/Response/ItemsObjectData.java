package com.example.chunyili.myrecyclerview.Response;

import com.example.chunyili.myrecyclerview.Model.Magazine;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemsObjectData  {

    @SerializedName("total")
    private String total;
    @SerializedName("pre_page")
    private String pre_page;
    @SerializedName("page")
    private String page;
    @SerializedName("objects")
    private List<Magazine> objects;

    public String getTotal() {
        return total;
    }

    public String getPre_page() {
        return pre_page;
    }

    public String getPage() {
        return page;
    }

    public List<Magazine> getObjects() {
        return objects;
    }

}
