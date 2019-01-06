package Genetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Item {
  int item, value, weight, unit;

  public Item(int item, int weight, int value, int unit) {
    this.item = item;
    this.value = value;
    this.weight = weight;
    this.unit = unit;
  }
}

class Chromosome {
  int chromosome[]; // 染色體
  int fitness; // 適應值(背包重量)
  double probability; // 機率

  public Chromosome(int[] chromosome, int fitness, double probability) {
    this.chromosome = chromosome.clone();
    this.fitness = fitness;
    this.probability = probability;
  }
}

class GAknapsack {
  ArrayList<Chromosome> popList; // Population
  ArrayList<Item> itemList; // 背包物品
  int maxWeight; // 背包最大重量
  int geneSize; // 染色體數量
  int popSize; // Population 數量
  int maxGen; // 演化幾代
  double mutationRate; // 突變率
  int maxFitness = 0; // 最大 Fitness
  int[] solution; // 最佳解

  public GAknapsack(ArrayList<Item> itemList, int N, int maxWeight, double mutationRate, int maxGen, int popSize) {
    this.itemList = itemList;
    this.popList = new ArrayList<>();
    this.geneSize = N; // 基因的數量經由背包物品的總數量得出
    this.maxWeight = maxWeight;
    this.mutationRate = mutationRate;
    this.maxGen = maxGen; // 演化N代 若maxGen=0時，則連續100代的最佳值都相同時當作收斂故跳出迴圈
    this.popSize = popSize;
  }

  // 隨機產生a~b間的浮點數
  private double random(int a, int b) {
    return a + Math.random() * (b - a);
  }

  // 隨機產生a~b間的整數
  private int randomInt(int a, int b) {
    return (int) Math.round(random(a, b));
  }

  // 執行GA演算法
  public void GArun() {
    // 隨機產生Population 傳入值代表有多少基因(Population)以及染色體的(Chromosome)基因個數
    initPopulation(popSize, geneSize);
    // print();
    // 複製、選擇->交配->突變 (循環重複到 maxGen 代)
    if (maxGen != 0) {
      for (int i = 0; i < maxGen; i++) {
        System.out.println("--------- Generation " + (i + 1) + " ---------");
        evolution(popSize);
        print();
      }
      System.out.println("\n|  Population Size  |   Mutate Rate   | Max Generation |");
      System.out.println("----------------------------------------------------------");
      System.out.printf("%10d %20s %15d\n", popSize, Double.toString(mutationRate), maxGen);
    }
    // 若maxGen=0時，則連續100代的最佳值都相同時當作收斂故跳出迴圈
    else {
      int count = 0, maxValue = 0, genCount = 0;
      while (true) {
        System.out.println("--------- Generation " + (genCount) + " ---------");
        evolution(popSize);
        print();
        if (maxValue == maxFitness) {
          count++;
        } else {
          maxValue = maxFitness;
          count = 0;
        }
        if (count > 100)
          break;
        genCount++;
      }
      System.out.println("\n|  Population Size  |   Mutate Rate   | Max Generation |");
      System.out.println("-----------------------------------------");
      // System.out.printf("%10d %20s\n", popSize, Double.toString(mutationRate));
      System.out.printf("%10d %20s %15d\n", popSize, Double.toString(mutationRate), genCount);
    }

    // 印出 GA 後找出來的最佳解
    System.out.print("\nSolution => ");
    for (int i = 0; i < geneSize; i++)
      System.out.print(solution[i] + " ");
    // 印出 GA 後找出來的最佳 Fitness
    System.out.println("\nMax Value: " + maxFitness);
  }

  // 首次執行初始化 Population (隨機產生子代)
  private void initPopulation(int popSize, int geneSize) {
    for (int count = 0; count < popSize;) {
      // random Chromosome
      int chromosome[] = new int[geneSize];
      for (int j = 0; j < geneSize; j++) {
        int bit = randomInt(0, 1);
        chromosome[j] = bit; // 隨機給定一個0,1值
      }
      int fitness = calcFitness(chromosome);// fitness計算
      if (fitness != 0) {
        // 尋找最佳值
        if (maxFitness < fitness) {
          maxFitness = fitness; // 目前最大Fitness
          solution = chromosome.clone(); // 目前最佳解
        }
        popList.add(new Chromosome(chromosome, fitness, 0));
        count++;
      }
    }
  }

  // evolution (產生下一代)
  private void evolution(int popSize) {
    // new Population
    ArrayList<Chromosome> newList = new ArrayList<>();
    for (int i = 0; i < popSize; i += 2) {
      // select parent
      int chromosome1[] = popList.get(selection()).chromosome;
      int chromosome2[] = popList.get(selection()).chromosome;

      // Crossover
      int crossChromosome[][] = crossover(chromosome1, chromosome2);
      chromosome1 = crossChromosome[0];
      chromosome2 = crossChromosome[1];
      // 有一定機率突變
      if (random(0, 1) < this.mutationRate) {
        chromosome1 = mutate(chromosome1);
        chromosome2 = mutate(chromosome2);
      }
      // fitness計算
      int fitness1 = calcFitness(chromosome1);
      int fitness2 = calcFitness(chromosome2);
      // 並放回 Population
      if (fitness1 != 0)
        popList.add(new Chromosome(chromosome1, fitness1, 0));
      if (fitness2 != 0)
        popList.add(new Chromosome(chromosome2, fitness2, 0));
      if (maxFitness < fitness1) {
        maxFitness = fitness1; // 目前最大Fitness
        solution = chromosome1.clone(); // 目前最佳解
      }
      if (maxFitness < fitness2) {
        maxFitness = fitness2; // 目前最大Fitness
        solution = chromosome2.clone(); // 目前最佳解
      }
    }
    // Population產生後要依據 Fitness 做排序(小->大)
    mergeSort(popList, 0, popList.size() - 1);
    // 更新Population(新一代)
    for (int i = popList.size() - 1, count = 0; count < popSize; i--, count++) {
      if (count < popSize * 0.9)
        newList.add(popList.get(i));
      else
        newList.add(popList.get(randomInt(0, popList.size() - 1)));
    }
    popList = newList;

  }

