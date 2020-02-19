package fu.berlin.apptap_helptool.extra;

import android.os.Bundle;

import fu.berlin.apptap_helptool.AppTapHelper;

/**
 *  A kind of stub class to make the extraction of smali code as easy as possible.
 *  The goal is to have only code relevant or helpful for smali conversion in this class.
 */
public class SmaliCodeClass {

    /**
     * The method signature just mimics the signature of the target method, where the code will
     * be injected.
     *
     * Use AppTapHelper to pass on the arguments
     *
     */
    public void targetMethod_01(String string1, String string2, long j_long, Bundle bundle, boolean bool1, boolean bool2, boolean bool3, String string3) {
        AppTapHelper.captureMethodArguments(string1, string2, j_long, bundle, bool1, bool2, bool3, string3);
    }

}
