import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int n;
	private WeightedQuickUnionUF unionUF;
	private boolean[] visited;
	private int sqr;
	private int top;
	private int bottom;
	private int numberOfOpenSites;
	private int[] rows;
	private int[] cols;
	private WeightedQuickUnionUF fullSiteUF;
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		this.n = n;
		this.sqr = n * n;
		unionUF = new WeightedQuickUnionUF(sqr + 2);
		fullSiteUF = new WeightedQuickUnionUF(sqr+1);
		visited = new boolean[sqr];
		top = sqr;
		bottom = sqr+1;
		rows = new int[] { -1, 0, 1, 0 };
		cols = new int[] { 0, 1, 0, -1 };
		// create n-by-n grid, with all sites blocked
	}

	public void open(int row, int col) {
		if (!validate(row, col)) {
			throw new IllegalArgumentException();
		}
		int index = oneBasedIndex(row, col);
		if (visited[index - 1]) {
			return;
		}
		visited[index - 1] = true;
		numberOfOpenSites++;
		if (isBottomToBeOpened(row, col)) {
			unionUF.union(index - 1, bottom);
		}
		if (isTopToBeOpened(row, col)) {
			unionUF.union(index - 1, top);
			fullSiteUF.union(index-1, top);
		}
		for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
			int targetRow = row + rows[rowIndex];
			int targetCol = col + cols[rowIndex];
			if (validate(targetRow, targetCol)) {
				int targetIndex = oneBasedIndex(targetRow, targetCol);
				if (visited[targetIndex - 1]) {
					unionUF.union(index - 1, targetIndex - 1);
					fullSiteUF.union(index-1, targetIndex-1);
				}
			}
		}
		// open site (row, col) if it is not open already
	}

	private boolean isTopToBeOpened(int row, int col) {
		return row == 1;
	}

	private boolean isBottomToBeOpened(int row, int col) {
		return row == n;
	}

	private boolean validate(int row, int col) {
		return row >= 1 && row <= n && col >= 1 && col <= n;
	}

	private int oneBasedIndex(int row, int col) {
		return (row - 1) * n + col;
	}

	public boolean isOpen(int row, int col) {
		if (!validate(row, col)) {
			throw new IllegalArgumentException();
		}
		return visited[oneBasedIndex(row, col) - 1];
		// is site (row, col) open?
	}

	public boolean isFull(int row, int col) {
		if (!validate(row, col)) {
			throw new IllegalArgumentException();
		}
		return fullSiteUF.connected(oneBasedIndex(row, col) - 1, top);
		// is site (row, col) full?
	}

	public int numberOfOpenSites() {
		return numberOfOpenSites;
		// number of open sites
	}

	public boolean percolates() {
		return unionUF.connected(top, bottom);
		// does the system percolate?
	}
}