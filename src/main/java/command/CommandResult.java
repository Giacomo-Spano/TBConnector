package command;


public class CommandResult {

    private final long code;
    private final String result;

    public CommandResult(long code, String result) {
        this.code = code;
        this.result = result;
    }

    public long getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }
}
