package com.nevergarden.myna.core;

public class MynaThread extends Thread {
    public boolean isRunning = false;
    private long loopRunTime = 0;

    private final Myna mina;
    public MynaThread(Myna myna) {
        this.mina = myna;
        this.isRunning = true;
    }
    public void run() {
        while(isRunning) {
            long loopStart = System.currentTimeMillis();
            int FPS = (1000 / 60);
            if (loopRunTime < FPS) {
                try {
                    this.mina.requestRender();
                    Thread.sleep(FPS - loopRunTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long loopEnd = System.currentTimeMillis();
            loopRunTime = ((loopEnd - loopStart));
            this.mina.update(loopRunTime);
        }
    }
}
