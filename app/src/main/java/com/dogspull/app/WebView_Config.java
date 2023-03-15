package com.dogspull.app;

public class WebView_Config {
    // WebView Settings Start Form Here....
    public  static boolean JavaScriptEnable(){
        final boolean JavaScript=true;
        return JavaScript;
    }
    public static boolean DomStorageEnabled(){
        final boolean DomStorage=true;
        return DomStorage;
    }
    public static boolean SavePassword(){
        final boolean SavePass=true;
        return SavePass;
    }
    public  static boolean SaveFormData(){
        final boolean SaveForm=true;
        return SaveForm;
    }
    public static boolean AppCache(){
        final boolean CacheYesNot=true;
        return CacheYesNot;
    }
    public static boolean SupportZoom(){
        final boolean ZoomYesNot=true;
        return ZoomYesNot;
    }

}
