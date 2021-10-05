//  Copyright 2016 The Sawdust Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package dp;

public class Leetcode123BestTimetoBuyandSellStockIII {

  /*
  You are given an array prices where prices[i]
  is the price of a given stock on the ith day.
  Find the maximum profit you can achieve.

  - You may complete at most two transactions.
  - you must sell the stock before you buy again).

   1 <= N <= 105
   0 <= prices[i] <= 105
   */

  /*
    divide this problem into two subproblems, and each subproblem is
    actually of the same problem of `Best Time to Buy and Sell Stock`
      Example 1:

   Run cases:
   prices = [3,3,5,0,0,3,1,4]
   Output: 6.  Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
   Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.

   prices = [1,2,3,4,5]
   Output: 4. Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
   Note that you cannot buy on day 1, buy on day 2 and sell them later,
   as you are engaging multiple transactions at the same time. You must sell before buying again.

   prices = [7,6,4,3,1]
   Output: 0. In this case, no transaction is done, i.e. max profit = 0.

   Input: prices = [1]
   Output: 0

  "at most two transactions." means 0, or 1 or 2 transactions
  "you must sell the stock before you buy again." but this can happen on one day
  e.g. buy on day a, then sell, buy, sell, on day b.
  only restrict the order not day.
  O(N) time and space
   */
  public int maxProfit(int[] p) {
    if (p.length == 0) return 0;
    int N = p.length;
    int[] maxl = new int[N]; // maxl[i] and l is the max one transaction profit in [0,i]
    int l = 0, min = Integer.MAX_VALUE;
    for (int i = 0; i < N; i++) {
      l = Math.max(l, p[i] - min); // calculate profit firstly before update min
      min = Math.min(min, p[i]);

      maxl[i] = l;
    }

    int a = Integer.MIN_VALUE; //  a=left_profits[0,i]+right_profits[i,N-1]
    // r is the max one transaction profit in the scope[i,N-1]
    int r = 0, max = Integer.MIN_VALUE;
    for (int i = N - 1; i >= 0; i--) {
      r = Math.max(r, max - p[i]); // calculate profit firstly before update max
      max = Math.max(p[i], max);

      a = Math.max(a, r + maxl[i]);
    }
    return a;
  }

  /* --------------------------------------------------------------------------
  The two transactions be decomposed into 4 actions:
  "buy of transaction  #1",
  "sell of transaction #1",
  "buy of transaction  #2"
  "sell of transaction #2".
  Possible to buy->sell->buy->sell on the same day.
   Run cases:
   day       1 2 3 4 5 6 7 8
   prices = [3,3,5,0,0,3,1,4]
   Output: 6.  Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
   Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.

   prices = [1,2,3,4,5]
   Output: 4. Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
   Note that you cannot buy on day 1, buy on day 2 and sell them later,
   as you are engaging multiple transactions at the same time. You must sell before buying again.

   prices = [7,6,4,3,1]
   Output: 0. In this case, no transaction is done, i.e. max profit = 0.

   Input: prices = [1]
   Output: 0

  Note:

      the order to calculate c1, p1, c2, p2 to make sure
      "at most two transactions." means 0, or 1 or 2 transactions
      "you must sell the stock before you buy again." but this can happen on one day

      More general concept refer to Leetcode188BestTimetoBuyandSellStockIV
      c1: minimum value of cost / price that we have seen so far at each step
      m1: the maximum profit, money, of one transaction in [0, i] day scope, this value would be the answer
          for the first problem in the series `Best Time to Buy and Sell Stock`
      c2: minimum value of cost of reinvest the gained profit in the second transaction
          Similar with transaction_1_cost, we try to find the lowest price so far,
          which in addition would be partially compensated by the profits gained
          from the first transaction.
      m2:  the maximal profits with at most two transactions at each step.

  Idea: Trying to find the minimum cost for first and second T
        the second minimum cost depends on the max money of first T

  O(N) time O(1) space
    */
  public static int maxProfit2(int[] prices) {
    int c1 = Integer.MAX_VALUE, c2 = Integer.MAX_VALUE;
    int m1 = 0, m2 = 0;

    for (int p : prices) {
      c1 = Math.min(c1, p); // min cost
      m1 = Math.max(m1, p - c1); // max money at most 1 T
      c2 = Math.min(c2, p - m1); // min cost, update by today price- max money of T1 in [0,today].
      m2 = Math.max(m2, p - c2); // max money at most 2 Ts
    }
    return m2;
  }
}
