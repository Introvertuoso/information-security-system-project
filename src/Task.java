import javax.xml.validation.SchemaFactoryConfigurationError;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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

    public Pair<String, Certificate> execute(Certificate certificate) { // Execute should become in ConnectionHandler to allow navigation
        Logger.log("Executing task...");
        StringBuilder temp = new StringBuilder();
        String title = generateTitle();
        temp.append(title.length() > 20 ? title.substring(0, 19) : title.toUpperCase())
                .append(title.equals("") ? "" : Logger.LINE);
        switch (action) {
            case "quit":

            case"exit" :
                Logger.log("Connection termination requested.");
                temp.append(Logger.TERMINATE);
                break;

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

                    try {
                        if (!isAuthorized(certificate)) {
                            Logger.log("Access denied.");
                            temp.append(Logger.FAILURE);

                        } else {
                            authorize(certificate);
                            FileWriter writer = new FileWriter(this.filepath);
                            writer.write(this.newFileContent);
                            writer.close();
                            temp.append(Logger.SUCCESS);
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
        return new Pair<>(temp.toString(), certificate);
    }

    private boolean isAuthorized(Certificate certificate) throws IOException {
        Logger.log("Checking authorization...");
        boolean res = false;

        String[] lines = certificate.getCsr().getExtras().split("\\|");

        for (String s : lines) {
            if (s.equals(this.filepath)) {
                res = true;
                break;
            }
        }

        if(!new File(this.filepath).exists())
            res = true;

        return res;
    }

    private void authorize(Certificate certificate) {
        if(new File(this.filepath).exists())
            return;

        Logger.log("Authorizing user...");
        String delimiter = "|";
        String temp = certificate.getCsr().getExtras();
        if(temp.equals(""))
            delimiter = "";

        certificate.getCsr().setExtras(temp + delimiter + this.filepath);
    }

    private String generateTitle() {
        String res = "";

        if (action.equals("list")) {
            res = action.toUpperCase();
        }

        else if (action.equals("read") || action.equals("write")) {
            return String.join(
                    " ",
                    action,
                    filepath.substring(filepath.lastIndexOf("\\") + 1)
            ).toUpperCase();
        }

        return res;
    }
}