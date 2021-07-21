package me.earth.phobos.util;

import me.earth.phobos.manager.HwidManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public
class HwidUtil {

    public static
    List < String > readURL ( ) {
        List < String > s = new ArrayList <> ( );
        try {
            final URL url = new URL ( HwidManager.checkURL );
            BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( url.openStream ( ) ) );
            String hwid;
            while ( ( hwid = bufferedReader.readLine ( ) ) != null ) {
                s.add ( hwid );
            }
        } catch ( Exception e ) {

        }
        return s;
    }
}
