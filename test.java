
public abstract class test {

	public static void main(String[] args) {
		PuzzleState state = PuzzleState.from(new int[][]{{2,0,8},{6,3,5},{1,4,7}});
		long start;
		start = System.currentTimeMillis();
		System.out.println( "BFS Moves: "+BFSPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
		start = System.currentTimeMillis();
		System.out.println( "BNB Moves: "+BNBPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");	
	}
	
	

}
