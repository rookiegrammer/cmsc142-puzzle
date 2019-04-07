
public abstract class test {

	public static void main(String[] args) {
		PuzzleState state = PuzzleState.from(new int[][]{{7,8,0},{1,3,4},{6,2,5}});
		long start = System.currentTimeMillis();
		System.out.println( "BFS Moves: "+BFSPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
		start = System.currentTimeMillis();
		System.out.println( "BNB Moves: "+BNBPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
	}
	
	

}
