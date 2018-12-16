import java.util.*;

public class CommandObject {

    //Creates a new list for each command
    List<String>commandParsed = new ArrayList<String>();

    private String command;


    public CommandObject(boolean isWindows, String command){
        //if the command was executed on windows then an additional two commands are added to the String to be parsed
        if(isWindows == true){
            this.command = ("cmd.exe /c " + command);
            parseCommand(this.command);
        }
        else{
            this.command = command;
            parseCommand(this.command);
        }

    }

    public void parseCommand(String command) {

        //uses regular expression to breakdown the string into parts or commands and stores it into a string array for temporary use
        String[] breakDownOfCommand = command.split(" ");

        for(int i = 0; i < breakDownOfCommand.length; i++){
            commandParsed.add(breakDownOfCommand[i]);
        }
    }

    @Override
    public String toString(){
        return this.command;
    }


}
