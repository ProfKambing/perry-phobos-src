package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.Command;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public
class OpenFolderCommand
        extends Command {
    public
    OpenFolderCommand ( ) {
        super ( "openfolder" , new String[0] );
    }

    @Override
    public
    void execute ( String[] commands ) {
        try {
            Desktop.getDesktop ( ).open ( new File ( "phobos/" ) );
            Command.sendMessage ( "Opened config folder!" , true );
        } catch ( IOException e ) {
            Command.sendMessage ( "Could not open config folder!" , true );
            e.printStackTrace ( );
        }
    }
}