package me.earth.phobos.features.modules.client;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.Render2DEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.gui.PhobosGui;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.manager.WaypointManager;
import me.earth.phobos.util.ColorUtil;
import me.earth.phobos.util.RenderUtil;
import me.earth.phobos.util.Timer;
import me.earth.phobos.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public
class PhobosChat
        extends Module {
    private static final ResourceLocation SHULKER_GUI_TEXTURE = null;
    public static PhobosChat INSTANCE;
    public static IRCHandler handler;
    public static List < String > phobosUsers;
    public Setting < String > ip = this.register ( new Setting <> ( "IP" , "127.0.0.1" ) );
    public Setting < Boolean > waypoints = this.register ( new Setting <> ( "Waypoints" , false ) );
    public Setting < Boolean > ding = this.register ( new Setting <> ( "Ding" , Boolean.FALSE , v -> this.waypoints.getValue ( ) ) );
    public Setting < Integer > red = this.register ( new Setting < Object > ( "Red" , 0 , 0 , 255 , v -> this.waypoints.getValue ( ) ) );
    public Setting < Integer > green = this.register ( new Setting < Object > ( "Green" , 255 , 0 , 255 , v -> this.waypoints.getValue ( ) ) );
    public Setting < Integer > blue = this.register ( new Setting < Object > ( "Blue" , 0 , 0 , 255 , v -> this.waypoints.getValue ( ) ) );
    public Setting < Integer > alpha = this.register ( new Setting < Object > ( "Alpha" , 255 , 0 , 255 , v -> this.waypoints.getValue ( ) ) );
    public Setting < Boolean > inventories = this.register ( new Setting <> ( "Inventories" , false ) );
    public Setting < Boolean > render = this.register ( new Setting < Object > ( "Render" , Boolean.TRUE , v -> this.inventories.getValue ( ) ) );
    public Setting < Integer > cooldown = this.register ( new Setting < Object > ( "ShowForS" , 2 , 0 , 5 , v -> this.inventories.getValue ( ) ) );
    public Setting < Boolean > offsets = this.register ( new Setting <> ( "Offsets" , false ) );
    private final Setting < Integer > yPerPlayer = this.register ( new Setting < Object > ( "Y/Player" , 18 , v -> this.offsets.getValue ( ) ) );
    private final Setting < Integer > xOffset = this.register ( new Setting < Object > ( "XOffset" , 4 , v -> this.offsets.getValue ( ) ) );
    private final Setting < Integer > yOffset = this.register ( new Setting < Object > ( "YOffset" , 2 , v -> this.offsets.getValue ( ) ) );
    public Setting < Integer > invH = this.register ( new Setting < Object > ( "InvH" , 3 , v -> this.inventories.getValue ( ) ) );
    public Setting < Bind > pingBind = this.register ( new Setting <> ( "Ping" , new Bind ( - 1 ) ) );
    public boolean status;
    public Timer updateTimer = new Timer ( );
    public Timer downTimer = new Timer ( );
    public BlockPos waypointTarget;
    private boolean down;
    private boolean pressed;

    public
    PhobosChat ( ) {
        super ( "PhobosChat" , "Phobos chat server" , Module.Category.CLIENT , true , false , true );
        INSTANCE = this;
    }

    public static
    void updateInventory ( ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( "updateinventory" );
        PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
        writeByteArray ( serializeInventory ( ) , PhobosChat.handler.outputStream );
    }

    public static
    void updateWaypoint ( final BlockPos pos , final String server , final String dimension , final Color color ) throws IOException {
        send ( "waypoint" , server + ":" + dimension + ":" + pos.getX ( ) + ":" + pos.getY ( ) + ":" + pos.getZ ( ) , color.getRed ( ) + ":" + color.getGreen ( ) + ":" + color.getBlue ( ) + ":" + color.getAlpha ( ) );
    }

    public static
    void removeWaypoint ( ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( "removewaypoint" );
        PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
        PhobosChat.handler.outputStream.flush ( );
    }

    public static
    void send ( final String command , final String data , final String data1 ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( command );
        PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
        PhobosChat.handler.outputStream.writeUTF ( data );
        PhobosChat.handler.outputStream.writeUTF ( data1 );
        PhobosChat.handler.outputStream.flush ( );
    }

    public static
    void send ( final String command , final String data ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( command );
        PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
        PhobosChat.handler.outputStream.writeUTF ( data );
        PhobosChat.handler.outputStream.flush ( );
    }

    private static
    byte[] readByteArrayLWithLength ( final DataInputStream reader ) throws IOException {
        final int length = reader.readInt ( );
        if ( length > 0 ) {
            final byte[] cifrato = new byte[length];
            reader.readFully ( cifrato , 0 , cifrato.length );
            return cifrato;
        }
        return null;
    }

    public static
    void writeByteArray ( final byte[] data , final DataOutputStream writer ) throws IOException {
        writer.writeInt ( data.length );
        writer.write ( data );
        writer.flush ( );
    }

    public static
    List < ItemStack > deserializeInventory ( final byte[] inventory ) throws IOException, ClassNotFoundException {
        final ObjectInputStream stream = new ObjectInputStream ( new ByteArrayInputStream ( inventory ) );
        return (ArrayList < ItemStack >) stream.readObject ( );
    }

    public static
    byte[] serializeInventory ( ) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream ( );
        final ObjectOutputStream oos = new ObjectOutputStream ( bos );
        oos.writeObject ( new ArrayList <> ( PhobosChat.mc.player.inventory.mainInventory ) );
        return bos.toByteArray ( );
    }

    public static
    void say ( final String message ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( "message" );
        PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
        PhobosChat.handler.outputStream.writeUTF ( message );
        PhobosChat.handler.outputStream.flush ( );
    }

    public static
    void cockt ( final int id ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( "cockt" );
        PhobosChat.handler.outputStream.writeInt ( id );
        PhobosChat.handler.outputStream.flush ( );
    }

    public static
    String getDimension ( final int dim ) {
        switch (dim) {
            case 0: {
                return "Overworld";
            }
            case - 1: {
                return "Nether";
            }
            case 1: {
                return "End";
            }
            default: {
                return "";
            }
        }
    }

    @Override
    public
    void onUpdate ( ) {
        if ( PhobosChat.handler != null && PhobosChat.handler.isAlive ( ) && ! PhobosChat.handler.isInterrupted ( ) ) {
            this.status = ! PhobosChat.handler.socket.isClosed ( );
        } else {
            this.status = false;
        }
        if ( this.updateTimer.passedMs ( 5000L ) && PhobosChat.handler != null && PhobosChat.handler.isAlive ( ) && ! PhobosChat.handler.socket.isClosed ( ) ) {
            try {
                PhobosChat.handler.outputStream.writeUTF ( "update" );
                PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
                PhobosChat.handler.outputStream.flush ( );
            } catch ( Exception e ) {
                e.printStackTrace ( );
            }
            this.updateTimer.reset ( );
        }
        if ( ! PhobosChat.mc.isSingleplayer ( ) && ! ( PhobosChat.mc.currentScreen instanceof PhobosGui ) && PhobosChat.handler != null && ! PhobosChat.handler.socket.isClosed ( ) && this.status ) {
            if ( this.down ) {
                if ( this.downTimer.passedMs ( 2000L ) ) {
                    try {
                        removeWaypoint ( );
                    } catch ( IOException e2 ) {
                        e2.printStackTrace ( );
                    }
                    this.down = false;
                    this.downTimer.reset ( );
                }
                if ( ! Keyboard.isKeyDown ( this.pingBind.getValue ( ).getKey ( ) ) ) {
                    try {
                        updateWaypoint ( this.waypointTarget , PhobosChat.mc.currentServerData.serverIP , String.valueOf ( PhobosChat.mc.player.dimension ) , new Color ( this.red.getValue ( ) , this.green.getValue ( ) , this.blue.getValue ( ) , this.alpha.getValue ( ) ) );
                    } catch ( IOException e2 ) {
                        e2.printStackTrace ( );
                    }
                }
            }
            if ( Keyboard.isKeyDown ( this.pingBind.getValue ( ).getKey ( ) ) ) {
                if ( ! this.pressed ) {
                    this.down = true;
                    this.pressed = true;
                }
            } else {
                this.down = false;
                this.pressed = false;
                this.downTimer.reset ( );
            }
        }
    }

    @Override
    public
    void onRender3D ( final Render3DEvent event ) {
        if ( Feature.fullNullCheck ( ) || PhobosChat.mc.isSingleplayer ( ) ) {
            return;
        }
        final RayTraceResult result = PhobosChat.mc.player.rayTrace ( 2000.0 , event.getPartialTicks ( ) );
        if ( result != null ) {
            this.waypointTarget = new BlockPos ( result.hitVec );
        }
        if ( this.waypoints.getValue ( ) ) {
            for (final WaypointManager.Waypoint waypoint : Phobos.waypointManager.waypoints.values ( )) {
                if ( PhobosChat.mc.player.dimension == waypoint.dimension ) {
                    if ( ! PhobosChat.mc.currentServerData.serverIP.equals ( waypoint.server ) ) {
                        continue;
                    }
                    waypoint.renderBox ( );
                    waypoint.render ( );
                    GlStateManager.enableDepth ( );
                    GlStateManager.depthMask ( true );
                    GlStateManager.enableLighting ( );
                    GlStateManager.disableBlend ( );
                    GlStateManager.color ( 1.0f , 1.0f , 1.0f , 1.0f );
                    RenderHelper.disableStandardItemLighting ( );
                }
            }
        }
    }

    @Override
    public
    void onRender2D ( final Render2DEvent event ) {
        if ( fullNullCheck ( ) ) {
            return;
        }
        if ( this.inventories.getValue ( ) ) {
            final int x = - 4 + this.xOffset.getValue ( );
            int y = 10 + this.yOffset.getValue ( );
            //private static final ResourceLocation SHULKER_GUI_TEXTURE;
            for (final String player : PhobosChat.phobosUsers) {
                if ( Phobos.inventoryManager.inventories.get ( player ) != null ) {
                    continue;
                }
                final List < ItemStack > stacks = Phobos.inventoryManager.inventories.get ( player );
                this.renderShulkerToolTip ( stacks , x , y , player );
                y += this.yPerPlayer.getValue ( ) + 60;
            }
        }
    }

    public
    void connect ( ) throws IOException {
        if ( ! PhobosChat.INSTANCE.status ) {
            final Socket socket = new Socket ( this.ip.getValue ( ) , 1488 );
            ( PhobosChat.handler = new IRCHandler ( socket ) ).start ( );
            PhobosChat.handler.outputStream.writeUTF ( "update" );
            PhobosChat.handler.outputStream.writeUTF ( PhobosChat.mc.player.getName ( ) );
            PhobosChat.handler.outputStream.flush ( );
            PhobosChat.INSTANCE.status = true;
            Command.sendMessage ( "\u00A7aIRC connected successfully!" );
        } else {
            Command.sendMessage ( "\u00A7cIRC is already connected!" );
        }
    }

    public
    void disconnect ( ) throws IOException {
        if ( PhobosChat.INSTANCE.status ) {
            PhobosChat.handler.socket.close ( );
            if ( ! PhobosChat.handler.isInterrupted ( ) ) {
                PhobosChat.handler.interrupt ( );
            }
        } else {
            Command.sendMessage ( "\u00A7cIRC is not connected!" );
        }
    }

    public
    void friendAll ( ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( "friendall" );
        PhobosChat.handler.outputStream.flush ( );
    }

    public
    void list ( ) throws IOException {
        PhobosChat.handler.outputStream.writeUTF ( "list" );
        PhobosChat.handler.outputStream.flush ( );
    }

    public
    void renderShulkerToolTip ( final List < ItemStack > stacks , final int x , final int y , final String name ) {
        GlStateManager.enableTexture2D ( );
        GlStateManager.disableLighting ( );
        GlStateManager.color ( 1.0f , 1.0f , 1.0f , 1.0f );
        GlStateManager.enableBlend ( );
        GlStateManager.tryBlendFuncSeparate ( GlStateManager.SourceFactor.SRC_ALPHA , GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA , GlStateManager.SourceFactor.ONE , GlStateManager.DestFactor.ZERO );
        assert false;
        PhobosChat.mc.getTextureManager ( ).bindTexture ( PhobosChat.SHULKER_GUI_TEXTURE );
        RenderUtil.drawTexturedRect ( x , y , 0 , 0 , 176 , 16 , 500 );
        RenderUtil.drawTexturedRect ( x , y + 16 , 0 , 16 , 176 , 54 + this.invH.getValue ( ) , 500 );
        RenderUtil.drawTexturedRect ( x , y + 16 + 54 , 0 , 160 , 176 , 8 , 500 );
        GlStateManager.disableDepth ( );
        final Color color = new Color ( 0 , 0 , 0 , 255 );
        this.renderer.drawStringWithShadow ( name , (float) ( x + 8 ) , (float) ( y + 6 ) , ColorUtil.toRGBA ( color ) );
        GlStateManager.enableDepth ( );
        RenderHelper.enableGUIStandardItemLighting ( );
        GlStateManager.enableRescaleNormal ( );
        GlStateManager.enableColorMaterial ( );
        GlStateManager.enableLighting ( );
        for (int i = 0; i < stacks.size ( ); ++ i) {
            final int iX = x + i % 9 * 18 + 8;
            final int iY = y + i / 9 * 18 + 18;
            final ItemStack itemStack = stacks.get ( i );
            PhobosChat.mc.getRenderItem ( ).zLevel = 501.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI ( itemStack , iX , iY );
            RenderUtil.itemRender.renderItemOverlayIntoGUI ( PhobosChat.mc.fontRenderer , itemStack , iX , iY , null );
            PhobosChat.mc.getRenderItem ( ).zLevel = 0.0f;
        }
        GlStateManager.disableLighting ( );
        GlStateManager.disableBlend ( );
        GlStateManager.color ( 1.0f , 1.0f , 1.0f , 1.0f );
    }


    private static
    class IRCHandler extends Thread {
        public Socket socket;
        public DataInputStream inputStream;
        public DataOutputStream outputStream;

        public
        IRCHandler ( final Socket socket ) {
            super ( Util.mc.player.getName ( ) );
            this.socket = socket;
            try {
                this.inputStream = new DataInputStream ( socket.getInputStream ( ) );
                this.outputStream = new DataOutputStream ( socket.getOutputStream ( ) );
            } catch ( IOException e ) {
                e.printStackTrace ( );
            }
        }

        @Override
        public
        void run ( ) {
            Command.sendMessage ( "\u00A7aSocket thread starting!" );
            while ( true ) {
                while ( true ) {
                    try {
                        while ( true ) {
                            final String input = this.inputStream.readUTF ( );
                            if ( input.equalsIgnoreCase ( "message" ) ) {
                                final String name = this.inputStream.readUTF ( );
                                final String message = this.inputStream.readUTF ( );
                                Command.sendMessage ( "\u00A7c[PhobosChat] \u00A7r<" + name + ">: " + message );
                            }
                            if ( input.equalsIgnoreCase ( "list" ) ) {
                                final String f = this.inputStream.readUTF ( );
                                final String[] split;
                                split = f.split ( "%%%" );
                                for (final String friend : split) {
                                    Command.sendMessage ( "\u00A7b" + friend.replace ( "_&_" , " ID: " ) );
                                }
                            } else if ( input.equalsIgnoreCase ( "friendall" ) ) {
                                final String f = this.inputStream.readUTF ( );
                                final String[] split2;
                                split2 = f.split ( "%%%" );
                                for (final String friend : split2) {
                                    if ( ! friend.equals ( Util.mc.player.getName ( ) ) ) {
                                        Phobos.friendManager.addFriend ( friend );
                                        Command.sendMessage ( "\u00A7b" + friend + " has been friended" );
                                    }
                                }
                            } else if ( input.equalsIgnoreCase ( "waypoint" ) ) {
                                final String name = this.inputStream.readUTF ( );
                                final String[] inputs = this.inputStream.readUTF ( ).split ( ":" );
                                final String[] colors = this.inputStream.readUTF ( ).split ( ":" );
                                final String server = inputs[0];
                                final String dimension = inputs[1];
                                final Color color = new Color ( Integer.parseInt ( colors[0] ) , Integer.parseInt ( colors[1] ) , Integer.parseInt ( colors[2] ) , Integer.parseInt ( colors[3] ) );
                                Phobos.waypointManager.waypoints.put ( name , new WaypointManager.Waypoint ( name , server , Integer.parseInt ( dimension ) , Integer.parseInt ( inputs[2] ) , Integer.parseInt ( inputs[3] ) , Integer.parseInt ( inputs[4] ) , color ) );
                                Command.sendMessage ( "\u00A7c[PhobosChat] \u00A7r" + name + " has set a waypoint at " + "\u00A7c" + "(" + Integer.parseInt ( inputs[2] ) + "," + Integer.parseInt ( inputs[3] ) + "," + Integer.parseInt ( inputs[4] ) + ")" + "\u00A7r" + " on the server " + "\u00A7c" + server + "\u00A7r" + " in the dimension " + "\u00A7c" + PhobosChat.getDimension ( Integer.parseInt ( dimension ) ) );
                                if ( PhobosChat.INSTANCE.ding.getValue ( ) ) {
                                    Util.mc.world.playSound ( Util.mc.player.posX , Util.mc.player.posY , Util.mc.player.posZ , SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP , SoundCategory.PLAYERS , 1.0f , 0.7f , false );
                                }
                            } else if ( input.equalsIgnoreCase ( "removewaypoint" ) ) {
                                final String name = this.inputStream.readUTF ( );
                                Phobos.waypointManager.waypoints.remove ( name );
                                Command.sendMessage ( "\u00A7c[PhobosChat] \u00A7r" + name + " has removed their waypoint" );
                                if ( PhobosChat.INSTANCE.ding.getValue ( ) ) {
                                    Util.mc.world.playSound ( Util.mc.player.posX , Util.mc.player.posY , Util.mc.player.posZ , SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP , SoundCategory.PLAYERS , 1.0f , - 0.7f , false );
                                }
                            } else if ( input.equalsIgnoreCase ( "inventory" ) ) {
                                final String name = this.inputStream.readUTF ( );
                                final byte[] inventory = readByteArrayLWithLength ( this.inputStream );
                                for (final String player : PhobosChat.phobosUsers) {
                                    if ( player.equalsIgnoreCase ( name ) ) {
                                        Phobos.inventoryManager.inventories.put ( player , PhobosChat.deserializeInventory ( inventory ) );
                                    }
                                }
                            } else if ( input.equalsIgnoreCase ( "users" ) ) {
                                final byte[] inputBytes = readByteArrayLWithLength ( this.inputStream );
                                assert inputBytes != null;
                                final ObjectInputStream stream = new ObjectInputStream ( new ByteArrayInputStream ( inputBytes ) );
                                final List < String > players = (List < String >) stream.readObject ( );
                                Command.sendMessage ( "\u00A7c[PhobosChat]\u00A7r Active Users:" );
                                for (final String name2 : players) {
                                    Command.sendMessage ( name2 );
                                    if ( ! PhobosChat.phobosUsers.contains ( name2 ) ) {
                                        PhobosChat.phobosUsers.add ( name2 );
                                    }
                                }
                            }
                            PhobosChat.INSTANCE.status = ! this.socket.isClosed ( );
                        }
                    }
                    /*catch (IOException | ClassNotFoundException ex2) {
                        final Exception ex = new Exception();
                        //final Exception ex;
                        final Exception e = ex;
                        e.printStackTrace();
                        continue Label_0005_Outer;
                    }*/ catch ( IOException | ClassNotFoundException e ) {
                        e.printStackTrace ( );
                    }
                }
            }
        }
    }
}
