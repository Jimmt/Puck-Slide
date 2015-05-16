package com.jumpbuttonstudio.puckslide.android;

public interface IabInterface {

    public String SKU_REMOVE_ADS = "android.test.purchased";

    // (arbitrary) request code for the purchase flow     
    static final int RC_REQUEST = 10001;
    public void removeAds();
    public boolean getDisplayAds();
}
