import java.io.*;
import java.util.*;

public class SimpleShell {
    public static void main(String[] args) throws IOException {
        //this is the list of objects of all our commands each object with it's own list
        List<CommandObject> commandHistory = new ArrayList<CommandObject>();

        //immediately determines type of OS
        String OSName = (System.getProperty("os.name")).toLowerCase();

        //creates a variable called isWindows and if the OSname is identified as windows then the boolean is set to true
        Boolean isWindows = false;
        if (OSName.contains("windows")) {
            isWindows = true;
        }

        File currentDirectory = new File(System.getProperty("user.home"));
        //initializes an instance of ProcessBuilder class

        String commandLine;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        ProcessBuilder pb = new ProcessBuilder();


        while (true) {
            System.out.print("jsh> ");
            commandLine = console.readLine();

            //loops again if no input is entered by user
            if (commandLine.equals("")) {
                continue;
            }

            //creates a new instance of CommandObject and passes what the user command to the class then the object is saved to the commandHistoryList as well as if this command was executed on windows

            try {
                CommandObject newCommand = new CommandObject(isWindows, commandLine);


                //executes last command entered
                if (newCommand.commandParsed.get(newCommand.commandParsed.size() - 1).equals("!!")) {
                    int indexOfLastCommand = commandHistory.size() - 1;
                    StringBuffer commandExtractor = new StringBuffer();
                    if (isWindows == true) {
                        for (int i = 2; i < (commandHistory.get(indexOfLastCommand).commandParsed).size(); i++) {
                            commandExtractor.append(commandHistory.get(indexOfLastCommand).commandParsed.get(i));
                        }
                    } else {
                        for (int i = 0; i < (commandHistory.get(indexOfLastCommand).commandParsed).size(); i++) {
                            commandExtractor.append(commandHistory.get(indexOfLastCommand).commandParsed.get(i));
                        }
                    }
                    String extracteCcommand = String.valueOf(commandExtractor);
                    CommandObject executeLastComman = new CommandObject(isWindows, extracteCcommand);
                    newCommand = executeLastComman;
                }
                //executes the previous command by the index of the command the user enter
                if (newCommand.commandParsed.get(newCommand.commandParsed.size() - 1).matches("!" + "\\d+")) {
                    String commandParser = newCommand.commandParsed.get(newCommand.commandParsed.size() - 1);
                    int commandIndex = Integer.parseInt(commandParser.substring(1));
                    //if the machine is a windows machine this will execute
                    if (commandIndex <= commandHistory.size() - 1 && isWindows == true) {
                        String retreivedCommand = commandHistory.get(commandIndex - 1).toString().substring(11);
                        CommandObject previousCommand = new CommandObject(isWindows, retreivedCommand);
                        newCommand = previousCommand;
                    }
                    //Unix based machines will execute this
                    else if (commandIndex <= commandHistory.size() - 1) {
                        String retreivedCommand = commandHistory.get(commandIndex - 1).toString();
                        CommandObject previousCommand = new CommandObject(isWindows, retreivedCommand);
                        newCommand = previousCommand;
                    }
                    //only executes if the user enters a history index out of bounds
                    else {
                        System.out.println("Error, this command index does not exist");
                    }
                }

                //lists history of all executed commands
                else if (newCommand.commandParsed.get(newCommand.commandParsed.size() - 1).equals("history")) {
                    for (int i = 0; i < commandHistory.size(); i++) {
                        System.out.println(i + " " + commandHistory.get(i).toString());
                    }
                }

                //change directory
                if (newCommand.toString().contains("cd")) {

                    //this checks to see if the user only entered cd without specifying a directory then we will change the current working directory to the user’s home directory
                    if (newCommand.commandParsed.get(newCommand.commandParsed.size() - 1).equals("cd")) {
                        currentDirectory = new File(System.getProperty("user.home"));
                        pb.directory(currentDirectory);


                        //if the user entered a directory to traverse
                    } else {
                        String newDirectory = newCommand.commandParsed.get(newCommand.commandParsed.size() - 1);
                        currentDirectory = new File(pb.directory() + File.separator + newDirectory);
                        if (currentDirectory.isDirectory()) {
                            pb.directory(currentDirectory);
                            System.out.println(currentDirectory);
                        }
                        //if the directory does not exist then directory not found will be returned
                        else {
                            System.out.println("Directory not found");
                            continue;
                        }
                    }
                }
                //adds the command to the history list
                commandHistory.add(newCommand);


                // args[0] is the command that is run in a separate process
                pb.command(newCommand.commandParsed);
                Process process = pb.start();
                // obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                // read the output of the process
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);
                br.close();
            } catch (IOException e) {
                System.out.println("Error, Command does not exist");
            }
        }
    }
}


