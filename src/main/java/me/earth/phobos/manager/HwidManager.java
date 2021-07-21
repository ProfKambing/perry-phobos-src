package me.earth.phobos.manager;

import me.earth.phobos.util.DisplayUtil;
import me.earth.phobos.util.HwidUtil;
import me.earth.phobos.util.NoStackTraceThrowable;
import me.earth.phobos.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

public
class HwidManager {

    public static final String checkURL = "https://pastebin.com/raw/8jWWhpqM";

    public static List < String > hwids = new ArrayList <> ( );

    public static
    void hwidCheck ( ) {
        hwids = HwidUtil.readURL ( );
        boolean isHwidPresent = hwids.contains ( SystemUtil.getSystemInfo ( ) );
        if ( ! isHwidPresent ) {
            DisplayUtil.Display ( );
            throw new NoStackTraceThrowable ( "" );
        }
    }
}
