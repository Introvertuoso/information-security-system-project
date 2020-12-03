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

    public String execute(String key) { // Execute should become in ConnectionHandler to allow navigation
        Logger.log("Executing task...");
        StringBuilder temp = new StringBuilder();
        switch (action) {
            case "list":
                if (!this.filepath.equals("") || !this.newFileContent.equals("")) {
                    Logger.log("Command is malformed");
                    temp.append(Logger.FAILURE);
                } else {
                    File dir = new File(ConnectionHandler.currentDirectory);
                    if (!dir.isDirectory()) {
                        Logger.log("Root is invalid.");
                        temp.append(Logger.FAILURE);
                    } else {
                        File[] files = dir.listFiles();
                        if (files == null || files.length == 0) {
                            temp.append("Root is empty.");
                            Logger.log("Root is empty.");
                        } else {
                            for (File f : files) {
                                temp.append(f.getPath()).append("\n");
                            }
                        }
                    }
                }
                break;
            case "read":
                if (!this.newFileContent.equals("")) {
                    temp.append(Logger.FAILURE);
                    Logger.log("Command is malformed.");
                } else {
                    File file = new File(this.filepath);
                    if (!file.exists()) {
                        Logger.log("Invalid filepath");
                        temp.append(Logger.FAILURE);
                    } else {
                        try {
                            List<String> lines = Files.readAllLines(Path.of(this.filepath));
                            for (String s : lines) {
                                temp.append(s).append("\n");
                            }
                        } catch (IOException e) {
                            temp.append(Logger.FAILURE);
                            Logger.log(e.getMessage());
                        }
                    }
                }
                break;
            case "write":
                if (this.newFileContent.equals("")) {
                    temp.append(Logger.SUCCESS);

                } else {
                    File file = new File(this.filepath);
                    int compatibility;

                    try {
                        compatibility = isCompatible(key);

                        if (compatibility < 0) {
                            Logger.log("Access denied.");
                            temp.append(Logger.FAILURE);
                        } else {
                            try {
                                if (compatibility == 0) {
                                    registerFileOwnerPair(key);
                                }
                                FileWriter writer = new FileWriter(this.filepath);
                                writer.write(this.newFileContent);
                                writer.close();
                                temp.append(Logger.SUCCESS);

                            } catch (IOException e) {
                                Logger.log(e.getMessage());
                                temp.append(Logger.FAILURE);
                            }
                        }

                    } catch (Exception e) {
                        Logger.log(e.getMessage());
                        temp.append(Logger.FAILURE);
                    }
                }
                break;
            default:
                temp.append(Logger.FAILURE);
                Logger.log("Command is malformed.");
        }
        return temp.toString();
    }

    private int isCompatible(String key) throws IOException {
        Logger.log("Checking for compatibility...");
        int res = 0; // -1 = not the owner, 0 = doesn't exist, 1 = the owner
        File register = new File(Server.registerPath);

        if (!register.exists()) {
            return res;
        }

        List<String> lines = Files.readAllLines(Path.of(Server.registerPath));
        for (String s : lines) {
            String[] pathKey = s.split("\0");
            if (key == null) {
                res = 1; // giving the symmetric connection user full control
            } else {
                if (pathKey[0].equals(this.filepath)) {
                    res = -1;
                    if (pathKey[1].equals(key)) {
                        res *= -1;
                    }
                }
            }
        }

        return res;
    }

    private void registerFileOwnerPair(String key) throws IOException {
        Logger.log("Registering a new file...");
        FileWriter fileWriter = new FileWriter(Server.registerPath, true);
        String[] entry = new String[2];
        entry[0] = this.filepath;
        entry[1] = key + "\n";
        fileWriter.write(String.join("\0", entry));
        fileWriter.close();
    }
}