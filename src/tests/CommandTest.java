package tests;

import client.CommandHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandTest {

    private CommandHelper helper;


    public CommandTest() {
        this.helper = new CommandHelper();
    }

    @Test
    void demoTestMethod() {
        String input = "print -f filename.pdf -p 303A";
        String[] args = input.split(" ");

        String command = args[0];
        String fileName = helper.getArgValue(args,"-f");
        String printerName = helper.getArgValue(args,"-p");

        assertEquals("print", command);
        assertEquals("filename.pdf", fileName);
        assertEquals("303A", printerName);
    }

}
