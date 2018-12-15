
遞迴演算法則的複雜度分析
- 數學解法 (Mathematics-based method)
- 替代法 (Substitution method)
- 遞迴樹法 (Recursion tree method)
- 支配定理法 (Master theorem method)

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


## 舉例(費氏數列)
### Recursive Factorial Algorithm

```
inputs: num identified the ordinal of the Fibonacci number
outputs: returns the nth Fibonacci number
void Fib(int num)
{
     if (num is 0 OR num is 1)
         return num;
     else
         return (Fib(num-1) + Fib(num-2));
}
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
