//TODO: [JAWAD] Your code here
public class Task {
    private String filepath;
    private String action; // Supported actions are list, read, write, navigate?
    private String newFileContent;

    public Task(String filepath, String action, String newFileContent) {
        this.filepath = filepath;
        this.action = action;
        this.newFileContent = newFileContent;
    }

    public void execute() { // Execute should become in ConnectionHandler to allow navigation

    }
}