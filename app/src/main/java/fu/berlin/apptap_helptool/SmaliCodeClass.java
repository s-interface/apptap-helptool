package fu.berlin.apptap_helptool;

import android.os.Bundle;

/**
 *  A kind of stub class to make the extraction of smali code as easy as possible.
 *  The goal is to have only code relevant or helpful for smali conversion in this class.
 */
class SmaliCodeClass {

    /**
     * The method signature just mimics the signature of the intended hook method.
     *
     * Use EventTapHelper to pass on the arguments
     *
     */
    void hookMethod(String string1, String string2, long j_long, Bundle bundle, boolean bool1, boolean bool2, boolean bool3, String string3) {
        EventTapHelper.sendEventObjects(string1, string2, j_long, bundle, bool1, bool2, bool3, string3);
    }
}
