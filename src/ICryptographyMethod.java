interface ICryptographyMethod {
    String encrypt(Message message);

    Message decrypt(String data);

    void init();
}
