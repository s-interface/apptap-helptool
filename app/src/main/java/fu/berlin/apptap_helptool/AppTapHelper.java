package fu.berlin.apptap_helptool;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class AppTapHelper {

    private static final Uri CONTENT_URI = Uri.parse("content://fu.berlin.apptap.eventreceiverprovider/call");
    private static final String BUNDLE_KEY_APP_ID = "app_id";
    private static final String BUNDLE_KEY_EVENT_ORIGIN = "event_origin";
    private static final String BUNDLE_KEY_EVENT_NAME = "event_name";
    private static final String BUNDLE_KEY_EVENT_TIMESTAMP = "event_timestamp";
    private static final String BUNDLE_KEY_BUNDLE_ARG = "bundle_arg";

    private static AppTapHelper INSTANCE;

    private final Context context;
    private final String PACKAGE_NAME;

    private AppTapHelper(Context context) {
        this.context = context.getApplicationContext();
        this.PACKAGE_NAME = context.getApplicationContext().getPackageName();
    }

    private static AppTapHelper getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException();
        }
        return INSTANCE;
    }

    private static String getPackageName() {
        return getInstance().PACKAGE_NAME;
    }

    public static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppTapHelper(context);
        }
    }

    public static void captureEvent(String string1, String string2, long j_long, Bundle bundle, boolean bool1, boolean bool2, boolean bool3, String string3) {
        Bundle sendBundle = new Bundle();

        //adding thread info
//        Thread t = Thread.currentThread();
//        sendBundle.putString("a_thread_info", t.getName() + ";" + t.getId());

        sendBundle.putString(BUNDLE_KEY_APP_ID, getPackageName());
        sendBundle.putString(BUNDLE_KEY_EVENT_ORIGIN,string1);
        sendBundle.putString(BUNDLE_KEY_EVENT_NAME, string2);
        sendBundle.putLong(BUNDLE_KEY_EVENT_TIMESTAMP, j_long);
        sendBundle.putBundle(BUNDLE_KEY_BUNDLE_ARG, bundle);

        getInstance().sendEvent(sendBundle);
    }

    public void sendEventObjects(Object... objects) {
        Bundle bundle = new Bundle();
        int index = 0;

        for (Object object : objects
        ) {
            if (object == null) {
                bundle.putString("object_" + index++, "null");
            } else if (object instanceof String) {
                bundle.putString("object_" + index++, (String) object);
            } else if (object instanceof Long) {
                bundle.putLong("object_" + index++, (Long) object);
            } else if (object instanceof Bundle) {
                bundle.putBundle("object_" + index++, (Bundle) object);
            } else if (object instanceof Boolean) {
                bundle.putBoolean("object_" + index++, (Boolean) object);
            } else {
                bundle.putString("object_" + index++, "unknown Class: " + object.getClass().toString() + "; string representation: " + object.toString());
            }
        }
        sendEvent(bundle);
    }

    private void sendEvent(Bundle bundle) {
        AsyncTask task = new callProviderTask(context).execute(bundle);
    }

    private static class callProviderTask extends AsyncTask<Bundle, Void, Void> {
        private final ContentResolver resolver;

        callProviderTask(Context context) {
            super();

            resolver = context.getContentResolver();
        }

        @Override
        protected Void doInBackground(Bundle... bundles) {
            // ToDo: write check if Uri is valid
            // maybe resolver.getType(CONTENT_URI); can help
            resolver.call(CONTENT_URI, "send_bundle", "none", bundles[0]);
            return null;
        }
    }
}
