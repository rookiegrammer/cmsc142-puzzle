import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class PuzzleSolve {
	
	public static final int trials = 10;
	
	public static final long nanoInMil = 1000000L;
	public static final long nanoInSec = 1000000000L;
	public static final long nanoInMin = 60000000000L;
	public static final long nanoInHour = 3600000000000L;
	public static final long nanoInDay = 86400000000000L;
	
	public static final String emptyStr = "";
	public static final String separator = ", ";
	public static final String newline = "\n";
	
	public static final String BFSName = "BFS ******";
	public static final String BNBName = "BnB ******";
	public static final String resultSeparator = "\n---------\n";
	
	public static final String parenLeft = " (";
	public static final String parenRight = ")";
	
	public static final String defaultFileNameAppend = "-result.txt";
	
	public static void main(String[] args) {
		File[] files = new File[2];
		if (args.length < 1) {
			try {
				EventQueue.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (ClassNotFoundException e) {
						} catch (InstantiationException e) {
						} catch (IllegalAccessException e) {
						} catch (UnsupportedLookAndFeelException e) {
						}
						int res;
						JFileChooser fileSelector = new JFileChooser();
						res = fileSelector.showOpenDialog(null);
						if (res != JFileChooser.APPROVE_OPTION) return;
						files[0] = fileSelector.getSelectedFile();
						fileSelector.setCurrentDirectory(files[0].getParentFile());
						res = fileSelector.showSaveDialog(null);
						if (res == JFileChooser.APPROVE_OPTION) {
							files[1] = fileSelector.getSelectedFile();
						} else {
							files[1] = new File(files[0].getAbsolutePath()+defaultFileNameAppend);
						}
						
					}
				});
			} catch (InvocationTargetException e) {
			} catch (InterruptedException e) {}
			
		} else if (args.length < 2) {
			files[0] = new File(args[0]);
			files[1] = new File(args[1]+defaultFileNameAppend);
		} else {
			files[0] = new File(args[0]);
			files[1] = new File(args[1]);
		}
		solveFile(files[0], files[1]);
		System.exit(0);
	}
	
	public static PNResults[][] solve(int[] solvable) {
		PuzzleState state = PuzzleState.from(solvable);
		PNResults[][] solves = new PNResults[2][trials];
		long start;
		PNResults current;
		for (int i=0; i<trials; i++) {
			System.out.print(".");
			
			start = System.nanoTime();
			current = BFSPuzzleN.findMoves(state);
			current.duration = System.nanoTime()-start;
			current.puzzle = state;
			solves[0][i] = current;
			
			
			start = System.nanoTime();
			current = BNBPuzzleN.findMoves(state);
			current.duration = System.nanoTime()-start;
			current.puzzle = state;
			solves[1][i] = current;
			
			System.out.print(".."+(i+1));
		}
		return solves;
	}
	
	public static PNResults[][][] solveFile(File file) {
		ArrayList<PNResults[][]> list = new ArrayList<PNResults[][]>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			int l = 1;
			String st; 
			while ((st = br.readLine()) != null) {
				System.out.println("Processing line "+(l++));
				String[] spl = st.trim().replaceAll("\\s+", " ").split(" ");
				int[] sol = new int[spl.length];
				for (int i=0; i<spl.length; i++) {
					sol[i] = Integer.parseInt(spl[i]);
				}
				list.add(solve(sol));
				System.out.println("...finished");
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return list.toArray(new PNResults[0][][]);
	}
	
	public static void solveFile(File input, File output) {
		PNResults[][][] results = solveFile(input);
		String out = emptyStr;
		for (PNResults[][] mres: results)
			out += mres[0][0].puzzle.getSerial() + newline + tabulateSolves(mres) + newline + resultSeparator + newline;
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));
			writer.write(out);
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	private static String tabulateSolves(PNResults[][] result) {
		String b = emptyStr;
		for (int i=0; i<2; i++) {
			String m = emptyStr, v = null;
			for (int j=0; j<result[i].length; j++) {
				PNResults r = result[i][j];
				m += (j==0 ? emptyStr : separator) + nanosToString(r.duration) + parenLeft + r.nodeChecks + parenRight;
				v = r.moveList;
			}
			b += (i==0 ? BFSName : newline + BNBName) + newline + v + newline + m;
		}
		return b;
	}
	
	private static String nanosToString(long nanos) {
		long mil = (nanos%nanoInSec)/nanoInMil;
	    long sec = (nanos%nanoInMin)/nanoInSec;
	    long min = (nanos%nanoInHour)/nanoInMin;
	    long hour = (nanos%nanoInDay)/nanoInHour;
	    long day = nanos/nanoInDay;
	    String s = String.format("%s.%s", padLong(sec, nanos>=nanoInMin ? 2 : 0),padLong(mil,3));
	    String b = "";
	    boolean prev = false, next = false;
	    next = day > 0;
	    if (next) b += Long.toString(day)+':';
	    next = (prev = next) || hour > 0;
	    if (next) b += padLong(hour, prev ? 2 : 0)+':';
	    next = (prev = next) || min > 0;
	    if (next) b += padLong(min, prev ? 2 : 0)+':';
	    return b+s;
	}
	
	private static String padLong(long val, int size) {
		String s = Long.toString(val);
		int rem = size - s.length();
		while (rem > 0) {
			s = '0'+s;
			rem--;
		}
		return s;
	}
}
