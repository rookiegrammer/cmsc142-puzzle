
public class PNResults {
	int maxDepth;
	int nodeChecks;
	String moveList;
	
	public PNResults() {
	}
	
	public PNResults(int maxDepth, int nodeChecks, String moveList) {
		this.maxDepth = maxDepth;
		this.nodeChecks = nodeChecks;
		this.moveList = moveList;
	}
	
	public String toString() {
		return moveList+" (checked "+nodeChecks+" nodes until depth "+maxDepth+")";
	}
}
