
public abstract class test {

	// For file solving, use PuzzleSolve class

	public static void main(String[] args) {
		test1(); // fast test 3*3
		test2(); // slow test 4*4
	}

	public static void test1() {
		PuzzleState state = PuzzleState.from(new int[]{2,0,8,6,3,5,1,4,7});
		long start;
		start = System.currentTimeMillis();
		System.out.println( "BNB Moves: "+BNBPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
		start = System.currentTimeMillis();
		System.out.println( "BFS Moves: "+BFSPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
	}

	public static void test2() {
		PuzzleState state = PuzzleState.from(new int[]{11,9,2,0,3,5,7,4,1,8,10,12,14,13,15,6});
		long start;
		start = System.currentTimeMillis();
		System.out.println( "BNB Moves: "+BNBPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
		start = System.currentTimeMillis();
		System.out.println( "BFS Moves: "+BFSPuzzleN.findMoves(state)+" done in "+(System.currentTimeMillis()-start)+" ms");
	}

}
