package pso;

public class Polynomial {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    double low = -100.0, up = 100.0, step = 5E-4;
    double x, max = fit(low), max_x = low;
    for (x = low + step; x <= up; x += step)
      if (fit(x) > max) {
        max_x = x;
        max = fit(x);
      }
    System.out.printf("max fitness is : \n x = %.6f\nf(x) = %.16e\n", max_x, max);
  }

  public static double fit(double x) {
    return Math.abs(8000.0 + x * (-10000.0 + x * (-0.8 + x)));
  }

}
