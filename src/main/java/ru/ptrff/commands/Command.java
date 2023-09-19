package ru.ptrff.commands;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class Command {
    public boolean isCommand(String command) {
        return false;
    }

    public FileObject perform(String command, FileObject fileSystem) throws FileSystemException {
        return fileSystem;
    }
}
