import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BFSPuzzleN {
	
	public static PNResults findMoves(PuzzleState origin) {
		
		String serial = PuzzleState.defaultState(origin.getSize()).getSerial();
		PuzzleState select = PuzzleState.clone(origin);
		HashMap<String, PuzzleState> lookup = new HashMap<String, PuzzleState>();
		Queue<PuzzleState> queue = new LinkedList<PuzzleState>();
		
		int depth = 0;
		
		int maxDepth = 0;
		int nodeChecks = 0;
		
		queue.add(select);
		
		while (!queue.isEmpty()) {
			select = queue.poll();
			
			String sSerial = select.getSerial();
			PuzzleState check = lookup.get(sSerial);
			
			if (check == null) {
				lookup.put(sSerial, select);
			} else continue;
			
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
