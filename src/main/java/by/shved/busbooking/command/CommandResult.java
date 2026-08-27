package by.shved.busbooking.command;

public class CommandResult {
    private final String path;
    private final boolean redirect;

    private CommandResult(String path, boolean redirect) {
        this.path = path;
        this.redirect = redirect;
    }

    public static CommandResult forward(String path) {
        return new CommandResult(path, false);
    }

    public static CommandResult redirect(String path) {
        return new CommandResult(path, true);
    }

    public String getPath() {
        return path;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
