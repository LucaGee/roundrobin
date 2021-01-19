package csciproject;

/**
 *
 * @author Luca Gallagher
 */
public class Process {
    int pid;
    int burst;
    int arrive;
    int waitTime = 0;
    int finishTime = 0;
    int remainingBurst;
    
    public Process(int pid, int burst, int arrive) {//creates process object
        this.pid = pid;
        this.burst = burst;
        this.arrive = arrive;
        this.remainingBurst = burst;
    }
    
    public int getRemBurst() {
        return this.remainingBurst;
    }
    
    public void run(int timeQ) {
        remainingBurst = remainingBurst - timeQ;
    }
    
    public boolean isFinished() {
        if(remainingBurst <= 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getPID() {
        return this.pid;
    }
    
    public int getBurst() {
        return this.burst;
    }
    
    public int getArrive() {
        return this.arrive;
    }
    
    public int getWaitTime() {
        return this.waitTime;
    }
    
    public int getFinishTime() {
        return this.finishTime;
    }
    
    public void setFinishTime(int time) {
        finishTime = time;
    }
    
    public void addWaitTime(int time) {
        waitTime += time;
    }
}
