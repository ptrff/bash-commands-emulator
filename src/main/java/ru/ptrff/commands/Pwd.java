package ru.ptrff.commands;

import org.apache.commons.vfs2.FileObject;

public class Pwd extends Command{

    @Override
    public boolean isCommand(String command) {
        return command.startsWith("pwd");
    }

    @Override
    public FileObject perform(String command, FileObject fileSystem) {
        System.out.println(fileSystem.getName().getPath());
        return fileSystem;
    }
}
