package ru.ptrff;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import ru.ptrff.commands.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private FileObject currentPath;
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    private Command[] commands = new Command[]{
            new Ls(), new Cat(), new Cd(), new Pwd()
    };

    public FileObject openFileSystem(String path) throws FileSystemException {
        StandardFileSystemManager manager = new StandardFileSystemManager();
        manager.init();

        String jarPath = System.getProperty("user.dir");
        String baseUri = "zip:file://" + jarPath;

        return manager.resolveFile(baseUri + "/" + path);
    }

    public void start(String file) throws Exception {
        currentPath = openFileSystem(file);

        String command = "";
        Scanner scanner = new Scanner(System.in);
        while (!command.equals("exit")) {
            System.out.print(GREEN + "user@" + RESET + ":" +BLUE+ currentPath.getName().getBaseName()+RESET + "$ ");
            command = scanner.nextLine();

            command = command.replaceAll("^\\s+", "");

            boolean performed = false;
            for (Command cmd : commands) {
                if (cmd.isCommand(command)) {
                    currentPath = cmd.perform(command, currentPath);
                    performed = true;
                    break;
                }
            }

            if (!performed) {
                System.out.println(command + ": " + "command not found");
            }
        }
    }

    public void test(String file) throws FileSystemException {
        currentPath = openFileSystem(file);
        String[] textCommands = {
                "ls",
                "cd fol/der",
                "ls",
                "cat hype.txt",
                "pwd",
                "cd ../..",
                "ls"
        };
        performList(Arrays.asList(textCommands));
    }

    public static List<String> getTextCommandsFromFile(String filePath) {
        List<String> textCommands = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                textCommands.add(line);
            }
            reader.close();
            return textCommands;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void testScript(String file, String script) throws FileSystemException {
        currentPath = openFileSystem(file);
        performList(getTextCommandsFromFile(script));
    }

    private void performList(List<String> textCommands) throws FileSystemException {
        for (String textCmd: textCommands) {
            System.out.println(GREEN + "user@" + RESET + ":" +BLUE+ currentPath.getName().getBaseName()+RESET + "$ "+textCmd);
            for (Command cmd : commands) {
                if (cmd.isCommand(textCmd)) {
                    currentPath = cmd.perform(textCmd, currentPath);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        if(args.length>1){
            if(args[1].equals("--test")){
                main.test(args[0]);
            }else if(args[1].equals("--script") && args.length==3) {
                main.testScript(args[0], args[2]);
            }
        }else {
            main.start(args[0]);
        }
    }
}