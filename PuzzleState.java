import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PuzzleState {
	final static int emptyInt = 0;
	
	private int size = 3;
	
	int[][] matrix;
	Point emptyIndex;
	
	final static char LMove = 'L';
	final static char RMove = 'R';
	final static char TMove = 'T';
	final static char BMove = 'B';
	
	private String moveList = "";
	
	public PuzzleState(int size) {
		this.size = size;
		this.matrix = new int[size][size];
	}
	
	public static PuzzleState duplicate(PuzzleState src) {
		PuzzleState state = new PuzzleState(src.size);
		state.matrix = new int[src.size][];
		for (int i=0; i<src.size; i++)
			state.matrix[i] = src.matrix[i].clone();
		state.emptyIndex = (Point) src.emptyIndex.clone();
		state.moveList = src.getMoves();
		
		return state;	
	}
	
	public static PuzzleState defaultState(int size) {
		if (size <= 0) return null;
		
		PuzzleState state = new PuzzleState(size);
		int mIndex = size-1;
		int n = 1;
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++) {
				if (i==mIndex && j==mIndex)
					state.matrix[i][j] = emptyInt;
				else 
					state.matrix[i][j] = n++;
			}
		state.emptyIndex = new Point(mIndex, mIndex);
		return state;
	}
	
	private boolean checkPoint(int x, int y) {
		return x >= 0 && x < size && y >= 0 && y < size;
	}
	
	public Point getOriginalIndex(int fromIndex) {
		if (fromIndex < 0 || fromIndex > size*size-1) return null;
		if (fromIndex == emptyInt) return new Point(size-1, size-1);
		
		int n = fromIndex-1;
		return new Point(n%size, Math.floorDiv(n, size));
	}
	
	public int getSize() {
		return size;
	}
	
	private PuzzleState getSwap(int offsetX, int offsetY) {
		if (emptyIndex == null || !checkPoint(emptyIndex.x, emptyIndex.y))
			return null;
		
		int getX = emptyIndex.x+offsetX;
		int getY = emptyIndex.y+offsetY;
		
		if (!checkPoint(getX, getY))
			return null;
		
		PuzzleState newState = PuzzleState.duplicate(this);
		
		newState.matrix[emptyIndex.y][emptyIndex.x] = newState.matrix[getY][getX];
		newState.matrix[getY][getX] = emptyInt;
		
		newState.emptyIndex = new Point(getX, getY);
		
		return newState;
	}
	
	public String getSerial() {
		String serial = size+"S";
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++) {
				serial += matrix[i][j];
			}
		return serial;
	}
	
	public PuzzleState getLeftSwap() {
		PuzzleState state = getSwap(-1,0);
		state.moveList += LMove;
		return state;
	}
	
	public PuzzleState getRightSwap() {
		PuzzleState state = getSwap(1,0);
		state.moveList += RMove;
		return state;
	}
	
	public PuzzleState getTopSwap() {
		PuzzleState state = getSwap(0,-1);
		state.moveList += TMove;
		return state;
	}
	
	public PuzzleState getBottomSwap() {
		PuzzleState state = getSwap(0,1);
		state.moveList += BMove;
		return state;
	}
	
	public String getMoves() {
		return moveList+"";
	}
	
	public List<PuzzleState> getSwaps() {
		List<PuzzleState> list = new ArrayList<PuzzleState>();
		
		PuzzleState left = getLeftSwap();
		PuzzleState right = getRightSwap();
		PuzzleState top = getTopSwap();
		PuzzleState bottom = getBottomSwap();
		
		
		if (left != null) list.add(left);
		if (right != null) list.add(right);
		if (top != null) list.add(top);
		if (bottom != null) list.add(bottom);
		
		return list;
	}
	
	public String toString() {
		String build = "";
		for (int[] row: matrix) 
			build += Arrays.toString(row) + '\n';
		return build;
	}
	
	
}
