package client;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandHelper {

    public String getArgValue(String[] args, String flag){
        int index = Arrays.asList(args).indexOf(flag);
        return args[index+1];
    }

    public String getCommand(String[] args){
        return args[0];
    }
}
