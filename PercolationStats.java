import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

   private static final double CONFIDENCE_95 = 1.96;

   private final int n;
   private final int trials;
   private final double[] results;

   public PercolationStats(int n, int trials) {
      if (n <= 0)
         throw new IllegalArgumentException("n must be greater than zero.");

      if (trials <= 0)
         throw new IllegalArgumentException("trials must be greater than zero.");
      
      this.n = n;
      this.trials = trials;
      double totalSites = n * n;
      results = new double[trials];

      for (int t = 0; t < trials; t++) {
         int openSites = runPercolation();
         double threshold = openSites / totalSites;
         results[t] = threshold;
      }
   }

   public double mean() {
      return StdStats.mean(results);
   }

   public double stddev() {
      return StdStats.stddev(results);
   }

   public double confidenceLo() {
      double mean = mean();
      double stddev = stddev();
      return mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
   }

   public double confidenceHi() {
      double mean = mean();
      double stddev = stddev();
      return mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
   }

   public static void main(String[] args) {
      int n = Integer.parseInt(args[0]);
      int trials = Integer.parseInt(args[1]);
      PercolationStats stats = new PercolationStats(n, trials);

      StdOut.println(String.format("mean = %f", stats.mean()));
      StdOut.println(String.format("stddev = %f", stats.stddev()));
      StdOut.println(String.format(
         "95%% confidence interval = [%f, %f]",
         stats.confidenceLo(),
         stats.confidenceHi()));
   }

   private int runPercolation() {
      Percolation p = new Percolation(n);
      
      while (!p.percolates()) {
         int i = StdRandom.uniform(1, n * n);
         int[] rc = indexToRc(i);
         p.open(rc[0], rc[1]);
      }

      return p.numberOfOpenSites();
   }

   private int[] indexToRc(int index) {
      int[] rc = new int[2];
      rc[0] = (index - 1) / n + 1;
      rc[1] = (index - 1) % n + 1;
      return rc;
   }
}