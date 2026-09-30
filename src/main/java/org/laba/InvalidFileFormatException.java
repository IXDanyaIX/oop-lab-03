package org.laba;

public class InvalidFileFormatException extends Exception {
    private final int lineNumber;


    public InvalidFileFormatException(String message, int lineNumber) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (строка " + lineNumber + ")";
    }

}
