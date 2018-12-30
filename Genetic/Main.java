import java.util.ArrayList;
import java.util.Arrays;

class Node {
  int chromosome[];
  int fitness;

  public Node(int[] chromosome, int fitness) {
    this.chromosome = chromosome.clone();
    this.fitness = fitness;
  }
}

class GeneticAlogrithm {
  ArrayList<Node> list; // Population
  int[] key; // 解答
  int xyNum; // 染色體數量
  double mutationRate; // 突變率
  int maxGen; // 演化幾代
  int popSize; // Population 數量

  public GeneticAlogrithm(ArrayList<Node> list, int[] key, double mutationRate, int maxGen, int popSize) {
    this.list = list = new ArrayList<>();
    this.key = key.clone();
    this.xyNum = key.length; // 染色體(Chromosome)數量直接由解(key)得到
    this.mutationRate = mutationRate;
    this.maxGen = maxGen;
    this.popSize = popSize;
  }

  // 隨機產生a~b間的亂數
  private double random(int a, int b) {
    return a + Math.random() * (b - a);
  }

  // 隨機產生a~b間的整數
  private int randomInt(int a, int b) {
    return (int) Math.round(random(a, b));
  }

  // 隨機突變
  private int randomChoose(int[] array) {
    return array[randomInt(0, array.length - 1)];
  }

  // 運行GA演算法
  public void GArun() {
    // 隨機產生Population 傳入值代表有多少基因
    initPopulation(popSize, xyNum);
    // 複製、選擇->交配->突變 (循環重複到 maxGen 代)
    for (int i = 0; i < maxGen; i++) {
      System.out.println("--------- Generation " + (i + 1) + " ---------");
      rePopulation(popSize);
      print();
    }
  }

  // 第一次初始化 Population (隨機產生子代)
  private void initPopulation(int popSize, int xyNum) {
    for (int i = 0; i < popSize; i++) {
      // random Chromosome
      int chromosome[] = new int[xyNum];
      for (int j = 0; j < xyNum; j++) {
        int bit = randomInt(0, 1);
        chromosome[j] = bit; // 隨機給定一個0,1值
      }
      int fitness = calcFitness(chromosome);// fitness計算
      list.add(new Node(chromosome, fitness));
    }
    // Population產生後要依據 Fitness 做排序(小->大)
    mergeSort(list, 0, popSize - 1);
  }

  // rePopulation (產生下一代)
  private void rePopulation(int popSize) {
    // new Population
    ArrayList<Node> newList = new ArrayList<>();
    for (int i = 0; i < popSize; i++) {
      // select parent
      int parent1[] = selection();
      int parent2[] = selection();
      // Crossover
      int chromosome[] = crossover(parent1, parent2);
      // 有一定機率突變
      double prob = random(0, 1);
      // 如果亂數小於設定的突變率就進行突變
      if (prob < mutationRate) {
        chromosome = mutate(chromosome).clone();
      }
      // Fitness計算
      int fitness = calcFitness(chromosome);
      // 並放回 Population
      newList.add(new Node(chromosome, fitness));
    }
    // 更新Population(新一代)
    list = newList;
    // Population產生後要依據 Fitness 做排序(小->大)
    mergeSort(list, 0, popSize - 1);
  }

  // Selection (輪盤選擇法)
  private int[] selection() {
    int n = popSize - 1; // GA population;
    int shoot = randomInt(0, n * n / 2);
    int select = (int) Math.round(Math.sqrt(shoot * 2));
    return list.get(select).chromosome;
  }

  // Fitness 計算
  private int calcFitness(int[] chromosome) {
    int fitness = 0;
    for (int i = 0; i < xyNum; i++) {
      if (key[i] == chromosome[i]) {
        fitness += 1; // 計算每個位元是否吻合key每一個值
      }
    }
    return fitness;
  }

  // Crossover (交配)
  private int[] crossover(int[] c1, int[] c2) {
    int chromosome[] = new int[xyNum], cutIndex = randomInt(0, xyNum);
    for (int i = 0; i < xyNum; i++) {
      if (i < cutIndex)
        chromosome[i] = c1[i];
      else
        chromosome[i] = c2[i];
    }
    return chromosome;
  }

  // Mutate (突變)
  private int[] mutate(int[] chromsome) {
    int[] newChromosome = new int[xyNum];
    int index = randomInt(0, chromsome.length - 1);
    for (int i = 0; i < chromsome.length; i++) {
      if (i == index)
        newChromosome[i] = randomChoose(new int[] { 0, 1 });
      else
        newChromosome[i] = chromsome[i];
    }
    return newChromosome;
  }

  // 列出所有 Population
  private void print() {
    for (int i = 0; i < popSize; i++) {
      System.out.println(Arrays.toString(list.get(i).chromosome) + "  " + list.get(i).fitness);
    }
  }

  // 合併排序
  private void mergeSort(ArrayList<Node> list, int left, int right) {
    if (left < right) { // 當左邊大於右邊時代表只剩一個元素了
      int mid = (left + right) / 2; // 每次對切，切到只剩一個為止
      mergeSort(list, left, mid); // 左邊等份
      mergeSort(list, mid + 1, right); // 右邊等份
      Merge(list, left, mid + 1, right); // 排序且合併
    }
  }

  private void Merge(ArrayList<Node> list, int left, int mid, int right) {
    ArrayList<Node> temp = new ArrayList<>();// 建立一個temp串列存放排序後的值
    int left_end = mid - 1; // 左邊最後一個位置
    int index = left; // 位移起始點
    int origin_left = left; // 將最左邊的變數儲存起來(最後搬移元素會用到)
    for (int i = 0; i < right + 1; i++)
      temp.add(new Node(new int[xyNum], 0));

    while ((left <= left_end) && (mid <= right)) { // 左右兩串列比大小依序放入temp串列中儲存
      if (list.get(left).fitness <= list.get(mid).fitness)
        temp.add(index++, list.get(left++));
      else
        temp.add(index++, list.get(mid++));
    }

    if (left <= left_end) { // 若左邊的串列尚未走完將剩餘的數值依序放入temp串列中
      while (left <= left_end) {
        temp.add(index++, list.get(left++));
      }
    } else { // 反之若右邊的串列尚未走完將剩餘的數值依序放入temp串列中
      while (mid <= right) {
        temp.add(index++, list.get(mid++));
      }
    }
    // 最後將排序好的temp串列複製到list串列中
    for (int i = origin_left; i <= right; i++) {
      list.set(i, temp.get(i));
    }

  }
}

public class Main {
  public static void main(String[] args) {
    ArrayList<Node> list = new ArrayList<>();
    int[] key = { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 };
    // Population、解答、突變率、演化幾代、Population 數量
    GeneticAlogrithm gaKey = new GeneticAlogrithm(list, key, 0.01, 10, 25);
    gaKey.GArun();
  }

}
