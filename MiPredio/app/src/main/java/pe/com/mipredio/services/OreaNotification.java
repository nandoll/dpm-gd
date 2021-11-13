package pe.com.mipredio.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import pe.com.mipredio.R;

public class OreaNotification extends ContextWrapper {

    public static final String CHANNELID = "com.example.messenger";
    public static final String NAME = "Messenger";
    private NotificationManager notificationManager;
    private Context context;


    public OreaNotification(Context base) {
        super(base);
        this.context = base;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNELID, NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(CHANNELID);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationCompat.Builder getNotification(String title, String body, PendingIntent pIntent, Boolean soundUri, int icon) {
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context, CHANNELID)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setContentTitle(title)
                .setContentInfo("SMART");
        if(soundUri){
            notificationCompat.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
        return notificationCompat;

    }
}
