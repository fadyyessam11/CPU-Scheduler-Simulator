/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package assignment.pkg3.os;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.JLabel;

/**
 *
 * @author admin
 */
public class Priority {
    double AvgWaitingTime = 0;
    double AvgTurnAround = 0;
    static int Timer = 0;
    ArrayList<Process> processes = new ArrayList<>();
    ArrayList<Process> readyQueue = new ArrayList<>();
    ArrayList<Process> processes2 = new ArrayList<>();
    ArrayList<Process> FinishedProcesses = new ArrayList<>();
    private int ContextSwitching;
    private Process PreviousProcess;
    private GUI gui;

    public Priority() {
    }

    public Priority(int ContextSwitching) {
        this.ContextSwitching = ContextSwitching;
    }

    public void AddProcess(Process process){
        processes.add(process);
    }

    public ArrayList<Process> getProcesses(){return this.processes; };

    public void Aging(){
        for (int i = 1; i < processes.size(); i++) {
            if (processes.get(i).getPriority() > 1)
                processes.get(i).setPriority(processes.get(i).getPriority() - 1);
        }
    }
    public void AddProcessToReadyQueue(){
        readyQueue.clear();
        for(int i = 0; i < processes.size(); i++){
            if(processes.get(i).getArrivalTime() <= Timer && processes.get(i).getCompleted() == false){
                readyQueue.add(processes.get(i));
            }
            readyQueue.sort(Comparator.comparing(Process::getPriority));

        }
    }
    int GetProcessIndex(String ProcessName) {
        int index = 0;
        for (int i = 0; i < processes2.size(); i++) {
            if(processes2.get(i).getName() == ProcessName)
                return i;
        }
        return index;
    }
    private void AddContextTime(Process currProcess, int currentTime, int contextSwitching) {
        // TODO Auto-generated method stub
        Process p = new Process();
        p.setColor(Color.BLACK);
        gui.AddColor( Timer , GetProcessIndex(currProcess.getName()) , p.getColor(), ContextSwitching);
    }

    public void PriorityScheduling(){
        gui = new GUI(processes);
        for(int i = 0; i < processes.size(); i++){
            processes2.add(processes.get(i));
        }

        // Sort processes by Arrival time and priority
        processes.sort(Comparator.comparing(Process::getArrivalTime).thenComparingInt(Process::getPriority));

        Timer = processes.get(0).getArrivalTime();
        int TurnAroundTime;
        int WaitingTime;
        int Finished_Processes = 0;


        while(true){
            AddProcessToReadyQueue();

            if(!readyQueue.isEmpty()){
                //To add context switching time and block
                if(Timer > processes.get(0).getArrivalTime()){
                    if(readyQueue.get(0).getName() != PreviousProcess.getName()){
                        Timer += ContextSwitching;
                        AddProcessToReadyQueue();
                        System.out.println("current process = " + readyQueue.get(0).getName());
                        System.out.println("process index = " + GetProcessIndex(readyQueue.get(0).getName()));
                        AddContextTime(readyQueue.get(0), Timer, ContextSwitching);
                    }
                }

                readyQueue.get(0).setStartTime(Timer);
                // add blocks of proccess to the gui
                for(int i =0 ; i <readyQueue.get(0).getBurstTime(); i++){
                    gui.AddColor( Timer+1 , GetProcessIndex(readyQueue.get(0).getName()) , readyQueue.get(0).getColor());
                    Timer++;
                }
                //Timer += readyQueue.get(0).getBurstTime();
                readyQueue.get(0).setFinishTime(Timer);

                TurnAroundTime = readyQueue.get(0).getFinishTime() - readyQueue.get(0).getArrivalTime();
                readyQueue.get(0).setTurnAround(TurnAroundTime);
                WaitingTime = readyQueue.get(0).get_TurnAround_time() - readyQueue.get(0).getBurstTime();
                readyQueue.get(0).setWaitingTime(WaitingTime);

                AvgWaitingTime += readyQueue.get(0).get_wt_time();
                AvgTurnAround += readyQueue.get(0).get_TurnAround_time();

                FinishedProcesses.add(readyQueue.get(0));
                readyQueue.get(0).setIsFinished(true);
                Finished_Processes++;

                // to prevent starvation
                Aging();

                PreviousProcess = readyQueue.get(0);
                System.out.println("aT Time " + readyQueue.get(0).getStartTime()+ " P_ID---> "+ readyQueue.get(0).getName() +" will be execued");

            }
            else if (Finished_Processes != processes.size())
                while(processes.get(Finished_Processes).getArrivalTime() != Timer){
                    Timer++;
                    gui.AddColor( Timer , GetProcessIndex(processes.get(Finished_Processes).getName()) , Color.WHITE);
                }
            else
                break;
        }
    }

    public void Print(){
        AvgWaitingTime = AvgWaitingTime / FinishedProcesses.size();
        AvgTurnAround = AvgTurnAround / FinishedProcesses.size();
        System.out.println("  PROCESS    AT     BT       WT     CT     TurnAround     ");
        for(int i = 0; i < FinishedProcesses.size(); i++)
        {
            System.out.println("    " + FinishedProcesses.get(i).getName() + "       " +
                    FinishedProcesses.get(i).getArrivalTime() + "      " +
                    FinishedProcesses.get(i).getBurstTime() + "        " + FinishedProcesses.get(i).get_wt_time()
                    + "      " + FinishedProcesses.get(i).getFinishTime() + "       " +
                    FinishedProcesses.get(i).get_TurnAround_time());
        }

        System.out.println("----------------------------------------");
        System.out.println("Average waiting time = " + AvgWaitingTime + "\n-----------------------");


        System.out.println("---------------------");
        System.out.println("Average turn around time = " + AvgTurnAround + "\n------------------");
    }
}
