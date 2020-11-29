package fr.ubx.poo.engine;

public class Timer {
    private long lastTime;
    private double timeLeft;
    private boolean isRunning;

    public Timer(long creationTime, long duration) {
        timeLeft = duration * Math.pow(10, 9);
        this.lastTime = creationTime;
        isRunning = true;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void update(long now) {
        if(isRunning) {
            timeLeft += lastTime - now;
            lastTime = now;
            if(timeLeft <= 0) {
                isRunning = false;
                timeLeft = 0;
            }
        }
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public boolean getIsRunning() {
        return isRunning;
    }
}
