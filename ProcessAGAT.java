public class ProcessAGAT{
    private String ProcessName;
    private int ArrivalTime;
    private int BurstTime;
    private int remainingBurstTime;
    private int priority;
    private int Quantum;
    private int AGAT_factor;



    public ProcessAGAT() {
        ProcessName="";
        ArrivalTime=0;
        BurstTime=0;
        remainingBurstTime=0;
        priority=0;
        Quantum=0;
        AGAT_factor=0;

    }

    public ProcessAGAT(String ProcessName , int BurstTime, int ArrivalTime, int priority,int Quantum) {
        this.ProcessName = ProcessName;
        this.ArrivalTime = ArrivalTime;
        this.BurstTime = BurstTime;
        this.priority = priority;
        this.remainingBurstTime = this.BurstTime;
        this.Quantum=Quantum;

    }

    public void set_ProcessName(String Processname) {
        this.ProcessName = Processname;
    }
    public void setArrivalTime(int arrivalTime) {
        this.ArrivalTime = arrivalTime;
    }
    public void setBurstTime(int BurstTime) {
        this.BurstTime = BurstTime;
    }
    public void setRemainingBurstTime(int remainingTime) {
        this.remainingBurstTime = remainingTime;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public void setAGAT_factor(int AGAT_factor){this.AGAT_factor=AGAT_factor;}
    public void setQuantum(int Quantum){this.Quantum=Quantum;}


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
        return remainingBurstTime;
    }
    public int getPriority() {return priority;}
    public int getAGAT_factor(){
        return AGAT_factor;
    }
    public int getQuantum(){return Quantum;}


}
