import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Task {
    private String action; // Supported actions are list, read, write, navigate?
    private String filepath;
    private String newFileContent;

    public Task(String action, String filepath, String newFileContent) {
        this.action = action;
        this.filepath = filepath;
        this.newFileContent = newFileContent;
    }
    public Task(String command) {
        StringBuilder res = new StringBuilder(command);
        String[] temp = command.split(" ", 3);
        if (temp.length < 3) {
            res.append(" ".repeat(3 - temp.length));
        }
        temp = res.toString().split(" ", 3);
        this.action = temp[0];
        this.filepath = temp[1];
        this.newFileContent = temp[2];
    }

    @Override
    public String toString() {
        String[] temp = new String[3];
        temp[0] = this.action;
        temp[1] = this.filepath;
        temp[2] = this.newFileContent;
        return String.join(" ", temp);
    }

    //    out.println(
//      connectionPolicy.connectionMethod.encrypt(
//          new Message(
//              new Task(scanner.nextLine()), new Certificate()
//          ).packData().getData()
//      )
//    )

    public String execute() { // Execute should become in ConnectionHandler to allow navigation
        Logger.log("Executing task...");
        StringBuilder temp = new StringBuilder();
        switch (action) {
            case "list":
                if (!this.filepath.equals("") || !this.newFileContent.equals("")) {
                    Logger.log("FailedList1" + "\n");
                    temp.append(Logger.FAILURE);
                } else {
                    File dir = new File(ConnectionHandler.currentDirectory);
                    if (!dir.isDirectory()) {
                        Logger.log("FailedList2" + "\n");
                        temp.append(Logger.FAILURE);
                    }
                    else {
                        File[] files = dir.listFiles();
                        if (files == null || files.length == 0) {
                            temp.append(Logger.FAILURE);
                            Logger.log("FailedList3" + "\n");
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
                    Logger.log("FailedRead1" + "\n");
                } else {
                    File file = new File(this.filepath);
                    if (!file.exists()) {
                        Logger.log("FailedRead2" + "\n");
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
                            Logger.log("FailedRead3" + "\n");
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
                        Logger.log("FailedWrite1" + "\n");
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
                            Logger.log("FailedWrite2" + "\n");
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