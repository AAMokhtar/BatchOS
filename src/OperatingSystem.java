import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class OperatingSystem {
	
	public static ArrayList<Thread> ProcessTable;

	public static mutex inputMutex;
	public static mutex writeMutex;
	public static mutex printMutex;
	public static mutex readMutex;
	
	static {
		inputMutex = new mutex();
		writeMutex = new mutex();
		printMutex = new mutex();
		readMutex  = new mutex();
	}

//	public static int activeProcess= 0;
	//system calls:
	// 1- Read from File
	@SuppressWarnings("unused")
	public static String readFile(String name) {
		StringBuilder Data= new StringBuilder();
		File file = new File(name);
	 try {
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine())
		{
			Data.append(scan.nextLine()).append("\n");
		}
		scan.close();
	} catch (FileNotFoundException e) {
		System.out.println(e.getMessage());
	}
		return Data.toString();
	}
	
	// 2- Write into file
	@SuppressWarnings("unused")
	public static void writefile(String name, String data) {
		try
		{
			BufferedWriter BW = new BufferedWriter(new FileWriter(name));
			BW.write(data);
			BW.close();
		} 
		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}

	}
	//3- print to console
	@SuppressWarnings("unused")
	public static void printText(String text) {

		System.out.println(text);
		
	}
	
	//4- take input
	@SuppressWarnings("unused")
	public static String TakeInput() {
		Scanner in = new Scanner(System.in);
		return in.nextLine();
		
	}
	
	private static void createProcess(int processID){
		Process p = new Process(processID);
		ProcessTable.add(p);
		Process.setProcessState(p,ProcessState.Ready);
		p.start();
		
	}
	
	public static void main(String[] args) {
   		ProcessTable = new ArrayList<>();

		createProcess(1);
		createProcess(2);
		createProcess(3);
		createProcess(4);
		createProcess(5);
	}
}



