package me.earth.phobos.features.modules.player;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public
class AntiDDoS
        extends Module {
    private static AntiDDoS instance;
    public final Setting < Boolean > full = this.register ( new Setting <> ( "Full" , false ) );
    private final Map < String, Setting > servers = new ConcurrentHashMap <> ( );
    public Setting < String > newIP = this.register ( new Setting < Object > ( "NewServer" , "Add Server..." , v -> ! this.full.getValue ( ) ) );
    public Setting < Boolean > showServer = this.register ( new Setting < Object > ( "ShowServers" , Boolean.FALSE , v -> ! this.full.getValue ( ) ) );

    public
    AntiDDoS ( ) {
        super ( "AntiDDoS" , "Prevents DDoS attacks" , Module.Category.PLAYER , false , false , true );
        instance = this;
    }

    public static
    AntiDDoS getInstance ( ) {
        if ( instance == null ) {
            instance = new AntiDDoS ( );
        }
        return instance;
    }

    @SubscribeEvent
    public
    void onSettingChange ( ClientEvent event ) {
        if ( Phobos.configManager.loadingConfig || Phobos.configManager.savingConfig ) {
            return;
        }
        if ( event.getStage ( ) == 2 && event.getSetting ( ) != null && event.getSetting ( ).getFeature ( ) != null && event.getSetting ( ).getFeature ( ).equals ( this ) ) {
            if ( event.getSetting ( ).equals ( this.newIP ) && ! this.shouldntPing ( this.newIP.getPlannedValue ( ) ) && ! event.getSetting ( ).getPlannedValue ( ).equals ( event.getSetting ( ).getDefaultValue ( ) ) ) {
                Setting setting = this.register ( new Setting <> ( this.newIP.getPlannedValue ( ) , Boolean.TRUE , v -> this.showServer.getValue ( ) && ! this.full.getValue ( ) ) );
                this.registerServer ( setting );
                Command.sendMessage ( "<AntiDDoS> Added new Server: " + this.newIP.getPlannedValue ( ) );
                event.setCanceled ( true );
            } else {
                Setting setting = event.getSetting ( );
                if ( setting.equals ( this.enabled ) || setting.equals ( this.drawn ) || setting.equals ( this.bind ) || setting.equals ( this.newIP ) || setting.equals ( this.showServer ) || setting.equals ( this.full ) ) {
                    return;
                }
                if ( setting.getValue ( ) instanceof Boolean && ! (Boolean) setting.getPlannedValue ( ) ) {
                    this.servers.remove ( setting.getName ( ).toLowerCase ( ) );
                    this.unregister ( setting );
                    event.setCanceled ( true );
                }
            }
        }
    }

    public
    void registerServer ( Setting setting ) {
        this.servers.put ( setting.getName ( ).toLowerCase ( ) , setting );
    }

    public
    boolean shouldntPing ( String ip ) {
        return ! this.isOff ( ) && ( this.full.getValue ( ) || this.servers.get ( ip.toLowerCase ( ) ) != null );
    }
}

