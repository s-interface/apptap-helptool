package fu.berlin.apptap_helptool;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class AppTapHelper {

    private static final Uri CONTENT_URI = Uri.parse("content://fu.berlin.apptap.eventreceiverprovider/call");

    private static AppTapHelper INSTANCE;

    private final Context context;

    private AsyncTask task = null;

    private AppTapHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    private static AppTapHelper getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException();
        }
        return INSTANCE;
    }

    public static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppTapHelper(context);
        }
    }

    public static void captureMethodArguments(String string1, String string2, long j_long, Bundle bundle, boolean bool1, boolean bool2, boolean bool3, String string3) {
        getInstance().sendEventObjects(string1, string2, j_long, bundle, bool1, bool2, bool3, string3);
    }

    public void sendEventObjects(Object... objects) {
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
        sendEvent(bundle);
    }

    private void sendEvent(Bundle bundle) {
        task = new callProviderTask(context).execute(bundle);
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
