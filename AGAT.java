import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AGAT extends ProcessAGAT {
    static ArrayList<ProcessAGAT> readyQueue = new ArrayList<ProcessAGAT>();  //Arrived
    static ArrayList<ProcessAGAT> nonActive = new ArrayList<ProcessAGAT>();  //All Processes
    static ArrayList<ProcessAGAT> deadList = new ArrayList<ProcessAGAT>();   //Finished

    int length = nonActive.size();

    AGAT() {
        super();
    }

    public int getMaxRemainingBurstTime() {
        int maxRemainingBurstTime = nonActive.get(0).getRemainingTime();
        for (int i = 0; i < length; i++) {
            if (nonActive.get(i).getRemainingTime() > maxRemainingBurstTime)
                maxRemainingBurstTime = nonActive.get(i).getRemainingTime();
        }
        return maxRemainingBurstTime;
    }

    public double v1() {
        double v1;
        int maxArrTime = nonActive.get(nonActive.size() - 1).getArrivalTime();

        if (maxArrTime > 10.0)
            v1 = maxArrTime / 10.0;
        else
            v1 = 1;

        return v1;
    }

    public double v2() {
        double v2;
        if (getMaxRemainingBurstTime() > 10)
            v2 = getMaxRemainingBurstTime() / 10.0;
        else
            v2 = 1;
        return v2;
    }

    public void AGAT_factor() {
        System.out.println("v1: " + v1());
        System.out.println("v2: " + v2());
        for (int i = 0; i < nonActive.size(); i++) {
            nonActive.get(i).setAGAT_factor((int)  (10 - nonActive.get(i).getPriority() +
                    Math.ceil(nonActive.get(i).getArrivalTime() / v1()) + Math.ceil(nonActive.get(i).getRemainingTime() / v2())));
            System.out.println("AGAT for "+nonActive.get(i).getName()+"= "+nonActive.get(i).getAGAT_factor());
        }
    }

    public void updateRemainingBurstTime(ProcessAGAT i) {
        i.setRemainingBurstTime((i.getRemainingTime() - calc_q(i.getQuantum())));
    }

    public void printQ(){
        for (int i=0;i<nonActive.size();i++){
            System.out.println("Quantum for "+nonActive.get(i).getName()+"= "+nonActive.get(i).getQuantum());
        }
    }

    static int calc_q(int a) {
        return (int) Math.round(a * 0.4);
    }


    public int getindex(ProcessAGAT pr) {
        int x=0;
        for (int i = 0; i < readyQueue.size();i++ ){
            if(pr==readyQueue.get(i)){
                x=i;
            }
        }
        return x;
    }


    public void Execution() {
        int t = 0;
        int indexOfCurrentProcess=0;
        readyQueue.add(nonActive.get(0));

        ProcessAGAT currentProcess = nonActive.get(0);
        System.out.print(nonActive.get(0).getName());
        System.out.println(" Added to Ready Queue");


        while (nonActive.size() != 0) {
            int v = 0;

            System.out.print("Time: "+t);
            System.out.println(" process "+currentProcess.getName());
            AGAT_factor();
            printQ();
            updateRemainingBurstTime(currentProcess);
            t += calc_q(currentProcess.getQuantum());
            v = calc_q(currentProcess.getQuantum());


            label1:
            while (true) {
                // if any process arrived add it to the ready queue
                for (int i = 0; i < nonActive.size(); i++) {
                    if (nonActive.get(i).getArrivalTime() <= t && !readyQueue.contains(nonActive.get(i))) {
                        readyQueue.add(nonActive.get(i));
                        System.out.print(nonActive.get(i).getName());
                        System.out.println(" Added to ready queue");
                    }
                }
                if (readyQueue.size() != 0) {
                    int indexOfLeastFactor = 0;

                    long leastAGAT_Factor = readyQueue.get(0).getAGAT_factor();

                    for (int j = 0; j < readyQueue.size(); j++) {
                        if (readyQueue.get(j).getAGAT_factor() < leastAGAT_Factor) {
                            leastAGAT_Factor = readyQueue.get(j).getAGAT_factor();
                            indexOfLeastFactor = j;
                        }
                    }

                    ProcessAGAT leastFactorProcess = readyQueue.get(indexOfLeastFactor);
                    //if the process used all its quantum time and it still has job to do
                    if (currentProcess.getQuantum() == v && currentProcess.getRemainingTime() > 0) {

                        System.out.println(currentProcess.getName() + " Used all its quantum time and it still has job to do");
                        currentProcess.setQuantum(currentProcess.getQuantum()+2);

                        indexOfCurrentProcess=getindex(currentProcess);
                        readyQueue.remove(currentProcess);
                        readyQueue.add(currentProcess);
                        currentProcess = readyQueue.get(indexOfCurrentProcess);

                        break label1;
                    }

                    while (true) {
                        // if the current process has least AGAT factor
                        if (currentProcess==leastFactorProcess && leastFactorProcess.getRemainingTime()>0) {
                            t ++;
                            v ++;
                            leastFactorProcess.setRemainingBurstTime(leastFactorProcess.getRemainingTime() - 1);

                            // go to check if any process has arrived and update AGAT factor
                            break ;

                        }
                        else { // if the current process does not have the least factor
                            //if the process didn’t use all its quantum time
                            if (currentProcess.getQuantum() > v && currentProcess.getRemainingTime() > 0) {
                                readyQueue.remove(currentProcess);
                                readyQueue.add(currentProcess);
                                currentProcess.setQuantum(currentProcess.getQuantum() + (currentProcess.getQuantum() - v));
                                System.out.println(currentProcess.getName() + " Didn’t use all its quantum time");
                                currentProcess = leastFactorProcess;
                            }
                            //if The running process finished its job
                            else {
                                currentProcess.setQuantum(0);
                                indexOfCurrentProcess=getindex(currentProcess);
                                readyQueue.remove(currentProcess);
                                nonActive.remove(currentProcess);
                                deadList.add(currentProcess);
                                System.out.print(currentProcess.getName());
                                System.out.println(" ADDED TO DEAD LIST");

                                if (nonActive.size() == 0) {
                                    System.out.println("time: "+t);
                                    return;
                                }
                                else
                                    currentProcess = nonActive.get(indexOfCurrentProcess);

                            }
                            break label1;
                        }
                    }
                } else {
                    t++;
                }

            }
        }
    }
    static void  selectionSort(ArrayList<ProcessAGAT> arr, int n)
    {
        int i, j, min;

        // One by one move boundary of unsorted subarray
        for (i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            min = i;
            for (j = i+1; j < n; j++)
                if (arr.get(j).getArrivalTime() < arr.get(min).getArrivalTime())
                    min = j;

            ProcessAGAT temp = arr.get(min);

            Collections.swap(arr, min, i);
            Collections.swap(arr, i, arr.indexOf(temp));
        }
    }


    static void AGATmain() {

        int numOfProcesses;
        int arrivalTime;
        String nameOfProcess;
        int quantum;
        int priority;

        Scanner input = new Scanner(System.in);
        AGAT process = new AGAT();

        System.out.println("AGAT Scheduling");
        System.out.println("------------------------------------");

        System.out.println("Enter number of processes ");
        numOfProcesses = input.nextInt();

        for (int i = 0; i < numOfProcesses; i++) {

            System.out.println("Enter name of process " + (i + 1));
            nameOfProcess = input.next();

            System.out.println("Enter arrival time of process " + nameOfProcess);
            arrivalTime = input.nextInt();

            System.out.println("Enter burst time of process " + nameOfProcess);
            int burstTime = input.nextInt();

            System.out.println("Enter priority time ");
            priority = input.nextInt();

            System.out.println("Enter quantum ");
            quantum = input.nextInt();

            System.out.println("---------------------------------");

            ProcessAGAT p = new ProcessAGAT(nameOfProcess, burstTime, arrivalTime, priority, quantum);
            AGAT.nonActive.add(p);

        }

//        ProcessAGAT p1 = new ProcessAGAT("p1", 17, 0, 4, 4);
//        ProcessAGAT p2 = new ProcessAGAT("p2", 6, 3, 9, 3);
//        ProcessAGAT p3 = new ProcessAGAT("p3", 10, 4, 3, 5);
//        ProcessAGAT p4 = new ProcessAGAT("p4", 4, 29, 8, 2);
//
//
//        AGAT.nonActive.add(p1);
//        AGAT.nonActive.add(p2);
//        AGAT.nonActive.add(p3);
//        AGAT.nonActive.add(p4);
//
//
        selectionSort(nonActive,numOfProcesses);
        process.Execution();


    }
}

