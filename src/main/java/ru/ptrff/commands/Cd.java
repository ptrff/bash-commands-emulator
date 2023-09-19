package ru.ptrff.commands;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

public class Cd extends Command{

    @Override
    public boolean isCommand(String command) {
        return command.startsWith("cd");
    }

    @Override
    public FileObject perform(String command, FileObject fileSystem) throws FileSystemException {
        String[] args = command.split(" ");
        if (args.length == 1) {
            return fileSystem;
        }

        String[] pathComponents = args[1].split("/");

        // gobacks
        if (pathComponents[0].equals("..")) {
            FileObject currentDir = fileSystem;
            for (int i = 0; i < pathComponents.length && currentDir.getParent() != null; i++) {
                currentDir = currentDir.getParent();
            }
            return currentDir;
        }

        // goforwards
        FileObject currentDir = fileSystem;
        for (String dirName : pathComponents) {
            boolean found = false;
            for (FileObject child : currentDir.getChildren()) {
                if (child.getName().getBaseName().equals(dirName)) {
                    if (child.isFolder()) {
                        currentDir = child;
                        found = true;
                        break;
                    } else {
                        System.out.println("bash: cd: " + args[1] + ": Not a directory");
                        return fileSystem;
                    }
                }
            }
            if (!found) {
                System.out.println("bash: cd: " + dirName + ": No such file or directory");
                return fileSystem;
            }
        }

        return currentDir;
    }


}
