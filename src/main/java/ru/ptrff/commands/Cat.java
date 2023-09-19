package ru.ptrff.commands;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cat extends Command {
    @Override
    public boolean isCommand(String command) {
        return command.startsWith("cat");
    }

    @Override
    public FileObject perform(String command, FileObject fileSystem) throws FileSystemException {
        String[] args = command.split(" ");
        if (args.length == 1) {
            System.out.println();
            return fileSystem;
        }

        for (FileObject child : fileSystem.getChildren()) {
            if (child.getName().getBaseName().equals(args[1])) {
                if (child.isFolder()) {
                    System.out.println("cat: " + child.getName().getBaseName() + "/: Is a directory");

                    return fileSystem;
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(child.getContent().getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return fileSystem;
            }
        }

        System.out.println("cat: " + args[1] + ": No such file or directory");

        return super.perform(command, fileSystem);
    }
}
