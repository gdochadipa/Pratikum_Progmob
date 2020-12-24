package co.ocha.pratikum_progmob.SharedPrefed;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.Console;

public class SharedPrefed {
    public static final String SP_LOGIN_APP = "spLoginApp";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_PHOTO = "spPhoto";
    public static final String SP_PASS = "spPass";
    public static final String SP_TOKEN = "spToken";
    public static final String SP_TOKEN_FCM = "spTokenFCM";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefed(Context context) {
        this.sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        this.spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP,value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSPPhoto(){
        return sp.getString(SP_PHOTO, "");
    }

    public String getSPPass(){
        return sp.getString(SP_PASS, "");
    }

    public String getSPToken(){
        return sp.getString(SP_TOKEN, "");
    }

    public String getSPTokenFCM(){
        return sp.getString(SP_TOKEN_FCM, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }

}
