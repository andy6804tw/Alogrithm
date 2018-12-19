# 第一章-介紹演算法的設計與分析
對 Order 概念直觀與正規的介紹

## 分析解決問題的技巧
1. 線性搜尋(Sequential Search) 

    worst case : n

2. 二元搜尋(Binary Search)

    worst case : log<sub>2</sub><sup>n</sup>+1

3. 費氏數列(Fibonacci)

  - 遞迴: 
  
    worst case : <sub>2</sub><sup>n/2</sup>

  - 迴圈(迭代): 
  
    worst case : n+1

## 時間複雜度分析
針對特定演算法，精確計量時間複雜度和空間複雜度。

漸近符號表示方法：Big-O( Ο )、Omega( Ω )、Theta( θ )

- 大寫的英文字母 O 函數，代表演算法執行步驟數目上限
- 大寫的希臘字母 Ω 函數，代表下限
- 大寫的希臘字母 Θ 函數，代表同時滿足上限與下限（介於中間）
> 小結: 最佳、平均、最壞 => Ω 、 Θ 、 O 

時間複雜度往往無法正確反映演算法的快慢。例如 n=100 的情況下，有可能 O(n³) 的演算法表現的比 O(n²) 好。

ref: http://www.csie.ntnu.edu.tw/~u91029/AlgorithmAnalysis.html

![](./../Screenshot/img1-1.jpg)

### 大O記號(Big-O)
是漸近上界(Asymptotic Upper Bound)一種漸近記號(asymptotic notation)表示演算法的時間複雜度(time complexity)。

Big-Ο代表演算法時間函式的上限(Upper bound)，在最壞的狀況下，演算法的執行時間不會超過Big-Ο。


```
格式：f(n) = Ο(g(n)) iff ∃正常數c和n0，使得f(n) ≤ c × g(n), ∀ n >= n0
n：輸入資料大小
f(n)：理想狀況下，程式在電腦中實際執行指令次數
g(n)：執行時間的成長率
若輸入資料量(n)比(n0)多時，則時間函數f(n)必會小於等於g(n)
e.g. 
      f(n)=3n²+2
      g(n)=n²
      ⇒n0=2,c=4
      ∴f(n)=O(n²)
```

### Omega( Ω )
Ω代表演算法時間函式的下限(Lower bound)
在最好的狀況下，演算法的執行時間不會比Omega快

當輸入資料量大到一定程度時，則c×g(n)必定會小於實際執行指令次數。
cg(n)相當於f(n)的下限，在最好情況下(Best Case)，f(n)的成長率最多到g(n)，而不會低於它

```
格式：f(n) = Ω(g(n)) iff ∃正常數c和n0，使得f(n) ≥ c × g(n), ∀ n >= n0
n：輸入資料大小
f(n)：理想狀況下，程式在電腦中實際執行指令次數
g(n)：執行時間的成長率
若輸入資料量(n)比(n0)多時，則時間函數f(n)必會大於等於g(n)

```

### Theta( Θ )
Θ代表演算法時間函式的上限與下限


```
格式：f(n) = Θ(g(n)) iff ∃正常數c1,c2和n0，使得c1 × g(n) ≤ f(n) ≤ c2 × g(n) , ∀ n ≥ n0
n：輸入資料大小
f(n)：理想狀況下，程式在電腦中實際執行指令次數
g(n)：執行時間的成長率
c1×g(n)：下限 ⇒ Ω
c2×g(n)：上限 ⇒ Ο
```
