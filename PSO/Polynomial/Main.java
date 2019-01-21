package pso;

import java.util.ArrayList;

class Particle {
  double position; /* 目前位置, 即x value */
  double velocity; /* 目前粒子速度 */
  double fitness; /* 適應函式值 */
  double pbest_pos; /* particle 目前最好位置 */
  double pbest_fit; /* particle 目前最佳適應值 */

  public Particle(double position, double velocity, double fitness, double pbest_pos, double pbest_fit) {
    this.position = position;
    this.velocity = velocity;
    this.fitness = fitness;
    this.pbest_pos = pbest_pos;
    this.pbest_fit = pbest_fit;
  }
}

public class Main {
  static double w, c1, c2; /* 相關權重參數 */
  static double max_v; /* 最大速度限制 */
  static double max_pos, min_pos; /* 最大,小位置限制 */
  static int particle_cnt; /* 粒子數量 */
  static Particle gbest; /* 全域最佳值 */
  static ArrayList<Particle> list = new ArrayList<>(); /* 全域最佳值 */
  static int max_itera = 100000; /* max_itera : 最大演化代數 */

  public static void main(String[] args) {

    /* 設定參數 */
    min_pos = -100.0;
    max_pos = +100.0; /* 位置限制, 即解空間限制 */
    w = 1.00;
    c1 = 2.0;
    c2 = 2.0; /* 慣性權重與加速常數設定 */
    particle_cnt = 20; /* 設粒子個數 */
    max_v = (max_pos - min_pos) * 1.0; /* 設最大速限 */
    // 開始進行 PSO
    ParticleInit();
    for (int i = 0; i < max_itera; i++) // 進行迭代
      ParticleMove(); // 粒子移動
    ParticleDisplay(); // 顯示最後結果

    System.out.printf("know2: %10.6f , %f\n", -57.469, fit(-57.469));

  }

  // 隨機產生a~b間的浮點數
  public static double random(int a, int b) {
    return a + Math.random() * (b - a);
  }

  // 隨機產生a~b間的整數
  public static int randomInt(int a, int b) {
    return (int) Math.round(random(a, b));
  }

  // 計算 Fitness
  public static double fit(double x) {
    // x**3 - 0.8x**2 - 1000x + 8000
    return Math.abs(8000.0 + x * (-10000.0 + x * (-0.8 + x)));
  }

  // 粒子初始化
  public static void ParticleInit() {
    double pos_range = max_pos - min_pos; // 解寬度
    // 以下程式碼效率不佳, 但較易懂一點
    for (int i = 0; i < particle_cnt; i++) {
      // 隨機取得粒子位置, 並設為該粒子目前最佳適應值
      double position = random(0, 1) * pos_range + min_pos;
      // 隨機取得粒子速度
      double velocity = random(0, 1) * max_v;
      // 計算該粒子適應值, 並設為該粒子目前最佳適應值
      double fitness = fit(position);
      list.add(new Particle(position, velocity, fitness, position, fitness));

      // 全域最佳設定
      if (i == 0 || list.get(i).pbest_fit > gbest.fitness)
        gbest = list.get(i);

    }
  }

  // 開始移動
  public static void ParticleMove() {
    double v, pos; // 暫存每個粒子之速度, 位置用
    double ppos, gpos; // 暫存區域及全域最佳位置用
    gpos = gbest.position;
    // 更新速度與位置
    for (int i = 0; i < particle_cnt; i++) {
      v = list.get(i).velocity; // 粒子目前速度
      pos = list.get(i).position; // 粒子目前位置
      ppos = list.get(i).pbest_pos; // 粒子目前曾到到最好位置

      v = w * v + c1 * random(0, 1) * (ppos - pos) + c2 * random(0, 1) * (gpos - pos); // 更新速度
      if (v < -max_v)
        v = -max_v; // 限制最大速度
      else if (v > max_v)
        v = max_v; // 限制最大速度

      pos = pos + v; // 更新位置
      if (pos > max_pos)
        pos = max_pos; // 限制最大位置
      else if (pos < min_pos)
        pos = min_pos; // 限制最小位置
      double pbest_pos = list.get(i).pbest_pos;
      double pbest_fit = list.get(i).pbest_fit;
      // 更新該粒子目前找過之最佳值
      if (list.get(i).fitness > list.get(i).pbest_fit) {
        list.set(i, new Particle(pos, v, fit(pos), pos, fit(pos)));
      } else {
        list.set(i, new Particle(pos, v, fit(pos), pbest_pos, pbest_fit));
      }

      // 更新全域最佳值
      if (list.get(i).fitness > gbest.fitness)
        gbest = list.get(i);
    }

  }

  // 顯示所有粒子資訊
  public static void ParticleDisplay() {
    System.out.printf("best : %10.6f , %f\n", gbest.position, gbest.fitness);
  }

}
