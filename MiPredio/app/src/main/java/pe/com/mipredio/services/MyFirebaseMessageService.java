package pe.com.mipredio.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import pe.com.mipredio.MainActivity;
import pe.com.mipredio.R;
import pe.com.mipredio.utils.Consts;
import pe.com.mipredio.utils.Tools;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    private final String TAG = "FIREBASE";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("====>", "NEW_TOKEN: " + token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        Log.i("==========>", "De: " + remoteMessage.getFrom());
        Log.i("==========>", "Mensaje: " + remoteMessage.getNotification().getBody());

        if (remoteMessage.getData().size() > 0) {
            Log.i("======>", "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
        } else if (remoteMessage.getNotification() != null) {
            Log.i("======>", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            createNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void createNotification(String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id_01", "my_channel_id_01", importance);
            channel.setDescription("my_channel_id_01");
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                //.addAction(R.drawable.common_google_signin_btn_icon_light, "ACEPTAR", actionPendingIntent)
                //.setWhen(System.currentTimeMillis())
                .setContentIntent(actionPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setContentTitle("Curso de Android")
                //.setSound(Uri.parse("android.resource://"+getPackageName()+"/" + R.raw.beeep))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentInfo("SMART");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }



    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("==========>", "De: " + remoteMessage.getFrom());
        Log.i("==========>", "Mensaje: " + remoteMessage.getNotification().getBody());
        if (remoteMessage.getData().size() > 0) {
            Log.i("======>", "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
        } else if (remoteMessage.getNotification() != null) {
            Log.i("======>", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        OreaNotification oreoNotification = new OreaNotification(this);
        NotificationCompat.Builder builder = oreoNotification.getNotification(
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                pendingIntent, true, R.drawable.login_logo);
        oreoNotification.getManager().notify(Consts.NOTIFICATION_CLOSE_DAY, builder.build());
    }
    */


}
