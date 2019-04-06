import java.awt.Point;

public class BNBPuzzleN {
	
	
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
