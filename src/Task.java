import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Task {
    private String filepath;
    private String action; // Supported actions are list, read, write, navigate?
    private String newFileContent;

    public Task(String filepath, String action, String newFileContent) {
        this.filepath = filepath;
        this.action = action;
        this.newFileContent = newFileContent;
    }

    public String execute() { // Execute should become in ConnectionHandler to allow navigation
        Logger.log("Executing task...");
        StringBuilder temp = new StringBuilder();
        switch (action) {
            case "list":
                if (!this.filepath.equals("") || !this.newFileContent.equals("")) {
                    Logger.log("Failed" + "\n");
                    temp.append(Logger.FAILURE);
                } else {
                    File dir = new File(ConnectionHandler.currentDirectory);
                    if (!dir.isDirectory()) {
                        Logger.log("Failed" + "\n");
                        temp.append(Logger.FAILURE);
                    }
                    else {
                        File[] files = dir.listFiles();
                        if (files == null || files.length == 0) {
                            temp.append(Logger.FAILURE);
                            Logger.log("Failed" + "\n");
                        }
                        else {
                            for (File f : files) {
                                temp.append(f.getPath()).append("\n");
                            }
                            Logger.log("Done" + "\n");
                        }
                    }
                }
                break;
            case "read":
                if (!this.newFileContent.equals("")) {
                    temp.append(Logger.FAILURE);
                    Logger.log("Failed" + "\n");
                } else {
                    File file = new File(this.filepath);
                    if (!file.exists()) {
                        Logger.log("Failed" + "\n");
                        temp.append(Logger.FAILURE);
                    }
                    else {
                        try {
                            List<String> lines = Files.readAllLines(Path.of(this.filepath));
                            for (String s : lines) {
                                temp.append(s).append("\n");
                            }
                            Logger.log("Done" + "\n");
                        } catch (IOException e) {
                            temp.append(Logger.FAILURE);
                            Logger.log("Failed" + "\n");
                        }
                    }
                }
                break;
            case "write":
                if (this.newFileContent.equals("")) {
                    temp.append(Logger.SUCCESS);
                    Logger.log("Done" + "\n");
                } else {
                    File file = new File(this.filepath);
                    if (!file.exists()) {
                        Logger.log("Failed" + "\n");
                        temp.append(Logger.FAILURE);
                    }
                    else {
                        try {
                            FileWriter writer = new FileWriter(this.filepath);
                            writer.write(this.newFileContent);
                            writer.close();
                            temp.append(Logger.SUCCESS);
                            Logger.log("Done" + "\n");
                        } catch (IOException e) {
                            Logger.log("Failed" + "\n");
                            temp.append(Logger.FAILURE);
                        }
                    }
                }
                break;
            default:
                temp.append(Logger.FAILURE);
                Logger.log("Failed" + "\n");
        }
        return temp.toString();
    }
}