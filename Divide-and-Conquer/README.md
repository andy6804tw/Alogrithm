
遞迴演算法則的複雜度分析
- 數學解法 (Mathematics-based method)
- 替代法 (Substitution method)
- 遞迴樹法 (Recursion tree method)
- 支配定理法 (Master theorem method)
http://mropengate.blogspot.com/2015/01/algorithm-ch1-asymptotic-notation-and.html

一般來說，有兩種方式可以撰寫具有重覆執行 (Repetitive)特性的演算法
- Iteration (迴圈)
- Recursion (遞迴)

## 遞迴架構

```
Procedure 遞迴副程式名(參數)
{
  if (base case)
     return(結果); ……//達到終止條件時結束遞迴，需要時回傳結果
  else 
     general case; ……//利用general case執行遞迴呼叫，需要時加上return
}
```

> 遞迴比較沒有效率


## 舉例(階層)
### Recursive Factorial Algorithm

```
inputs: n is the number being raised factorially
outputs: n! is returned
Procedure Factorial(int n)
begin
     if (n = 0)
         return 1;
     else
         return (n Factorial(n-1));
end
```

### Iterative Factorial Algorithm

```
inputs: n is the number being raised factorially
outputs: n! is returned
void Factorial(int n)
{
     factN = 1;
     for (i=1, i ≤ n, i++)
         factN = factN * i;
     return factN;
}
```


常用的數列總和複雜度一覽
- 等差數列： 1+2+3+…+n=O(n2)  
- 等比數列： r+r2…+rn=O(rn)
- 2次方數列：12+22+…+n2=O(n3)
- d次方數列：1d+2d+…+nd=O(nd+1)
http://mropengate.blogspot.com/2015/04/algorithm-ch1-master-theorem-super.html

## 遞迴樹與時間複雜度分析
遞迴的思想就是將大問題一層一層地分解為小問題來求解，如果我們把這個分解過程畫成圖，它其實就是一棵樹，我們稱之為遞迴樹。

以下是一些遞迴關係常用的解法。

### Mathematics-based Method
數學解法是直接將遞迴方程式以遞迴的觀念由最末項往前求解，然後整理出答案。

```
Sol:
    T(n) = T(n-1) + n 
         = (T(n-2) + (n-1)) + n 
         = T(n-2) + (n-1) + n
         = (T(n-3) + (n-2)) + (n-1) + n 
         = T(n-3) + (n-2) + (n-1) + n
                …
         = T(1) + 2 + 3 + … + (n-2) + (n-1) + n
         = 1 + 2 + 3 + … + (n-2) + (n-1) + n

T(n) = n(n+1)/2       時間複雜度: O(n²)                                          
```

### Substitution method
適用於檢驗某個候選解答是否為此遞迴演算法的正確解

1. 利用猜測、觀察或匯整的方式，找出遞迴方程式的解
2. 利用數學歸納法証明此解是正確的


### Recursion-tree method
1. 依照遞迴定義展開
2. 對每一層的cost加總，得到per-level cost 
3. 加總per-level cost，得到total cost

![](../Screenshot/img2-1.jpg)


### Master Method
```
T(n) = aT(n/b) + f(n) ,  where a≥1, b> 1, and f is asymptotically positive. 
f (n) = O (nlogba–ε)for some constant ε> 0.
f (n) grows polynomially slower than nlogba (by nε factor)
Solution: T(n) = Θ(nlogba)
 
f (n) = Θ(nlogba lgkn)for some constant k≥0
f (n) and nlogba–ε grow at similar rates. 
Solution: T(n) = Θ(nlogba  lgk+1n).
T(n) = 3T(n / 3) + n log n , answer: T(n) = Θ(n log2n) 
  
f (n) grows polynomially faster than nlogba (by nε factor)
and f (n) satisfies regularity condition that af(n/b) ≤ cf(n) for some constant c< 1.
Solution: T(n) = Θ(f(n)).  
```

http://mropengate.blogspot.com/2015/04/algorithm-ch1-master-theorem-super.html


