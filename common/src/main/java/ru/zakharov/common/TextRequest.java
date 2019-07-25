package ru.zakharov.common;

public class TextRequest extends AbstractMessage{

    private String command;

    public TextRequest(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
