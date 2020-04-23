import java.util.concurrent.atomic.AtomicBoolean;

public class Process extends Thread {

	public int processID;
	ProcessState status = ProcessState.New;

	/*
	 * flag to break out of the loop simulating the transition from the blocked
	 * state to the running state
	 */
	AtomicBoolean flag = new AtomicBoolean(true);

	public Process(int m) {
		processID = m;
	}

	public void loop() { // process is blocked
		flag.set(false);
		while (!flag.get()) {} // will wait until flag is set
	}

	@Override
	public void run() {
		OperatingSystem.setRun(false);
		switch (processID) {
		case 1:
			process1();
			break;
		case 2:
			process2();
			break;
		case 3:
			process3();
			break;
		case 4:
			process4();
			break;
		case 5:
			process5();
			break;
		}
	}

	private void process1() {
		OperatingSystem.inputMutex.acquire(this);
		OperatingSystem.printMutex.acquire(this);
		OperatingSystem.readMutex.acquire(this);

		OperatingSystem.printText("Enter File Name: ");
		OperatingSystem.printText(OperatingSystem.readFile(OperatingSystem.TakeInput()));

		OperatingSystem.printMutex.release(this);
		OperatingSystem.readMutex.release(this);
		OperatingSystem.inputMutex.release(this);
		setProcessState(this, ProcessState.Terminated);
	}

	public String toString() {
		return processID + "  " + status;
	}

	private void process2() {

		OperatingSystem.inputMutex.acquire(this);
		OperatingSystem.printMutex.acquire(this);
		OperatingSystem.printText("Enter File Name: ");

		String filename = OperatingSystem.TakeInput();
		OperatingSystem.printText("Enter Data: ");
		String data = OperatingSystem.TakeInput();

		OperatingSystem.inputMutex.release(this);
		OperatingSystem.printMutex.release(this);
		OperatingSystem.writeMutex.acquire(this);

		OperatingSystem.writefile(filename, data);

		OperatingSystem.writeMutex.release(this);

		setProcessState(this, ProcessState.Terminated);
	}

	private void process3() {
		int x = 0;
		OperatingSystem.printMutex.acquire(this);

		while (x < 301) {
			OperatingSystem.printText(x + "\n");
			x++;
		}

		OperatingSystem.printMutex.release(this);

		setProcessState(this, ProcessState.Terminated);
	}

	private void process4() {
		OperatingSystem.printMutex.acquire(this);

		int x = 500;
		while (x < 1001) {
			OperatingSystem.printText(x + "\n");
			x++;
		}

		OperatingSystem.printMutex.release(this);

		setProcessState(this, ProcessState.Terminated);
	}

	private void process5() {
		OperatingSystem.inputMutex.acquire(this);
		OperatingSystem.printMutex.acquire(this);

		OperatingSystem.printText("Enter LowerBound: ");
		String lower = OperatingSystem.TakeInput();
		OperatingSystem.printText("Enter UpperBound: ");
		String upper = OperatingSystem.TakeInput();

		OperatingSystem.inputMutex.release(this);
		OperatingSystem.printMutex.release(this);

		int lowernbr = Integer.parseInt(lower);
		int uppernbr = Integer.parseInt(upper);
		StringBuilder data = new StringBuilder();

		while (lowernbr <= uppernbr) {
			data.append(lowernbr++).append("\n");
		}

		OperatingSystem.writeMutex.acquire(this);

		OperatingSystem.writefile("P5.txt", data.toString());

		OperatingSystem.writeMutex.release(this);

		setProcessState(this, ProcessState.Terminated);
	}

	public static void setProcessState(Process p, ProcessState s) {
		p.status = s;
		if (s == ProcessState.Terminated) {
			OperatingSystem.ProcessTable.remove(p);
			OperatingSystem.setRun(true);
			OperatingSystem.Running();
		}
	}

	@SuppressWarnings("unused")
	public static ProcessState getProcessState(Process p) {
		return p.status;
	}
}
