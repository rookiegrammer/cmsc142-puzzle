import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PuzzleState {
	final static int emptyInt = 0;
	
	private int size = 3;
	
	int[][] matrix;
	Point emptyIndex;
	
	final static char LMove = 'L';
	final static char RMove = 'R';
	final static char TMove = 'U';
	final static char BMove = 'D';
	
	private String moveList = "";
	
	private PuzzleState(int size, int[][] matrix) {
		initialize(size, matrix);
	}
	
	public PuzzleState(int size) {
		initialize(size, new int[size][size]);
	}
	
	private void initialize(int size, int[][] matrix) {
		this.size = size;
		this.matrix = matrix;
	}
	
	public boolean checkAndRefresh() {
		HashSet<Integer> lookup = new HashSet<Integer>();
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++) {
				if (!lookup.add(matrix[i][j]))
					return false;
				if (matrix[i][j] < 0 || matrix[i][j] >= size*size)
					return false;
				if (matrix[i][j] == emptyInt) {
					emptyIndex = new Point(j, i);
				}
				
			}
		return true;
	}
	
	public static PuzzleState from(int[][] matrix) {
		if (matrix.length == 0 || matrix.length != matrix[0].length) return null; // Simple check
		PuzzleState state = new PuzzleState(matrix.length, matrix);
		if (state.checkAndRefresh())
			return state;
		return null;
	}
	
	public static PuzzleState from(int[] list) {
		int size = (int) Math.sqrt(list.length);
		if (Math.pow(size, 2) != list.length) return null;
		int[][] matrix = new int[size][size];
		int k = 0;
		for (int i=0; i<size; i++) 
			for (int j=0; j<size; j++) {
				matrix[i][j] = list[k++];
			}
		return from(matrix);
	}
	
	public static PuzzleState copy(PuzzleState src) {
		PuzzleState state = clone(src);
		state.moveList = src.getMoveList();
		return state;	
	}
	
	public static PuzzleState clone(PuzzleState src) {
		PuzzleState state = new PuzzleState(src.size);
		state.matrix = new int[src.size][];
		for (int i=0; i<src.size; i++)
			state.matrix[i] = src.matrix[i].clone();
		state.emptyIndex = (Point) src.emptyIndex.clone();
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
	
	public int getPuzzleType() {
		return size*size-1;
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
		
		PuzzleState newState = PuzzleState.copy(this);
		
		newState.matrix[emptyIndex.y][emptyIndex.x] = newState.matrix[getY][getX];
		newState.matrix[getY][getX] = emptyInt;
		
		newState.emptyIndex = new Point(getX, getY);
		
		return newState;
	}
	
	public String getSerial() {
		String serial = size+"S: ";
		for (int i=0; i<size; i++)
			for (int j=0; j<size; j++) {
				serial += (i==0 && j==0 ? "" : " ") + matrix[i][j];
			}
		return serial;
	}
	
	public PuzzleState getLeftMoveState() {
		PuzzleState state = getSwap(-1,0);
		if (state != null) 
			state.moveList += LMove;
		return state;
	}
	
	public PuzzleState getRightMoveState() {
		PuzzleState state = getSwap(1,0);
		if (state != null) 
			state.moveList += RMove;
		return state;
	}
	
	public PuzzleState getTopMoveState() {
		PuzzleState state = getSwap(0,-1);
		if (state != null) 
			state.moveList += TMove;
		return state;
	}
	
	public PuzzleState getBottomMoveState() {
		PuzzleState state = getSwap(0,1);
		if (state != null) 
			state.moveList += BMove;
		return state;
	}
	
	public String getMoveList() {
		return moveList+"";
	}
	
	public List<PuzzleState> getAllMoveStates() {
		List<PuzzleState> list = new ArrayList<PuzzleState>();
		
		PuzzleState left = getLeftMoveState();
		PuzzleState right = getRightMoveState();
		PuzzleState top = getTopMoveState();
		PuzzleState bottom = getBottomMoveState();
		
		
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
	
	public boolean isEqual(PuzzleState state) {
		return this.getSerial().equals(state.getSerial());
	}
	
}
