import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BFSPuzzleN {
	
	public static PNResults findMoves(PuzzleState origin) {
		
		String serial = PuzzleState.defaultState(origin.getSize()).getSerial();
		PuzzleState select = PuzzleState.clone(origin);
		HashSet<String> lookup = new HashSet<String>();
		Queue<PuzzleState> queue = new LinkedList<PuzzleState>();
		
		int depth = 0;
		
		int maxDepth = 0;
		int nodeChecks = 0;
		
		queue.add(select);
		
		while (!queue.isEmpty()) {
			select = queue.poll();
			
			String sSerial = select.getSerial();
			
			if (!lookup.add(sSerial)) 
				continue;
			
			nodeChecks++;
			
			int moves = select.getMoveList().length();
			if (depth != moves) {
				depth = moves;
				maxDepth = depth > maxDepth ? depth : maxDepth;
				// System.out.println("Depth: "+depth); // Reply Depth
			}
			
			if (serial.equals(sSerial)) {
				return new PNResults(maxDepth, nodeChecks, select.getMoveList());
			}
				
			queue.addAll(select.getAllMoveStates());
		}
		
		return new PNResults(maxDepth, nodeChecks, "NaN");
	}
	
}
