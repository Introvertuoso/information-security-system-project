interface ICryptographyMethod {
    String encrypt(String message);

    String decrypt(String data);

    String encrypt(String message,String key);

    String decrypt(String data,String key);

    void init();
}
