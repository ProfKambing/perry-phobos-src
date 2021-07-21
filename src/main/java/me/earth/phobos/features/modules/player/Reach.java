package me.earth.phobos.features.modules.player;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public
class Reach
        extends Module {
    private static Reach INSTANCE = new Reach ( );
    public Setting < Boolean > override = this.register ( new Setting <> ( "Override" , false ) );
    public Setting < Float > add = this.register ( new Setting < Object > ( "Add" , 3.0f , v -> ! this.override.getValue ( ) ) );
    public Setting < Float > reach = this.register ( new Setting < Object > ( "Reach" , 6.0f , v -> this.override.getValue ( ) ) );

    public
    Reach ( ) {
        super ( "Reach" , "Extends your block reach" , Module.Category.PLAYER , true , false , false );
        this.setInstance ( );
    }

    public static
    Reach getInstance ( ) {
        if ( INSTANCE == null ) {
            INSTANCE = new Reach ( );
        }
        return INSTANCE;
    }

    private
    void setInstance ( ) {
        INSTANCE = this;
    }

    @Override
    public
    String getDisplayInfo ( ) {
        return this.override.getValue ( ) ? this.reach.getValue ( ).toString ( ) : this.add.getValue ( ).toString ( );
    }
}

