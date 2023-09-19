package ru.ptrff.commands;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import static ru.ptrff.Main.RED;
import static ru.ptrff.Main.RESET;

public class Ls extends Command{
    @Override
    public boolean isCommand(String command) {
        return command.startsWith("ls");
    }

    @Override
    public FileObject perform(String command, FileObject fileSystem) throws FileSystemException {
        String[] args = command.split(" ");
        if (args.length == 1) {
            for (FileObject child : fileSystem.getChildren()) {
                if (child.isFolder()) {
                    System.out.println(RED + child.getName().getBaseName() + RESET);
                } else {
                    System.out.println(child.getName().getBaseName());
                }
            }
        }
        return fileSystem;
    }

}
