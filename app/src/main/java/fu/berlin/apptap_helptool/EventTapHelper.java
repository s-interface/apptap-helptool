package fu.berlin.apptap_helptool;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class EventTapHelper {

    private static final Uri CONTENT_URI = Uri.parse("content://fu.berlin.apptap.eventreceiverprovider/call");

    private static EventTapHelper INSTANCE;

    private final Context context;

    private AsyncTask task = null;

    private EventTapHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    private static EventTapHelper getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException();
        }
        return INSTANCE;
    }

    static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EventTapHelper(context);
        }

    }

    private void sendEvent(Bundle bundle) {
        task = new callProviderTask(context).execute(bundle);
    }

    private void sendEvent(Bundle bundle, String... strings) {
        for (int i = 0; i < strings.length; i++) {
            String key = "extra_event_string_" + i;
            String value = strings[i];
            bundle.putString(key, value);
        }
        task = new callProviderTask(context).execute(bundle);
    }

    public void sendEvent(Bundle bundle, long j_long, String string1, String string2, String string3) {
        sendEvent(bundle, Long.valueOf(j_long).toString(), string1, string2, string3);
    }

    public static void sendEventObjects(Object... objects) {
        Bundle bundle = new Bundle();
        int index = 0;

        for (Object object : objects
        ) {
            if (object instanceof String) {
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
        getInstance().sendEvent(bundle);
    }

    private static class callProviderTask extends AsyncTask<Bundle, Void, Void> {
        private final ContentResolver resolver;

        callProviderTask(Context context) {
            super();

            resolver = context.getContentResolver();
        }

        @Override
        protected Void doInBackground(Bundle... bundles) {
            resolver.call(CONTENT_URI, "send_bundle", "none", bundles[0]);
            return null;
        }
    }
}
