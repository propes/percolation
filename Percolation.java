import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private final int n;
    private final WeightedQuickUnionUF topUF;
    private final WeightedQuickUnionUF bottomUF;
    private final boolean[] isOpen;
    private final int lastIndex;
    private boolean percolates = false;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be greater than zero.");

        this.n = n;
        lastIndex = n * n;
        topUF = new WeightedQuickUnionUF(lastIndex + 1);
        bottomUF = new WeightedQuickUnionUF(lastIndex + 1);
        isOpen = new boolean[lastIndex + 1];
    }

    public void open(int row, int col) {
        validateRow(row);
        validateCol(col);

        int i = rcToIndex(row, col);
        isOpen[i] = true;

        if (row == 1) {
            topUF.union(0, i);
        }

        if (row == n) {
            bottomUF.union(0, i);
        }

        int[] adjIndexes = getAdjacentIndexes(row, col);
        for (int j = 0; j < adjIndexes.length; j++) {
            int adjIndex = adjIndexes[j]; 
            if (adjIndex > 0 && isOpen[adjIndex]) {
                topUF.union(i, adjIndex);
                bottomUF.union(i, adjIndex);
            }
        }

        if (topUF.find(0) == topUF.find(i) && bottomUF.find(0) == bottomUF.find(i)) {
            percolates = true;
        }
    }

    public boolean isOpen(int row, int col) {
        validateRow(row);
        validateCol(col);

        return isOpen[rcToIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateRow(row);
        validateCol(col);

        int i = rcToIndex(row, col);
        return topUF.find(i) == topUF.find(0);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 1; i <= lastIndex; i++) {
            if (isOpen[i]) count++;
        }
        return count;
    }

    public boolean percolates() {
        return percolates;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(20);
        p.open(1, 5);
        p.open(2, 5);

        StdOut.println(String.format(
            "Number of open sites: %d",
            p.numberOfOpenSites()));
    }

    private int rcToIndex(int row, int col) {
        return (row - 1) * n + col;
    }

    private void validateRow(int row) {
        if (row <= 0 || row > n) {
            throw new IllegalArgumentException("row index is out of bounds.");
        }
    }

    private void validateCol(int col) {
        if (col <= 0 || col > n) {
            throw new IllegalArgumentException("col index is out of bounds.");
        }
    }

    private int[] getAdjacentIndexes(int row, int col) {
        int[] indexes = new int[4];
        if (row != 1) indexes[0] = rcToIndex(row - 1, col);
        if (row != n) indexes[1] = rcToIndex(row + 1, col);
        if (col != 1) indexes[2] = rcToIndex(row, col - 1);
        if (col != n) indexes[3] = rcToIndex(row, col + 1);

        return indexes;
    }
}
