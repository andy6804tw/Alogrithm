# 01-Knapsack(Genetic Algorithm)

## 基因演算法各步驟

### 架構
首先初始化 Population 的所有的個體，依據使用者輸入可以自由定義 Population 的大小。接著第二步驟計算每一個個體的 Fitness ，這邊的 Fitness 即為在背包限重內所獲得的總利益值。第三步驟開始進行演化首先使用輪盤法則挑選父代與母代，個體被選中的概率與其 Fitness 成正比，所以 Fitness 越大被選重的機率就越高。父代與母代經由輪盤法則挑選完畢後接著進行 Crossover，這邊是以單點交叉來實作。交叉完畢後最後會經由使用者所設定的突變率來判斷是否要對個體產生突變，突變的用意是為了避免最佳值落在區域最佳當中。演化完成後會把 Population 中比較優良的個體保留起來接著回到第二步驟計算 Fitness 並再一次新的演化直到使用者所設定的最大演化代數，或是在連續N代中最佳值都一樣，才停止迴圈並列印出結果。

![](https://i.imgur.com/MYycv1B.png)


### Chromosome (染色體)
在01背包中物品的數量代表每個染色體(Chromosome)的長度，每一個基因以0或1表示1代表該物品要拿;反之0代表該物品不拿。下圖範例為一個 Chromosome 內部含有六個基因，每一個基因代表每一個物品要不要拿。

![](https://i.imgur.com/ND2NZfd.png)



### Population Initialization
初始化的方式為建立N個染色體個體並隨機用亂數將每個基因賦予初始值，建立好之後會檢查該筆染色體的組合是否超出設定的最大重量W，若超重則拋棄並重新亂數產生出新的一條染色體;直到數量到達使用者所設定的 Population 的大小即可結束初始化動作。


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

```java=
int selection(ArrayList popList) {
    double probability <- 0 // 機率
    double totalSum <- 0 // 全部 Fitness 總和的變數
    double randNum <- Randomly generate decimals between 0 and 1 // 0~1亂數
    double partialSum <- 0 // 目前機率總和

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
            return i;
        }
    }
    return 0;
}
```



### Crossover
所謂 Crossover(交叉) 是指對兩個(父、母代)配對的染色體相互交換其部分基因，從而形成新的個體。在這邊是使用隨機單點基因交換，並且隨機交換 N 次。

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

```java=
ArrayList survivorSelect(ArrayList popList, int popSize) {
    // 依據 Fitness 做排序(大->小)
    Sort(popList)
    // new Population
    ArrayList newList
    // 前 90% 大的個體先挑選起來，剩下的 10% 的個體使用亂數隨機挑選
    for (i = 0 to popSize - 1) {
        if (i less than (popSize * 0.9))
            newList.add(GET i-th chromosome in popList);
        else
            newList.add(Randomly GET chromosome in popList);
    }
    return newList
}
```