  // Selection (輪盤選擇法)
  private int selection() {
    double probability = 0; // 機率
    double totalSum = 0; // 全部 Fitness 總和的變數
    double randNum = random(0, 1); // 0~1 中隨機取得亂數的變數
    double partialSum = 0; // 目前機率加總的變數

    // 計算所有的 Fitness 總和
    for (int i = 0; i < popSize; i++) {
      totalSum += popList.get(i).fitness;
    }

    // 計算每個的機率
    for (int i = 0; i < popSize; i++) {
      probability = popList.get(i).fitness / totalSum;
      popList.set(i, new Chromosome(popList.get(i).chromosome, popList.get(i).fitness, probability));
    }

    // partialSum 機率依據加總直到隨機挑選出來的 randNum 小於等於 partialSum
    // 就把所索引值回傳代表選擇到這一個
    // 0~1 中隨機取得亂數(機率)
    for (int i = 0; i < popList.size(); i++) {
      partialSum += popList.get(i).probability;
      if (randNum <= partialSum) {
        return i;
      }
    }
    return 0;
  }

  // Fitness 計算
  private int calcFitness(int[] chromosome) {
    int weight = 0;
    int value = 0;
    for (int i = 0; i < geneSize; i++) {
      if (chromosome[i] == 1) {
        weight += itemList.get(i).weight; // 計算每個位元是否吻合key每一個值
        value += itemList.get(i).value; // 計算 value 物品價值
      }
    }

    if (weight > maxWeight)
      return 0;
    else {
      return value;
    }
  }

  // Crossover (交配)
  private int[][] crossover(int[] c1, int[] c2) {
    int chromosome[][] = new int[2][geneSize];
    int crossoverNum = randomInt(0, geneSize - 1); // crossover次數
    for (int i = 0; i < crossoverNum; i++) {
      // 每次選擇一個基因交換，一共交換 crossoverNum 次。
      int exIndex = randomInt(0, geneSize - 1);
      int gene = c1[exIndex];
      c1[exIndex] = c2[exIndex];
      c2[exIndex] = gene;

    }
    chromosome[0] = c1;
    chromosome[1] = c2;
    return chromosome;
  }

  // Mutate (突變)
  private int[] mutate(int[] chromsome) {
    // 隨機挑選一位1變0 0變1
    int index = randomInt(0, chromsome.length - 1);
    chromsome[index] = 1 - chromsome[index];
    return chromsome;
  }

  // 列出所有 Population
  private void print() {
    for (int i = 0; i < popSize; i++) {
      System.out.println(Arrays.toString(popList.get(i).chromosome) + "  " + popList.get(i).fitness);
    }
  }

  // 合併排序
  private void mergeSort(ArrayList<Chromosome> list, int left, int right) {
    if (left < right) { // 當左邊大於右邊時代表只剩一個元素了
      int mid = (left + right) / 2; // 每次對切，切到只剩一個為止
      mergeSort(list, left, mid); // 左邊等份
      mergeSort(list, mid + 1, right); // 右邊等份
      Merge(list, left, mid + 1, right); // 排序且合併
    }
  }

  private void Merge(ArrayList<Chromosome> list, int left, int mid, int right) {
    ArrayList<Chromosome> temp = new ArrayList<>();// 建立一個temp串列存放排序後的值
    int left_end = mid - 1; // 左邊最後一個位置
    int index = left; // 位移起始點
    int origin_left = left; // 將最左邊的變數儲存起來(最後搬移元素會用到)
    for (int i = 0; i < right + 1; i++)
      temp.add(new Chromosome(new int[geneSize], 0, 0));

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

public class Knapsack {
  static ArrayList<Item> itemList;
  static int N = 0, maxWeight = 0;

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);
    itemList = new ArrayList<>();
    System.out.print("請輸入物品數量: ");
    N = Integer.parseInt(scn.nextLine());
    System.out.print("請輸入最大重量上限: ");
    maxWeight = Integer.parseInt(scn.nextLine());
    int weight = 0, value = 0, unit = 0;
    System.out.println("請輸入每個物品的Weight 和 Profit: ");
    for (int i = 0; i < N; i++) {
      String arr[] = scn.nextLine().split(" ");
      weight = Integer.parseInt(arr[0]);
      value = Integer.parseInt(arr[1]);
      unit = Integer.parseInt(arr[1]) / Integer.parseInt(arr[0]);
      itemList.add(new Item(i + 1, weight, value, unit));
    }

    System.out.println("\n| Item | Weight | Profit | Unit |");
    System.out.println("-----------------------------------");
    for (int i = 0; i < N; i++) {
      System.out.printf("%4d %8d %8d %7d\n", itemList.get(i).item, itemList.get(i).weight, itemList.get(i).value,
          itemList.get(i).unit);
    }
    // 物品、物品數量、背包最大承重、突變率、演化幾代、Population 數量
    GAknapsack gaKnapsack = new GAknapsack(itemList, N, maxWeight, 0.15, 0, 100);
    gaKnapsack.GArun();
  }
}
