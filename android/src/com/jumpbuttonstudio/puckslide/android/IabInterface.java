package com.jumpbuttonstudio.puckslide.android;

public interface IabInterface {

    public String SKU_REMOVE_ADS = "remove_ads";

    // (arbitrary) request code for the purchase flow     
    static final int RC_REQUEST = 10001;
    public void removeAds();
}
