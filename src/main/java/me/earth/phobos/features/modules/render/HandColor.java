package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public
class HandColor
        extends Module {
    public static HandColor INSTANCE;
    public Setting < Boolean > colorSync = this.register ( new Setting <> ( "Sync" , false ) );
    public Setting < Boolean > rainbow = this.register ( new Setting <> ( "Rainbow" , false ) );
    public Setting < Integer > saturation = this.register ( new Setting < Object > ( "Saturation" , 50 , 0 , 100 , v -> this.rainbow.getValue ( ) ) );
    public Setting < Integer > brightness = this.register ( new Setting < Object > ( "Brightness" , 100 , 0 , 100 , v -> this.rainbow.getValue ( ) ) );
    public Setting < Integer > speed = this.register ( new Setting < Object > ( "Speed" , 40 , 1 , 100 , v -> this.rainbow.getValue ( ) ) );
    public Setting < Integer > red = this.register ( new Setting < Object > ( "Red" , 0 , 0 , 255 , v -> ! this.rainbow.getValue ( ) ) );
    public Setting < Integer > green = this.register ( new Setting < Object > ( "Green" , 255 , 0 , 255 , v -> ! this.rainbow.getValue ( ) ) );
    public Setting < Integer > blue = this.register ( new Setting < Object > ( "Blue" , 0 , 0 , 255 , v -> ! this.rainbow.getValue ( ) ) );
    public Setting < Integer > alpha = this.register ( new Setting <> ( "Alpha" , 255 , 0 , 255 ) );

    public
    HandColor ( ) {
        super ( "HandColor" , "Changes the color of your hands" , Module.Category.RENDER , false , false , false );
        INSTANCE = this;
    }
}

