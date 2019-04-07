/**
 * A Branch and Bound (Best First Search) Implementation of N Puzzle Solver using Cost Function:
 * f = depth + sum(manhattanDistances(puzzle));
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class BNBPuzzleN {
	
	private static class PSWrapper implements Comparable<PSWrapper> {
		PuzzleState state;
		int cost;
		
		PSWrapper(PuzzleState state, int cost) {
			this.state = state;
			this.cost = cost;
		}
		
		@Override
		public int compareTo(PSWrapper o) {
			return this.cost - o.cost;
		}
	}
	
	public static PNResults findMoves(PuzzleState origin) {
		
		String serial = PuzzleState.defaultState(origin.getSize()).getSerial();
		PuzzleState cloned = PuzzleState.clone(origin);
		PSWrapper select = new PSWrapper(cloned, getCost(cloned));
		HashSet<String> lookup = new HashSet<String>();
		List<PSWrapper> queue = new ArrayList<PSWrapper>();
		
		int depth = 0;

		int maxDepth = 0;
		int nodeChecks = 0;

		queue.add(select);
		
		while (!queue.isEmpty()) {
			select = removeBest(queue);
			nodeChecks++;
			
			String sSerial = select.state.getSerial();
			
			int moves = select.state.getMoveList().length();
			if (depth != moves) {
				depth = moves;
				maxDepth = depth > maxDepth ? depth : maxDepth;
				// System.out.println("Depth: "+depth); // Reply Depth
			}
			
			if (serial.equals(sSerial)) {
				return new PNResults(maxDepth, nodeChecks, select.state.getMoveList());
			}
			
			// Set Lookup Here to avoid rechecking old nodes
			queue.addAll(getChildren(select, lookup));
		}
		
		return new PNResults(maxDepth, nodeChecks, "NaN");
	}
	
	private static List<PSWrapper> getChildren(PSWrapper parent, HashSet<String> lookup) {
		List<PSWrapper> children = new ArrayList<PSWrapper>();
		for (PuzzleState child: parent.state.getAllMoveStates())
			if (lookup.add(child.getSerial()))
				children.add(new PSWrapper(child, getCost(child)));
			
		return children;
	}
	
	private static PSWrapper removeBest(List<PSWrapper> list) {
		Iterator<PSWrapper> iterator = list.iterator();
		if (!iterator.hasNext()) return null;
		int i = 0;
		int maxI = 0;
		PSWrapper max = iterator.next();
		while (iterator.hasNext()) {
			PSWrapper next = iterator.next();
			i++;
			if (max.compareTo(next) > 0) {
				max = next;
				maxI = i;
			}
		}
		return list.remove(maxI);
	}
	
	public static int getCost(PuzzleState state) {
		return state.getMoveList().length() + getManhattanSum(state);
	}
	
	public static int getManhattanSum(PuzzleState state) {
		int size = state.getSize(), sum = 0;
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++) {
				int indNumber = state.matrix[i][j];
				if (indNumber == PuzzleState.emptyInt) continue;
				Point origIndex = state.getOriginalIndex(indNumber);
				sum += Math.abs(origIndex.y-i) + Math.abs(origIndex.x-j);
			}
		return sum;
	}
}
