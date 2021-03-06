# 01-Knapsack(Genetic Algorithm)

## 基因演算法各步驟

### 架構
首先初始化 Population 的所有的個體，依據使用者輸入可以自由定義 Population 的大小。接著第二步驟計算每一個個體的 Fitness ，這邊的 Fitness 即為在背包限重內所獲得的總利益值。第三步驟開始進行演化首先使用輪盤法則挑選父代與母代，個體被選中的概率與其 Fitness 成正比，所以 Fitness 越大被選重的機率就越高。父代與母代經由輪盤法則挑選完畢後接著進行 Crossover，這邊是以單點交叉來實作。交叉完畢後最後會經由使用者所設定的突變率來判斷是否要對個體產生突變，突變的用意是為了避免最佳值落在區域最佳當中。演化完成後會把 Population 中比較優良的個體保留起來接著回到第二步驟計算 Fitness 並再一次新的演化直到使用者所設定的最大演化代數，或是在連續N代中最佳值都一樣，才停止迴圈並列印出結果。

![](https://i.imgur.com/ZSs2mai.png)

```java=
void GAknapsack() {
    // Population Initialization
    initPopulation()
    for (i = 0 to maxGeneration-1) {
        // evolution
        Selection()
        Crossover()
        Mutation()
        SurvivorSelect()
    }
    // Terminate and print solution
}
```



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
    double percentage <- 0 // 比例
    double totalSum <- 0 // 全部 Fitness 總和的變數
    double randNum <- Randomly generate decimals between 0 and 1 // 0-1亂數
    double partialSum <- 0 // 累積比例總和

    // 計算所有的 Fitness 總和
    for (i = 0 to popSize-1) {
        totalSum <- GET i-th fitness in popList
    }
    // 計算每個Fitness佔有的比例
    for (i = 0 to popSize-1) {
        popList(i).percentage <- (GET i-th fitness in popList) / totalSum
    }
    // partialSum 比例依序加總直到隨機挑選出來的 randNum 小於等於 partialSum
    // 就把所索引值回傳代表此一輪選擇到這個
    for (i = 0 to popSize-1) {
        partialSum <- GET i-th percentage in popList;
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
int[] mutation(int[] chromsome) {
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
此類別是紀錄染色體的每個基因，總共有三個變數分別為 `chromosome[]` 存放染色體基因的陣列，這裡是以整數型態的陣列從放基因;接著是 `fitness` 適應值變數，此變數是存放每一組染色體的 `profit` 值;最後一個變數是 `percentage` 比例，此變數記錄著該染色體在目前群體中所佔的比例是多少。最後一樣建立建構子當產生新的染色體時並初始化上述所有的變數。

```java=
class Chromosome {
	int chromosome[]; // 染色體
	int fitness; // 適應值(背包重量)
	double percentage; // 比例

	public Chromosome(int[] chromosome, int fitness, double percentage) {
		this.chromosome = chromosome.clone();
		this.fitness = fitness;
		this.percentage = percentage;
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

#### random() 函式
此函式是利用 Math 函式庫中的 `random()` 函式來隨機產生a-b間的浮點數，此函式擁有兩個傳入值分別為a(起始值)與b(最終值)，最後將隨機產生出來的浮點數回傳。

```java=
// 隨機產生a-b間的浮點數
private double random(int a, int b) {
    return a + Math.random() * (b - a);
}
```

#### randomInt() 函式
此函式跟上述的 `random()` 函式類似只不過是將回傳值強制轉型成整數(int)型態。此函式擁有兩個傳入值分別為a(起始值)與b(最終值)，最後將隨機產生出來的整數回傳。

```java=
// 隨機產生a-b間的整數
private int randomInt(int a, int b) {
    return (int) Math.round(random(a, b));
}
```

#### initPopulation() 函式
基因演算法第一步驟是初始化 Population (隨機產生子代)，傳入值分別有 Population 大小`popSize`，以及染色體基因的長度 `geneSize`。

初始化的方式為建立 `popSize` 個染色體個體並隨機用亂數將每個基因賦予初始值(程式6-9行)，建立好之後會呼叫 `calcFitness()` 計算 Fitness 並檢查該筆染色體的組合是否超出設定的最大重量(maxWeight)，若超重則 `calcFitness()` 函式會回傳 0 因此不將此組染色體放入 Population List 當中並重新亂數產生出新的一條染色體;直到數量到達使用者所設定的 Population 的大小即結束初始化。

```java=
// 首次執行初始化 Population (隨機產生子代)
private void initPopulation(int popSize, int geneSize) {
    for (int count = 0; count < popSize;) {
        // Random Chromosome
        int chromosome[] = new int[geneSize];
        for (int j = 0; j < geneSize; j++) {
            int bit = randomInt(0, 1);
            chromosome[j] = bit; // 隨機給定一個0,1值
        }
        int fitness = calcFitness(chromosome); // fitness計算
        if (fitness != 0) {
            // 尋找最佳值
            if (solFitness < fitness) {
                solFitness = fitness; // 目前最大Fitness
                solution = chromosome.clone(); // 目前最佳解
            }
            popList.add(new Chromosome(chromosome.clone(), fitness, 0));
            count++;
        }
    }
}
```

#### calcFitness() 函式
在每次繁殖過程中需要計算每個染色體的Fitness(適應值)，在每個染色體的基因串列中1代表該物品要拿，所以就將該物品的重量(Weight)和利益值(Profit)分別記錄並累加起來(程式5-10行)。加總完成後檢查該染色體基因組合的背包重量是否超出設定的最大重量 maxWeight(程式12-16行)，若超重則回傳0;反之回傳總利益值做為該染色體基因組合的 Fitness。


```java=
// Fitness 計算
private int calcFitness(int[] chromosome) {
    int weight = 0;
    int profit = 0;
    for (int i = 0; i < geneSize; i++) {
        if (chromosome[i] == 1) {
            weight += itemList.get(i).weight; // 物品重量
            profit += itemList.get(i).profit; // 物品價值
        }
    }
    // 若背包重量超出設定的 maxWeight 就回傳0
    if (weight > maxWeight)
        return 0;
    else {
        return profit;
    }
}
```

#### selection() 函式
選擇父代與母代染色體來產生下一代，這邊選擇的方式使用 `輪盤選擇法` (Roulette Wheel Selection) 來實作。所謂的輪盤選擇法就是在整個族群中，每個個體存活下來或是可以產生後代的機率和個體分數成正比。也就是說 Fintness 越大的個體存活下來被選擇到的機率就越大。

首先計算所有的染色體 Fitness 總和並且儲存在 `totalSum`  變數當中(程式9-11行)，接著在分別計算每個染色體在整個群體中所佔有的比例，計算的方式為(目前某一個染色體的Fitness)/totalSum (程式13-16行)。全部的染色體都計算好後接著計算每個Fitness佔有的比例(程式13-16行)。最後依據隨機產撐出來的變數 `randNum` 產生 0-1 之間的小數，進行每一個 Fitness 比例的累加直到隨機挑選出來的 `randNum` 小於等於 `partialSum` 就把目前的所索引值回傳代表此一輪選擇到這個。

```java=
// Selection (輪盤選擇法)
private int selection() {
    double percentage = 0; // 比例
    double totalSum = 0; // 全部 Fitness 總和變數
    double randNum = random(0, 1); // 隨機產生0-1之間的小數
    double partialSum = 0; // 累積比例總和

    // 計算所有的 Fitness 總和
    for (int i = 0; i < popSize; i++) {
        totalSum += popList.get(i).fitness;
    }
    // 計算每個Fitness佔有的比例
    for (int i = 0; i < popSize; i++) {
        percentage = popList.get(i).fitness / totalSum;
        popList.set(i, new Chromosome(popList.get(i).chromosome.clone(), popList.get(i).fitness, percentage));
    }

    // partialSum 比例依序加總直到隨機挑選出來的 randNum 小於等於 partialSum
    // 就把所索引值回傳代表此一輪選擇到這個
    for (int i = 0; i < popList.size(); i++) {
        partialSum += popList.get(i).percentage;
        if (randNum <= partialSum) {
            return i;
        }
    }
    return 0;
}
```

#### crossover() 函式
所謂 Crossover 是指對兩個(父、母代)配對的染色體相互交換其部分基因，從而形成新的個體。在這邊是使用隨機單點基因交換，並且隨機交換 N 次。

此函式會有兩個參數 `c1`、`c2` 分別代表父代與母代，首先會有一個 `crossoverNum` 變數進行亂數初始化，接著進入迴圈每次隨機選擇一個基因交換，一共交換 crossoverNum 次，並且每一次做單點的基因交換。

```java=
private int[][] crossover(int[] c1, int[] c2) {
    int chromosome[][] = new int[2][geneSize];
    int crossoverNum = randomInt(0, geneSize - 1); // 亂數決定crossover次數
    for (int i = 0; i < crossoverNum; i++) {
        // 每次隨機選擇一個基因交換，一共交換 crossoverNum 次。
        int exIndex = randomInt(0, geneSize - 1);
        int tempGene = c1[exIndex];
        c1[exIndex] = c2[exIndex];
        c2[exIndex] = tempGene;
    }
    chromosome[0] = c1;
    chromosome[1] = c2;
    return chromosome;
}
```
#### mutation() 函式
在演化的過程中有一定的機率突變，突變的時機在當父母繁殖 Crossover 後產生出來的新子代，此時新的子代經由突變率會有一定的機率突變。這裡突變的方式是隨機將某一個基因做交換(0變1;1變0)。

```java=
// Mutation (突變)
private int[] mutation(int[] chromsome) {
    // 隨機挑選一個基因1變0 0變1
    int index = randomInt(0, chromsome.length - 1);
    chromsome[index] = 1 - chromsome[index];
    return chromsome;
}
```

#### evolution() 函式
此函式為 repopulation 實例進行演化，分別處理 `Select` (程式11-18行) 進行輪盤選擇法挑選父代與母代，接著進行 `Crossover` 隨機做單點交換(程式10-13行)，交叉完成後隨著突變率有一定的機率突變(程式15-18行)。上述三個動作都完成後進行 Fitness 計算並檢查此染色體是否超重，若沒超重則將染色體放回去 Population List 中(程式碼23-26)。

最後每一代演化後要保留比較好的個體作為下一次演化的 Population ，這裏挑選的方式是。首先排序按照 Fitness 數值由小到大排列，接著從裡面挑選前 90% 大的個體先挑選起來，剩下的 10% 的個體使用亂數隨機挑選。這麼做的原因是要確保我們的最佳解不會落在區域最佳(程式碼37-45)。

```java=
// evolution (演化新子代)
private void evolution(int popSize) {
    // new Population
    ArrayList < Chromosome > newList = new ArrayList < > ();
    for (int i = 0; i < popSize; i++) {
        // Select parent
        int chromosome1[] = popList.get(selection()).chromosome.clone();
        int chromosome2[] = popList.get(selection()).chromosome.clone();

        // Crossover
        int crossChromosome[][] = crossover(chromosome1, chromosome2);
        chromosome1 = crossChromosome[0].clone();
        chromosome2 = crossChromosome[1].clone();
        // 有一定機率突變
        if (random(0, 1) < this.mutationRate) {
            chromosome1 = mutation(chromosome1).clone();
            chromosome2 = mutation(chromosome2).clone();
        }
        // fitness計算
        int fitness1 = calcFitness(chromosome1);
        int fitness2 = calcFitness(chromosome2);
        // 若沒超重則放回 Population
        if (fitness1 != 0)
            popList.add(new Chromosome(chromosome1.clone(), fitness1, 0));
        if (fitness2 != 0)
            popList.add(new Chromosome(chromosome2.clone(), fitness2, 0));
        if (solFitness < fitness1) {
            solFitness = fitness1; // 目前最大Fitness
            solution = chromosome1.clone(); // 目前最佳解
        }
        if (solFitness < fitness2) {
            solFitness = fitness2; // 目前最大Fitness
            solution = chromosome2.clone(); // 目前最佳解
        }
    }
    // 依據 Fitness 做排序(大->小)
    mergeSort(popList, 0, popList.size() - 1);
    // 前 90% 大的個體先挑選起來，剩下的 10% 的個體使用亂數隨機挑選
    for (int i = 0; i < popSize; i++) {
        if (i < popSize * 0.9)
            newList.add(popList.get(i));
        else
            newList.add(popList.get(randomInt(0, popList.size() - 1)));
    }
    popList = newList;
}
```
 
#### mergeSort() 排序函式
由於每次演化後要挑選比較好的子代做為下一次演化的依據，所以我們要先將每個染色體內的 Fitness 由大到小做排序，這邊排序是使用 Merge Sort 合併排序實作。

```java=
// 合併排序
private void mergeSort(ArrayList < Chromosome > list, int left, int right) {
    if (left < right) { // 當左邊大於右邊時代表只剩一個元素了
        int mid = (left + right) / 2; // 每次對切，切到只剩一個為止
        mergeSort(list, left, mid); // 左邊等份
        mergeSort(list, mid + 1, right); // 右邊等份
        Merge(list, left, mid + 1, right); // 排序且合併
    }
}

private void Merge(ArrayList < Chromosome > list, int left,
int mid, int right) {
    // 建立一個temp串列存放排序後的值
    ArrayList < Chromosome > temp = new ArrayList < > (); 
    int left_end = mid - 1; // 左邊最後一個位置
    int index = left; // 位移起始點
    int origin_left = left; // 將最左邊的變數儲存起來(最後搬移元素會用到)
    for (int i = 0; i < right + 1; i++)
        temp.add(new Chromosome(new int[geneSize], 0, 0));

    while ((left <= left_end) && (mid <= right)) { 
    // 左右兩串列比大小依序放入temp串列中儲存
        if (list.get(left).fitness >= list.get(mid).fitness)
            temp.add(index++, list.get(left++));
        else
            temp.add(index++, list.get(mid++));
    }

    if (left <= left_end) { 
    // 若左邊的串列尚未走完將剩餘的數值依序放入temp串列中
        while (left <= left_end) {
            temp.add(index++, list.get(left++));
        }
    } else { 
    // 反之若右邊的串列尚未走完將剩餘的數值依序放入temp串列中
        while (mid <= right) {
            temp.add(index++, list.get(mid++));
        }
    }
    // 最後將排序好的temp串列複製到list串列中
    for (int i = origin_left; i <= right; i++) {
        list.set(i, temp.get(i));
    }

}
```

#### GArun() 函式
此函式是主程式中被呼叫基因演算法執行的函式，首先會進行 Population 初始化動作(程式第4行)。接著程式6-13行進行演化選擇->交配->突變 (循環重複到 maxGen 代)，此外若 maxGen=0 時，則會檢查連續100代的最佳值都相同時當作收斂故跳出迴圈。

最後是輸出結果，會顯示此次GA執行的參數設定，這裡會依序列出群體大小、突變率、以及終止條件(演化最大代數)。接著印出GA後的結果第一行為此次執行最佳的一組染色體(Chromosome);接著輸出最佳解的背包物品組合;最後則是背包內商品的最大利益值。

```java=
//執行GA演算法
public void GArun() {
 // 隨機產生 Population 傳入值代表有多少基因(Population)以及染色體的(Chromosome)基因個數
 initPopulation(popSize, geneSize);
 // 選擇->交配->突變 (循環重複到 maxGen 代)
 if (maxGen != 0) {
     for (int i = 0; i < maxGen; i++) {
         evolution(popSize);
     }
     System.out.println("\n|  Population Size  |   Mutate Rate   | Max Generation |");
     System.out.println("--------------------------------------------------------");
     System.out.printf("%10d %20s %15d\n", popSize, Double.toString(mutationRate), maxGen);
 }
 // 若maxGen=0時，則連續100代的最佳值都相同時當作收斂故跳出迴圈
 else {
     int count = 0, maxProfit = 0, genCount = 0;
     while (true) {
         evolution(popSize);
         if (maxProfit == solFitness) {
             count++;
         } else {
             maxProfit = solFitness;
             count = 0;
         }
         if (count > 100)
             break;
         genCount++;
     }
     System.out.println("\n|  Population Size  |   Mutate Rate   | Max Generation |");
     System.out.println("--------------------------------------------------------");
     System.out.printf("%10d %20s %15d\n", popSize, Double.toString(mutationRate), genCount);
 }

 // 印出 GA 後找出來的最佳解
 System.out.print("\nSolution => ");
 for (int i = 0; i < geneSize; i++)
     System.out.print(solution[i] + " ");
 // 列出背包的物品
 System.out.print("\nKnapsack Item => ");
 for (int i = 0; i < geneSize; i++) {
     if (solution[i] == 1)
         System.out.print(itemList.get(i).item + " ");
 }
 // 印出 GA 後找出來的最佳 Fitness
 System.out.println("\nProfit: " + solFitness);
}
```

### main() 主函式
在主函式中首先要求使用者輸入背包內容，輸入的第一行程式會要求使用者輸入物品數量(N)。第二行輸入最大的背包重量上限(maxWeight)。接著第三行開始會有連續N筆資料輸入，每一行請輸入兩個數值;第一個為物品的重量(weight)、第二個數為此物品的利益值(profit)。

接著程式第23行呼叫 GAknapsack 類別變數並初始化執行基因演算法，參數分別放入物品、物品數量、背包最大承重、突變率、演化代數、Population 數量。其中突變率、演化代數、Population 數量可以依照使用者喜好手動調整參數，若演化代數輸入0則終止條件會檢查連續100代的最佳值都相同時當作收斂故跳出迴圈並列印出結果。

```java=
public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);
    itemList = new ArrayList < > ();
    System.out.print("請輸入物品數量: ");
    N = Integer.parseInt(scn.nextLine());
    System.out.print("請輸入最大重量上限: ");
    maxWeight = Integer.parseInt(scn.nextLine());
    int weight = 0, profit = 0, unit = 0;
    System.out.println("請每行依序輸入每個物品的Weight 和 Profit(以空白隔開): ");
    for (int i = 0; i < N; i++) {
        String arr[] = scn.nextLine().split(" ");
        weight = Integer.parseInt(arr[0]);
        profit = Integer.parseInt(arr[1]);
        itemList.add(new Item(i + 1, weight, profit));
    }

    System.out.println("\n| Item | Weight | Profit |");
    System.out.println("--------------------------");
    for (int i = 0; i < N; i++) {
        System.out.printf("%4d %8d %8d\n", itemList.get(i).item, itemList.get(i).weight, itemList.get(i).profit);
    }
    // 物品、物品數量、背包最大承重、突變率、演化代數、Population 數量
    GAknapsack gaKnapsack = new GAknapsack(itemList, N, maxWeight, 0.15, 1000, 100);
    gaKnapsack.GArun();
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
