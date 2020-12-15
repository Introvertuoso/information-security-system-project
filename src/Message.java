public class Message {

    private Task task;
    private String signature;
    private Certificate certificate;
    private String data;

    public Message(String data) {
        this.task = null;
        this.signature = null;
        this.certificate = null;
        this.data = data;
    }
    public Message(Task task, Certificate certificate, String signature) {
        this.task = task;
        this.signature = signature;
        this.certificate = certificate;
        this.data = null;
    } 

    // data -> task, certificate and signature
    public void unpackData() {
        Logger.log("Unpacking data...");
        if (this.data == null) {
            Logger.log("Data cannot be null.");
        }
        else {
            String[] temp = data.split("\0");
            if (temp.length != 3) { // Slots
                Logger.log("Data is corrupt.");
            }
            else {
                String[] taskTemp = temp[0].split(" ", 3);
                String[] signatureTemp = temp[1].split("\0"); // unnecessary split
                String[] certificateTemp = temp[2].split("\0");

                if (taskTemp.length != 3) {
                    Logger.log("Task is corrupt.");
                } else {
                    this.task = new Task(taskTemp[0], taskTemp[1], taskTemp[2]);
                }

                if (signatureTemp.length != 1) {
                    Logger.log("Signature is corrupt.");
                } else {
                    this.signature = signatureTemp[0];
                }

                if (certificateTemp.length != 1) {
                    Logger.log("Certificate is corrupt.");
                } else {
                    this.certificate = new Certificate(certificateTemp[0]);
                }

            }
        }
    }

    // task, certificate and signature -> data
    public void packData() {
        Logger.log("Packing data...");
        if (this.task == null || this.certificate == null || this.signature == null) {
            Logger.log("Task, Certificate and Signature cannot be null.");
        }
        else {
            String[] temp = new String[3]; // Slots
            temp[0] = this.task.toString();
            temp[1] = this.signature;
            temp[2] = this.certificate.toString();
            data = String.join("\0", temp);
        }
    }

    public Task getTask() {
        return task;
    }
    public void setTask(Task task) {
        this.task = task;
    }
    public Certificate getCertificate() {
        return certificate;
    }
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
