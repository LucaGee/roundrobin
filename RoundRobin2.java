package csciproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Luca Gallagher
 */
public class RoundRobin2 {
    public static void main(String[] args) throws FileNotFoundException {
    File file = new File("programs.txt");//file to be used as a variable
    LinkedList<Process> processes = new LinkedList();
    
    //BEGIN Line count code (to use later to create objects of info)
    Scanner countlines = new Scanner(new FileReader(file)); 
    int lineCount = 0;
    while(countlines.hasNext()) {
        lineCount++;
        countlines.nextLine();
    }
    //Lines of file counted
    
    //START Process of putting info into objects from file
    Scanner scan = new Scanner(new FileReader(file));
        for(int g = 0; g < lineCount; g++) { //repeats amount of times that there are lines.
            String current = scan.nextLine();//current line

            //temporary variables to then be added to the objects. My program will fail with anything but numbers, don't put anything other than numbers
            String xpid = "";
            String xarrive = "";
            String xburst = "";
            
                int loops = 0; //This is to keep track of which position in the string the program is at, so that we can keep reading for new variables (pid, arrive, burst)
                while(true) {
                    char currentChar = current.charAt(loops);
                    if(currentChar == ',') {
                        break;
                    }
                    xpid = xpid + current.charAt(loops);
                    loops++;
                }

                //the next lines just repeat this process for arrive and burst.
                int loops2 = loops + 1;
                while(true) {
                    char currentChar = current.charAt(loops2);
                    if(currentChar == ',') {
                        break;
                    }
                    xarrive = xarrive + current.charAt(loops2);
                    loops2++;
                }
                
                int loops3 = loops2 + 1;
                
                while(true) {
                    char currentChar = current.charAt(loops3);

                    xburst = xburst + current.charAt(loops3);
                    if((current.length()) == (loops3 + 1)){
                        break;
                    }
                    loops3++;
                    
                }
                
                //converts all of the numbers taken as strings into ints so they can be used in the object.
                int xxpid = Integer.parseInt(xpid);
                int xxburst = Integer.parseInt(xburst);
                int xxarrive = Integer.parseInt(xarrive);
                processes.add(new Process(xxpid, xxburst, xxarrive));
                
        }
        
        //START!
        LinkedList<Process> readyQueue = new LinkedList();//initialize the readyQueue
        int time = 0;
        int timeQ = 1;
        int timeLast = 0;
        int timeDiff = 0;
        LinkedList<Process> order = new LinkedList();
        //initial start condition to find processes ready at arrive = 0.

        Scanner scans = new Scanner(System.in);
        System.out.println("Enter Time Quantum: ");
        timeQ = Integer.parseInt(scans.nextLine());
        while(true) {
            
            boolean complete = true; //will be set to false if it's not complete, but if it is it will remain true.
            
            System.out.println("Current Time is t = " + time);
            for(int i = 0; i < lineCount; i++) { //Checks if all processes are complete, if not it will set complete to false so that the loop keeps running
                if(processes.get(i).isFinished() == false) {
                    //System.out.println("PID #" + processes.get(i).getPID() + " is not finished. Rem B time " + processes.get(i).getRemBurst());
                    complete = false;
                }
            }
            
            //this will check for arrivals that are of the current time.
            for(int i = 0; i < lineCount; i++) {
                if(processes.get(i).getArrive() == time) {
                    if(processes.get(i).getRemBurst() > 0) {
                        if(!readyQueue.contains(processes.get(i))) {
                            readyQueue.addLast(processes.get(i));
                            //System.out.println("####CURRENT TIME# Added " + processes.get(i).getPID());
                        }
                    }
                }
            }
            for(int x = 0; x < lineCount; x++) {
                if((time+timeQ) >= processes.get(x).getArrive()) {
                    for(int j = time; j < ((time + timeQ) + 1); j++) {
                        for(int i = 0; i < lineCount; i++) {
                            if(processes.get(i).getArrive() == j) {
                                if(!readyQueue.contains(processes.get(i))){
                                    readyQueue.addLast(processes.get(i));
                                    //System.out.println("####MIDCHECKER# Added " + processes.get(i).getPID());
                                }
                            }
                        } 
                    }
                }
            }
            
            if(!readyQueue.isEmpty()){
                
                
                System.out.println("Process #" + readyQueue.getFirst().getPID() + " was at burst time " + readyQueue.getFirst().getRemBurst());
                readyQueue.getFirst().run(timeQ);
                if(readyQueue.getFirst().remainingBurst < 0) {
                    time = time - readyQueue.getFirst().remainingBurst;
                    
                    for(int i = 0; i < lineCount; i++) {
                        if(readyQueue.getFirst() != processes.get(i)) {
                            if(processes.get(i).isFinished() == false) {
                                processes.get(i).waitTime -= readyQueue.getFirst().remainingBurst;
                            }
                        }
                    }
                } else {
                    time = time + timeQ;
                    for(int i = 0; i < lineCount; i++) {
                        if(readyQueue.getFirst() != processes.get(i)) {
                            if(processes.get(i).isFinished() == false) {
                                processes.get(i).waitTime += timeQ;
                            }
                        }
                    }
                }
                
                
                if(readyQueue.getFirst().isFinished()) {
                    if(readyQueue.getFirst().getFinishTime() == 0) {
                        if(readyQueue.getFirst().remainingBurst < 0) {
                            readyQueue.getFirst().setFinishTime(time);
                            //System.out.println("+++++Set finish time of PID " + readyQueue.getFirst().getPID() + " at 1");
                        } else {
                            
                            readyQueue.getFirst().setFinishTime(time);
                            //System.out.println("+++++Set finish time of PID " + readyQueue.getFirst().getPID() + " at 2");
                        }
                    }
                }
                System.out.println("Process #" + readyQueue.getFirst().getPID() + " is now at burst time " + readyQueue.getFirst().getRemBurst());
                

                System.out.println();
                if(readyQueue.getFirst().getRemBurst() > 0) {
                    readyQueue.addLast(readyQueue.getFirst());
                    order.addLast(readyQueue.getFirst());
                    readyQueue.removeFirst();
                } else {
                    order.addLast(readyQueue.getFirst());
                    readyQueue.removeFirst();
                }

            }
            
            for(int i = 0; i < lineCount; i++) {
                if(processes.get(i).getArrive() == time + 1) {
                    if(processes.get(i).getRemBurst() > 0) {
                        if(!readyQueue.contains(processes.get(i))) {
                            readyQueue.addLast(processes.get(i));
                        }
                    }
                }
            }
            
            
            if(complete == true) {
                break;
            }
        }
        //Proper wait time math
        for(int i = 0; i < lineCount; i++) {
            processes.get(i).waitTime -= processes.get(i).arrive;
        }
        
       
        //Finish Times
        for(int i = 0; i < lineCount; i++) {
            System.out.println("PID #" + processes.get(i).getPID() + " finish time: " + processes.get(i).getFinishTime());
        }
        
        //Wait Times
        for(int i = 0; i < lineCount; i++) {
            System.out.println("PID #" + processes.get(i).getPID() + " wait time: " + processes.get(i).getWaitTime());
        }
        
        //Turn around time
        for(int i = 0; i < lineCount; i++) {
            int turnAround = processes.get(i).finishTime - processes.get(i).arrive;
            System.out.println("PID #" + processes.get(i).getPID() + " turn around time " + turnAround);
        }
        //Average Wait Time
        int waitTotal = 0;
        for(int i = 0; i < lineCount; i++) {
            waitTotal += processes.get(i).getWaitTime();
        }
        int waitAvg = waitTotal / lineCount;
        System.out.println("Average Wait Time: " + waitAvg);
        
        //Order of Execution
        System.out.print("Order of Execution: ");
        for(int i = 0; i < order.size(); i++) {
            System.out.print("|" + order.get(i).getPID());
        }
        System.out.print("|");
        System.out.println();
    }
}
