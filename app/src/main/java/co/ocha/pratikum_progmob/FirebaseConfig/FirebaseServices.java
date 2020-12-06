package co.ocha.pratikum_progmob.FirebaseConfig;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.BuildConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import co.ocha.pratikum_progmob.BaseActivity;
import co.ocha.pratikum_progmob.R;

public class FirebaseServices extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("TAG", "Refreshed token: " + s);
        super.onNewToken(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> data = remoteMessage.getData();
        String dataPayload = data.get("data");
        Log.d("TAG", "Message data : "+dataPayload);
        if(remoteMessage.getData().size() > 0){
            Log.e("TAG", "Message data payload : " +remoteMessage.getData());

            try{
                JSONObject jsonObject = new JSONObject(dataPayload);
                showNotif(jsonObject.getString("title"), jsonObject.getString("message"));
            }catch (JSONException e){
                e.printStackTrace();
                Log.e("TAG","Message error"+ e.getMessage());
            }
        }

        if(remoteMessage.getNotification() != null){
            Log.e("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            showNotif(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showNotif(String title, String message){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("message",message);
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant")NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            notificationChannel.setDescription("BookStore Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setOnlyAlertOnce(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent);
        notificationManager.notify(1,notifBuilder.build());
    }

}
