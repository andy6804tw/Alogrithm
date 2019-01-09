# 01-Knapsack(Genetic Algorithm)

## 基因演算法各步驟

### 架構
首先初始化 Population 的所有的個體，依據使用者輸入可以自由定義 Population 的大小。接著第二步驟計算每一個個體的 Fitness ，這邊的 Fitness 即為在背包限重內所獲得的總利益值。第三步驟開始進行演化首先使用輪盤法則挑選父代與母代，個體被選中的概率與其 Fitness 成正比，所以 Fitness 越大被選重的機率就越高。父代與母代經由輪盤法則挑選完畢後接著進行 Crossover，這邊是以單點交叉來實作。交叉完畢後最後會經由使用者所設定的突變率來判斷是否要對個體產生突變，突變的用意是為了避免最佳值落在區域最佳當中。演化完成後會把 Population 中比較優良的個體保留起來接著回到第二步驟計算 Fitness 並再一次新的演化直到使用者所設定的最大演化代數，或是在連續N代中最佳值都一樣，才停止迴圈並列印出結果。

![](https://i.imgur.com/MYycv1B.png)


### Chromosome (染色體)
在01背包中物品的數量代表每個染色體(Chromosome)的長度，每一個基因以0或1表示1代表該物品要拿;反之0代表該物品不拿。下圖範例為一個 Chromosome 內部含有六個基因，每一個基因代表每一個物品要不要拿。

![](https://i.imgur.com/fmKs9gm.png)



### Population Initialization
初始化的方式為建立N個染色體個體並隨機用亂數將每個基因賦予初始值，建立好之後會檢查該筆染色體的組合是否超出設定的最大重量(W)，若超重則拋棄並重新亂數產生出新的一條染色體;直到數量到達使用者所設定的 Population 的大小即結束初始化。

![](https://i.imgur.com/bn8BZct.png)


```java=
void initPopulation(ArrayList popList,int popSize, int geneSize) {
    int i <- 0
    // 隨機產生 popSize 個染色體
    while (i less than popSize) {
        // 每一個染色體內的基因亂數隨機產生
        int chromosome[]
        for  (j = 0 to geneSize-1) {
            // 隨機給定一個0或1值
            chromosome[j] <- Random select 0 or 1
        }
        // 檢查重量是否超出設定的最大重量W
        if (weight less than or equal to max weight) {
            popList.add(chromosome)
            i++
        }
    }
}
```



### Calculation of Fitness
在每次繁殖過程中需要計算每個染色體的Fitness(適應值)，在每個染色體的基因串列中1代表該物品要拿，所以就將該物品的重量(Weight)和利益值(Profit)分別記錄並累加起來。加總完成後檢查該染色體基因組合的背包重量是否超出設定的最大重量 maxWeight，若超重則回傳0;反之回傳總利益值做為該染色體基因組合的Fitness。

![](https://i.imgur.com/RHmc7uA.png)


```java=
int calcFitness(int[] chromosome,ArrayList itemList,int maxWeight) {
    int weight <- 0 // 物品總重量
    int profit <- 0 // 物品總價值
    for (i = 0 to geneSize-1) {
        if (chromosome[i] equal 1) {
            weight <- GET i-th weight in itemList
            profit <- GET i-th profit in itemList 
        }
    }
    if (weight is better than maxWeight)
        return 0
    else {
        return profit
    }
}
```


### Selection
選擇父代與母代染色體來產生下一代，這邊選擇的方式使用 `輪盤選擇法` (Roulette Wheel Selection) 來實作。所謂的輪盤選擇法就是在整個族群中，每個個體存活下來或是可以產生後代的機率和個體分數成正比。也就是說 Fintness 越大的個體存活下來被選擇到的機率就越大，但弱勢個體也有存活的可能，不是絕對的淘汰。就想像個體放在飛標靶上，而個體分數就對應到個體擁有的標靶面積。

![](https://i.imgur.com/7VAaWal.png)
![](https://i.imgur.com/8puNZ9N.png)
![](https://i.imgur.com/QJnsfzV.png)

```java=
int selection(ArrayList popList) {
    double probability <- 0 // 機率
    double totalSum <- 0 // 全部 Fitness 總和的變數
    double randNum <- Randomly generate decimals between 0 and 1 // 0~1亂數
    double partialSum <- 0 // 累積機率總和

    // 計算所有的 Fitness 總和
    for (i = 0 to popSize-1) {
        totalSum <- GET i-th fitness in popList
    }
    // 計算每個的機率
    for (i = 0 to popSize-1) {
        popList(i).probability <- (GET i-th fitness in popList) / totalSum
    }
    // partialSum 機率依序加總直到隨機挑選出來的 randNum 小於等於 partialSum
    // 就把所索引值回傳表示選擇到這一個
    for (i = 0 to popSize-1) {
        partialSum <- GET i-th probability in popList;
        if (randNum less than or equal to partialSum) {
            return i
        }
    }
    return 0
}
```



### Crossover
所謂 Crossover 是指對兩個(父、母代)配對的染色體相互交換其部分基因，從而形成新的個體。在這邊是使用隨機單點基因交換，並且隨機交換 N 次。

![](https://i.imgur.com/SYIZvd7.png)


```java=
int[][] crossover(int[] chromosome1, int[] chromosome2) {
   int chromosome[][]
   // 亂數取得 Crossover 次數
   int crossoverNum <-Randomly generate an integer between 0 and chromosome length-1
   for (i = 0 to crossoverNum-1) {
       // 每次隨機選擇一個基因交換，一共交換 crossoverNum 次。
       int exIndex <- Randomly generate an integer between 0 and chromosome length-1
       int tempGene <- c1[exIndex]
       chromosome1[exIndex] <- c2[exIndex]
       chromosome2[exIndex] <- tempGene
   }
   chromosome[0] <- chromosome1
   chromosome[1] <- chromosome2
   return chromosome
}
```


### Mutation
在演化的過程中有一定的機率突變，突變的時機在當父母繁殖 Crossover 後產生出來的新子代，此時新的子代經由突變率會有一定的機率突變。這裡突變的方式是隨機將某一個基因做交換(0變1;1變0)。

![](https://i.imgur.com/FfCKVqa.png)


```java=
int[] mutate(int[] chromsome) {
    // 隨機挑選一位1變0 0變1
    int index <- Randomly generate an integer between 0 and chromosome length-1
    chromsome[index] = 1 - chromsome[index]
    return chromsome
}
```

### Survivor Selection
每一代演化後要保留比較好的個體作為下一次演化的 Population ，這裏挑選的方式是。首先排序按照 Fitness 數值由小到大排列，接著從裡面挑選前 90% 大的個體先挑選起來，剩下的 10% 的個體使用亂數隨機挑選。這麼做的原因是要確保我們的最佳解不會落在區域最佳。

![](https://i.imgur.com/DMQaNcx.png)


```java=
ArrayList survivorSelect(ArrayList popList, int popSize) {
    // 依據 Fitness 做排序(大->小)
    Sort(popList)
    // new Population
    ArrayList newList
    // 前 90% 大的個體先挑選起來，剩下的 10% 的個體使用亂數隨機挑選
    for (i = 0 to popSize - 1) {
        if (i less than (popSize * 0.9))
            newList.add(GET i-th chromosome in popList)
        else
            newList.add(Randomly GET chromosome in popList)
    }
    return newList
}
```


## 程式碼解說
### Item 類別
此類別建立是存放的所有的物品，分別記錄著 `item` 物品編號、`profit` 物品的利益值、`weight` 物品的重量。以及建立一個建構子每一次新增一個物品時即初始化並放入 ArrayList 串列中儲存。

```java=
class Item {
	int item, profit, weight;

	public Item(int item, int weight, int profit) {
		this.item = item;
		this.profit = profit;
		this.weight = weight;
	}
}
```

### Chromosome 類別
此類別是紀錄染色體的每個基因，總共有三個變數分別為 `chromosome[]` 存放染色體基因的陣列，這裡是以整數型態的陣列從放基因;接著是 `fitness` 適應值變數，此變數是存放每一組染色體的 `profit` 值;最後一個變數是 `probability` 機率，此變數記錄著該染色體在目前群體中所佔的比例是多少。最後一樣建立建構子當產生新的染色體時並初始化上述所有的變數。

```java=
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
```

### GAknapsack 類別
此類別是 Genetic Algorithm 的實例，分別進行Population的初始化以及選擇->交配->突變。由於此類別比較複雜故將每一個 Function 拆開逐一來解釋。

#### 全域變數
在此類別中分別建立以下變數。第一個為 Population List 使用 ArrayList 儲存每一個 Chromosome(染色體)。第二個為背包的物品依樣是使用 ArrayList 來儲存每一個 Item。第三個為背包的最大重量 `maxWeight` 。第四個是染色體基因數量 `geneSize`。第五個是Population 數量 `popSize`。第六個是突變率 `mutationRate`。第七個是最佳解 `solution[]` 此變數是一個整數的陣列型態記錄著每次演化後的最佳的一組解(染色體)。最後一個變數為 `solFitness` 此變數記錄著最佳解的 Profit 背包物品的利益值。
```java=
class GAknapsack {
    ArrayList < Chromosome > popList; // Population List
    ArrayList < Item > itemList; // 背包物品
    int maxWeight; // 背包最大重量
    int geneSize; // 染色體基因數量
    int popSize; // Population 數量
    int maxGen; // 演化代數
    double mutationRate; // 突變率
    int[] solution; // 最佳解
    int solFitness = 0; // 最佳 Fitness


    public GAknapsack(ArrayList < Item > itemList, int N, int maxWeight, double mutationRate, int maxGen, int popSize) {
        this.itemList = itemList;
        this.popList = new ArrayList < > ();
        this.geneSize = N; // 基因的數量經由背包物品的總數量得出
        this.maxWeight = maxWeight;
        this.mutationRate = mutationRate;
        this.maxGen = maxGen; // 演化N代 若maxGen=0時，則連續100代的最佳值都相同時當作收斂故跳出迴圈
        this.popSize = popSize;
    }
}
```

### random() 函式
此函式是利用 Math 函式庫中的 `random()` 函式來隨機產生a~b間的浮點數，此函式擁有兩個傳入值分別為a(起始值)與b(最終值)，最後將隨機產生出來的浮點數回傳。

```java=
// 隨機產生a~b間的浮點數
private double random(int a, int b) {
    return a + Math.random() * (b - a);
}
```

### randomInt() 函式
此函式跟上述的 `random()` 函式類似只不過是將回傳值強制轉型成整數(int)型態。此函式擁有兩個傳入值分別為a(起始值)與b(最終值)，最後將隨機產生出來的整數回傳。

```java=
// 隨機產生a~b間的整數
private int randomInt(int a, int b) {
    return (int) Math.round(random(a, b));
}
```
 
## 測試
Input:
輸入的第一行程式會要求使用者輸入物品數量(N)。第二行輸入最大的背包重量上限(maxWeight)。接著第三行開始會有連續N筆資料輸入，每一行請輸入兩個數值;第一個為物品的重量(weight)、第二個數為此物品的利益值(profit)。

Output:
首先會依序印出所有物品的重量(weight)與利益值(profit);接著會顯示此次GA執行的參數設定(若要更改參數要在程式裡做修改)，這裡會依序列出群體大小、突變率、以及終止條件(演化最大代數)。演化結束後會印出GA後的結果第一行為此次執行最佳的一組染色體(Chromosome);接著輸出最佳解的背包物品組合;最後則是背包內商品的最大利益值。

- 測試1

測資:
```
11
30
3 9
2 5
5 14
4 11
6 16
3 8
4 13
1 3
7 15
8 23
9 29
```

![](https://i.imgur.com/7hcI9mY.png)


Profit: 9+14+13+3+23+29 = 91

- 測試2

測資:
```
20
6844
1999 1
901 4
2227 5
532 4
3554 4
1815 5
3292 2
3093 4
2442 3
393 2
3657 5
3694 4
2283 5
366 5
3626 2
3924 2
81 3
3570 3
551 2
124 1
```

![](https://i.imgur.com/fTrPfHh.png)


Profit: 4+5+4+5+2+5+3+1 = 29

- 測試三

測資:
```
6
30
15 15
3 7
2 10
5 5
9 8
20 17
```

![](https://i.imgur.com/Dy7azIL.png)

Profit: 15+7+10+8 = 40
