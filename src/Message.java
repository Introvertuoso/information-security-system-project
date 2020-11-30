public class Message {

    private Task task;
    private Certificate certificate;
    private String data;

    public Message(String data) {
        this.task = null;
        this.certificate = null;
        this.data = data;
    }
    public Message(Task task, Certificate certificate) {
        this.task = task;
        this.certificate = certificate;
        this.data = null;
    }

    // data -> task and certificate
    public void unpackData() {
        System.out.print("Unpacking data...");
        if (this.data == null) {
            System.out.println("Failed");
        }
        else {
            String[] temp = data.split("\0");
            if (temp.length != 2) {
                System.out.println("Failed");
            }
            else {
                String[] taskTemp = temp[0].split(" ", -1);
                // TODO: [JAWAD] Properly unpack certificate string for construction.
                String[] certificateTemp = temp[1].split("\0");
                if (taskTemp.length != 3) {
                    System.out.println("Failed");
                } else {
                    this.task = new Task(taskTemp[0], taskTemp[1], taskTemp[2]);
                    System.out.println("Done");
                }
                if (certificateTemp.length != 1) {
                    System.out.println("Failed");
                } else {
                    this.certificate = new Certificate(certificateTemp[0]);
                    System.out.println("Done");
                }
            }
        }
    }

    // task and certificate -> data
    public void packData() {
        System.out.print("Packing data...");
        if (this.task == null || this.certificate == null) {
            System.out.println("Failed");
        }
        else {
            String[] temp = new String[2];
            temp[0] = this.task.toString();
            temp[1] = this.certificate.toString();
            data = String.join("\0", temp);
            System.out.println("Done");
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
}
