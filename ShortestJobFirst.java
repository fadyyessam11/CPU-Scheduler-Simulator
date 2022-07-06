/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package assignment.pkg3.os;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author admin
 */
public class ShortestJobFirst {
    double AvgWaitingTime = 0;
    double AvgTurnAround = 0;
    static int Timer = 0;
    ArrayList<Process> processes = new ArrayList<>();
    ArrayList<Process> processes2 = new ArrayList<>();
    ArrayList<Process> readyQueue = new ArrayList<>();
    ArrayList<Process> FinishedProcesses = new ArrayList<>();
    ArrayList<Process> RemainEdProcesses = new ArrayList<>();
    private Process PreviousProcess;
    private GUI gui;

    public ShortestJobFirst() {
    }
    public void AddProcess(Process process){
        processes.add(process);
    }

    public ArrayList<Process> getProcesses(){return this.processes; };
    public void AddProcessToReadyQueue(){
        readyQueue.clear();
        for(int i = 0; i < processes.size(); i++){
            if(processes.get(i).getArrivalTime() <= Timer && processes.get(i).getCompleted() == false){
                readyQueue.add(processes.get(i));
            }
            readyQueue.sort(Comparator.comparing(Process::getBurstTime));

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

    public void ShortestJobFirstScheduling(){
        gui = new GUI(processes);
        for(int i = 0; i < processes.size(); i++){
            processes2.add(processes.get(i));
        }
        // Sort processes by Arrival time and burst time
        processes.sort(Comparator.comparing(Process::getArrivalTime).thenComparingInt(Process::getBurstTime));
        for(int i = 0; i < processes.size(); i++){
            RemainEdProcesses.add(processes.get(i));
        }
        RemainEdProcesses.sort(Comparator.comparing(Process::getBurstTime));

        int VarShorterJobs = 0;
        int VarLargejob = RemainEdProcesses.get(RemainEdProcesses.size()-1).getBurstTime();

        Timer = processes.get(0).getArrivalTime();
        int TurnAroundTime;
        int WaitingTime;
        int Finished_Processes = 0;
        boolean added = false;

        while(true){
            AddProcessToReadyQueue();

            // TO PREVENT STARVATION
            if(RemainEdProcesses.size() > 0)
            {
                if(VarLargejob <= VarShorterJobs && processes.get(0).getName() != RemainEdProcesses.get(RemainEdProcesses.size()-1).getName()){
                    System.out.println("varShorterJobs = " + VarShorterJobs);
                    System.out.println("varLargerJobs = " + VarLargejob);
                    for(int i = 0; i < RemainEdProcesses.size(); i++){
                        if(RemainEdProcesses.get(i).getBurstTime() == VarLargejob){
                            readyQueue.clear();
                            int SIZE = RemainEdProcesses.size()-1;
                            readyQueue.add(RemainEdProcesses.get(SIZE));
                            break;
                        }
                    }

                    RemainEdProcesses.remove(RemainEdProcesses.size()-1);
                    if(RemainEdProcesses.size() > 0){
                        VarLargejob = RemainEdProcesses.get(RemainEdProcesses.size()-1).getBurstTime();
                    }
                }
            }
            if(!readyQueue.isEmpty()){
                readyQueue.get(0).setStartTime(Timer);
                // add blocks of proccess to the gui
                for(int i =0 ; i < readyQueue.get(0).getBurstTime(); i++){
                    //System.out.println("timer = " + Timer);
                    gui.AddColor( Timer+1 , GetProcessIndex(readyQueue.get(0).getName()) , readyQueue.get(0).getColor());
                    Timer++;
                }
                readyQueue.get(0).setFinishTime(Timer);

                TurnAroundTime = readyQueue.get(0).getFinishTime() - readyQueue.get(0).getArrivalTime();
                readyQueue.get(0).setTurnAround(TurnAroundTime);
                WaitingTime = readyQueue.get(0).get_TurnAround_time() - readyQueue.get(0).getBurstTime();
                readyQueue.get(0).setWaitingTime(WaitingTime);

                AvgWaitingTime += readyQueue.get(0).get_wt_time();
                AvgTurnAround += readyQueue.get(0).get_TurnAround_time();

                FinishedProcesses.add(readyQueue.get(0));
                readyQueue.get(0).setIsFinished(true);


                boolean check = true;
                int i;
                for(i = 0 ; i < RemainEdProcesses.size(); i++){
                    if(readyQueue.get(0).getName() == RemainEdProcesses.get(i).getName()){
                        check = false;
                        RemainEdProcesses.remove(RemainEdProcesses.get(i));
                    }
                }

                Finished_Processes++;
                if(check == false){
                    if(RemainEdProcesses.size() > 0 && added == false){
                        VarShorterJobs += FinishedProcesses.get(Finished_Processes-1).getBurstTime();
                        added = false;
                        System.out.println("found, VarShorterJobs = " + VarShorterJobs);
                    }
                }
                else{
                    if(RemainEdProcesses.size() > 0){
                        VarShorterJobs = RemainEdProcesses.get(0).getBurstTime();
                        added = true;
                        System.out.println("not found, VarShorterJobs = " + VarShorterJobs);
                    }
                }
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

        System.out.println("--------------------------------------------------------------------");
        System.out.println("Average waiting time = " + AvgWaitingTime + "\n-----------------------");


        System.out.println("---------------------");
        System.out.println("Average turn around time = " + AvgTurnAround + "\n------------------");
    }
}
