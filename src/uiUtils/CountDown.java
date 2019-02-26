package uiUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableValue;

import java.util.Observable;
import java.util.Scanner;

public class CountDown implements Runnable{

    private long upperLimit;
    private long currentCount;
    private long interval;

    private Thread threadCounter;

    public CountDown(int seconds) {
        this.interval = 1000;
        this.upperLimit = seconds * interval;
        this.threadCounter = new Thread(this);

    }

    public void restart(){
        currentCount = 0;
    }

    public void stop(){

    }

    public void setCountdown (long upperLimit){
        this.upperLimit = upperLimit;
    }

    public long getCurrentCount(){
        return currentCount;
    }

    @Override
    public void run() {
        for (currentCount=0; currentCount<upperLimit; currentCount+=interval){
        //for(;;){
            try {
                Thread.sleep(interval);
                //System.out.println(currentCount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void begin(){
        threadCounter.start();
    }

    public static void main(String[] args) {
        CountDown countDown = new CountDown(5);
        countDown.begin();
        Scanner reader = new Scanner(System.in);
        while(!reader.nextLine().isEmpty()){
            countDown.restart();
        }
    }

}
