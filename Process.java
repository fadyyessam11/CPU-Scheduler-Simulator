/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package assignment.pkg3.os;

/**
 *
 * @author admin
 */
//import com.sun.javafx.scene.shape.PathUtils;
import java.awt.Color;

public class Process{
    private String ProcessName;
    private int ArrivalTime;
    private int BurstTime;
    private int remainingTime;
    private int waitingTime;
    private int turnAroundTime;
    private int priority;
    private int FinishTime;
    private boolean isFinished;
    private Color color;
    private int StartTime;

    public Process(){}
    public Process(String ProcessName, int ArrivalTime, int BurstTime, int priority, Color color) {
        this.ProcessName = ProcessName;
        this.ArrivalTime = ArrivalTime;
        this.BurstTime = BurstTime;
        this.isFinished = false;
        this.color = color;
        this.priority = priority;
        this.remainingTime = this.BurstTime;
    }


    public void Set_ProcessName(String Processname) {
        this.ProcessName = Processname;
    }

    public void Set_AT(int arrivalTime) {
        this.ArrivalTime = arrivalTime;
    }

    public void Set_BT(int BurstTime) {
        this.BurstTime = BurstTime;
    }

    public void SetRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }


    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnAround(int turnAround) {
        this.turnAroundTime = turnAround;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setFinishTime(int FinishTime) {
        this.FinishTime = FinishTime;
    }

    public void setStartTime(int StartTime) {
        this.StartTime = StartTime;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public String getName() {
        return ProcessName;
    }

    public int getArrivalTime() {
        return ArrivalTime;
    }

    public int getBurstTime() {
        return BurstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
    public int getFinishTime() {
        return FinishTime;
    }
    public int getStartTime() {
        return StartTime;
    }

    public int get_wt_time() {
        return waitingTime;
    }

    public int get_TurnAround_time() {
        return turnAroundTime;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getCompleted() {
        return isFinished;
    }

    public Color getColor(){
        return this.color;
    }

}
