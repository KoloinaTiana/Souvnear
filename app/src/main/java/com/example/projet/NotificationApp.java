package com.example.projet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationApp extends Service {

    private static final int NOTIFICATION_HOUR = 8; // L'heure à laquelle la vérification doit se faire
    private static final int NOTIFICATION_INTERVAL = 24 * 60 * 60 * 1000; // 24 heures en millisecondes
    private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000L; // nombre de millisecondes dans une journée
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long notificationTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        long delay = notificationTime <= currentTime ? notificationTime + MILLISECONDS_IN_DAY - currentTime : notificationTime - currentTime + 1000;

        mHandler.postDelayed(mRunnable, delay);;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Souv'near Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel pour les notifications de Souv'near");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification(int timeElapsed) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.logo_round)
                .setContentTitle("Rappel souv'near")
                .setContentText("Vous avez un souv'near d'il y a "+timeElapsed+ " an(s) aujourd'hui")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        int notificationID = (int) System.currentTimeMillis();
        Notification notification = builder.build();
        notificationManager.notify(notificationID, notification);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
            int uid = sharedPreferences.getInt("id", -1);
            boolean b = sharedPreferences.getBoolean("connexion", false);

            if (uid >= 0 && b) {
                List<String> dates = db.getDates(uid);

                for (String dateString : dates) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Log.i("Heyyyyyyyyyyyy", dateString);
                    try {
                        Date date = format.parse(dateString);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int month = cal.get(Calendar.MONTH) + 1;

                        Calendar today = Calendar.getInstance();
                        int todayDay = today.get(Calendar.DAY_OF_MONTH);
                        int todayMonth = today.get(Calendar.MONTH) + 1;

                        if (day == todayDay && month == todayMonth) {
                            int year = cal.get(Calendar.YEAR);
                            int timeElapsed = today.get(Calendar.YEAR) - year;
                            createNotification(timeElapsed);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };
}
