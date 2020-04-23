import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

//WARNING: IF YOU ARE RUNNING WITHOUT A SCHEDULER
//REPLACE LINE 43 WITH LINE 46 IN THIS CLASS!!!
public class mutex {
    Queue<Process> blocked; //blocked queue for that resource

    /* atomic to ensure that the value is read correctly
     * by all threads and, that only one thread can
     * write to the mutex at a time
     */
    AtomicReference<Process> owner = new AtomicReference<>(); //the process that owns the mutex

    public mutex() {blocked = new LinkedList<>();}

    public void acquire(Process p){
        if (owner.get() == null){ //the mutex is free
            p.flag.set(true); //turn flag
            owner.set(p); //owner = p
        }
        else { //the mutex is locked
            blocked.add(p); //add the requesting process to the blocked queue

            //==========
            //BLOCK THE PROCESS
            Process.setProcessState(p,ProcessState.Waiting);
            p.loop();
            //==========
        }
    }

    public void release(Process p){
        if (owner.get() != p) return; //if the calling process is not the owner, do nothing

        //else

        if (!blocked.isEmpty()){ //give the resource to the process that waited the longest
            owner.compareAndSet(p,blocked.poll()); //acquire the mutex
            Process.setProcessState(owner.get(),ProcessState.Ready); //ready state

            //if we use FCFS scheduler:
                OperatingSystem.readyQ.add(owner.get()); //put in the ready queue

            //if we use mutexes alone;
//                owner.get().flag.set(true); //set turn


        }
        else  owner.compareAndSet(p,null); //no owner
    }
}
