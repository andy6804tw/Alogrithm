## Intractability
For a problem to be intractable, there must be no polynomial-time algorithm that solves it. 

For example, the brute-force algorithm for the  Chained Matrix Multiplication problem is nonpolynomial time. However, the same problem can be solved by dynamic programming algorithm in Θ(_𝑛_3_).

就難以處理性而言，有三大類問題：
1. 已找到多項式時間算法的問題。
2. 已被證明難以處理的問題。
3. 尚未被證明是棘手的問題，但從未找到過多項式時間算法。
例如，旅行銷售員決策問題可能在P？ 即使沒有人創建過解決這個問題的多項式時間算法，但沒有人能夠證明它不能用多項式時間算法求解。

沒有被證明是難以處理的問題，但是從未發現過多項式時間算法。
1.  Traveling Salesperson Problem
2.  0–1 Knapsack Problem 
3.  Graph–Coloring Problem 
4.  Clique Problem 

## P and NP
定義𝑃是可以通過多項式時間算法求解的所有決策問題的集合。
我們發現多項式時間算法的所有決策問題肯定都在𝑃。
