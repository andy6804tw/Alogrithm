# Genetic Algorithm

模仿達爾文的演化論，兩性生殖的優點是生出來的子代和父代差異是很大的，好的父代和母代交配出來之後產生出好的下一代這種特性才能用遺傳演算法。

```java
package Genetic;

import java.util.ArrayList;
import java.util.Arrays;

class Node{
	int chromosome[]=new int [16];
	int fitness;
	public Node(int[] chromosome,int fitness) {
		this.chromosome=chromosome.clone();
		this.fitness=fitness;
	}
}
public class Main {
	static ArrayList<Node> list;
	static int []key= {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0};
	public static void main(String[] args) {
		list=new ArrayList<>();
		GArun();
	}
	public static double random(int a,int b) {
		return a+Math.random()*(b-a);
	}
	public static int randomInt(int a,int b) {
		return (int)Math.round(random(a,b));
	}

	public static void GArun() {
		// 隨機產生Population 傳入值代表有多少基因
		initPopulation(100);
		mergeSort(list, 0, 99);
		for(int i=0;i<100;i++) {
			System.out.println("--------- Generation "+(i+1)+"---------");
			rePopulation(100);
			print();
		}
	}
	// 初始化 Population
	public static void initPopulation(int size) {
		for(int i=0;i<size;i++) {
			// random chromosome
			int chromosome[]=new int [16];
			for(int j=0;j<16;j++) {
				int bit=randomInt(0,1);
				chromosome[j]=bit; // 隨機給定一個0,1值
			}
			int fitness=calcFitness(chromosome);// fitness計算
			list.add(new Node(chromosome,fitness));
		}
	}
	// rePopulation
	public static void rePopulation(int size) {
		// new population
		ArrayList<Node> newList=new ArrayList<>();
		for(int i=0;i<size;i++) {
			// select parent
			int parent1[]=selection();
			int parent2[]=selection();
			// crossover
			int chromosome[]=crossover(parent1,parent2);
			int fitness=calcFitness(chromosome);// fitness計算
			newList.add(new Node(chromosome,fitness));
		}
		list=newList; //更新population(新一代)
		mergeSort(list, 0, 99); // 排序
	}
	// selection
	public static int[] selection() {
		int n=99; //GA population;
		int shoot=randomInt(0,n*n/2);
		int select=(int)Math.round(Math.sqrt(shoot*2));
		//System.out.println(select);
		return list.get(select).chromosome;
	}
	// fitness計算
	public static int calcFitness(int[] chromosome) {
		int fitness = 0;
		for (int i = 0; i < 16; i++) {
			if (key[i] == chromosome[i]) {
				fitness += 1; // 計算每個位元是否吻合key每一個值
			}
		}
		return fitness;
	}

	// crossover
	public static int[] crossover(int[] c1,int[] c2) {
		int chromosome[] = new int [16], cutIndex=randomInt(0,16);
		for(int i=0;i<16;i++) {
			if(i<cutIndex)
				chromosome[i]=c1[i];
			else
				chromosome[i]=c2[i];
		}
		return chromosome;
	}
	// 列出所有 Population
	public static void print() {
		for(int i=0;i<100;i++) {
			System.out.println(Arrays.toString(list.get(i).chromosome)+"  "+list.get(i).fitness);
		}
	}
	public static void mergeSort(ArrayList<Node> list, int left, int right) {
		if (left < right) { // 當左邊大於右邊時代表只剩一個元素了
			int mid = (left + right) / 2; // 每次對切，切到只剩一個為止
			mergeSort(list, left, mid); // 左邊等份
			mergeSort(list, mid + 1, right); // 右邊等份
			Merge(list, left, mid + 1, right); // 排序且合併
		}
	}

	public static void Merge(ArrayList<Node> list, int left, int mid, int right) {
		ArrayList<Node> temp = new ArrayList<>();// 建立一個temp串列存放排序後的值
		int left_end = mid - 1; // 左邊最後一個位置
		int index = left; // 位移起始點
		int origin_left = left; // 將最左邊的變數儲存起來(最後搬移元素會用到)
		for (int i = 0; i < right + 1; i++)
			temp.add(new Node(new int [16],0));

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
```
