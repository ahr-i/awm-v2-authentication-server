package com.example.authenticationserver.TestFile;

public class TestThread {


    public static void main(String[] args) {
        Runnable runnable = () -> {

            for (int i=0; i<5;i++){
                Sleep(3000);
                System.out.println("Hello World "+i);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void Sleep(int m){

        try {

            Thread.sleep(m);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
