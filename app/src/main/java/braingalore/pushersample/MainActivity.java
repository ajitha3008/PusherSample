package braingalore.pushersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.pusher.android.PusherAndroid;
import com.pusher.android.notifications.ManifestValidator;
import com.pusher.android.notifications.PushNotificationRegistration;
import com.pusher.android.notifications.fcm.FCMPushNotificationReceivedListener;
import com.pusher.android.notifications.interests.InterestSubscriptionChangeListener;
import com.pusher.android.notifications.tokens.PushNotificationRegistrationListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PusherAndroid pusher = new PusherAndroid("e74c6c080cf081127822");
        PushNotificationRegistration nativePusher = pusher.nativePusher();
        try {
            nativePusher.registerFCM(this);
        } catch (ManifestValidator.InvalidManifestException e) {
            e.printStackTrace();
        }
        try {
            nativePusher.registerFCM(this, new PushNotificationRegistrationListener() {
                @Override
                public void onSuccessfulRegistration() {
                    System.out.println("REGISTRATION SUCCESSFUL!!! YEEEEEHAWWWWW!");
                }

                @Override
                public void onFailedRegistration(int statusCode, String response) {
                    System.out.println(
                            "A real sad day. Registration failed with code " + statusCode +
                                    " " + response
                    );
                }
            });
        } catch (ManifestValidator.InvalidManifestException e) {
            e.printStackTrace();
        }
        nativePusher.subscribe("donuts"); // the client is interested in donuts
        //nativePusher.unsubscribe("donuts"); // we are no longer interested in donuts
        nativePusher.subscribe("donuts", new InterestSubscriptionChangeListener() {
            @Override
            public void onSubscriptionChangeSucceeded() {
                Toast.makeText(MainActivity.this, "Success! I love donuts!", Toast.LENGTH_SHORT).show();
                //System.out.println("Success! I love donuts!");
            }

            @Override
            public void onSubscriptionChangeFailed(int statusCode, String response) {
                Toast.makeText(MainActivity.this, ":(: received " + statusCode + " with" + response, Toast.LENGTH_SHORT).show();
                System.out.println(":(: received " + statusCode + " with" + response);
            }
        });
        nativePusher.setFCMListener(new FCMPushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                Log.i("ajitha", "From: " + remoteMessage.getFrom());
                Log.i("ajitha", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            }

        });
    }
}
