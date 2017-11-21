package shenoyg.math.matrix;

public class Matrix {
	private double[][] a;
	private double[][] b;
	private double[][] c;

	public double[][] getA() {
		return a;
	}

	public void setA(double[][] a) {
		this.a = a;
	}

	public double[][] getB() {
		return b;
	}

	public void setB(double[][] b) {
		this.b = b;
	}

	private double[][] getC() {
		return c;
	}

	public void print(double[][] x) {
		int m = x.length;
		int n = x[0].length;
		System.out.println();
		for (int i = 0; i < m; i++) {
			System.out.print("| ");
			for (int j = 0; j < n; j++) {
				System.out.printf("%9.3f ", x[i][j]);
			}
			System.out.println(" |");
		}
	}

	public double[][] transpose(double[][] x) {
		int m = x.length;
		int n = x[0].length;
		double[][] t = new double[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				t[j][i] = x[i][j];
			}
		}
		return t;
	}

	public double[][] multiply(double[][]... args) {
		if (args.length == 2) {
			setA(args[0]);
			setB(args[1]);
		}
		int m = a.length;
		int n = a[0].length;
		int p = b.length;
		int q = b[0].length;
		if (n != p) {
			throw new IllegalArgumentException(
					"Arguments specified are incompatible for multiplication");
		}
		c = new double[m][q];
		double sum = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < q; j++) {
				for (int k = 0; k < p; k++) {
					sum += a[i][k] * b[k][j];
				}
				c[i][j] = sum;
				sum = 0;
			}
		}
		return getC();
	}

	public static void main(String[] args) {
		double[][] A = {{0.1, 0.8, 0.1}, {0.7, 0.2, 0.1}};
		double[][] R = {{1, 0, 1.2}, {1, 1.2, 0}, {0.1, 0, 1}};
		Matrix mult = new Matrix();
		double[][] At = mult.transpose(A);

		mult.print(A);
		mult.print(R);
		mult.print(At);

		double[][] ar = mult.multiply(A, R);
		mult.print(ar);

		double[][] arat = mult.multiply(ar, At);
		mult.print(arat);

		System.out.println();
	}
}
