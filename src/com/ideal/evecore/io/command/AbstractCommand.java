package com.ideal.evecore.io.command;

/**
 * Created by chris on 07/04/2017.
 */
public abstract class AbstractCommand {
    protected final String command;

    protected AbstractCommand(String cmd){
        command = cmd;
    }
}
