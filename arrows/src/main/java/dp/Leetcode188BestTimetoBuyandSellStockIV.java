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

import java.util.Arrays;

public class Leetcode188BestTimetoBuyandSellStockIV {
  /*
  an array for which the ith element is the price of a given stock on day i.
  find the maximum profit. You may complete
  at most k transactions.
  you must sell the stock before you buy again.
   */

  /*
   Idea: state machine.
     when K is bigger enough the answer is profit got by no times limitation transactions
     any times of transaction on the first day will get profit 0, as only one price
     the price diff is 0.
     Idea same as `123. Best Time to Buy and Sell Stock III` just extended the column
     so just column and column to update from top to downside

     Details:
     min-cost[k]:  may not happen( by buy action) on today
              it is affected by the previous 0~k-1 transaction.
        initial value is MAX.

     max-profit[k]: is the max profit at most k transactions
        The sell action of the kth transaction with max profit may not happen
        today and in that case it has been calculated and kept in max-profix[].
        if the sell action of the ith transaction with max profit happens
        today, it depends on the just calculated cost[k].
        initial value is 0

    candidate-cost[k] in i days depends on
       - left:  candidate-cost[k] in i-1 days
       - above: max-profit[k-1] in i days
    max-profit[k] in i days depends on
       - left:  max-profit[k] in i-1 days
       - above: candidate-cost[k] in i days


  Notice:
  - the inner loop order it make sure
       " you must sell the stock before you buy again."
  - the inner loop times to make sure providing K chance to make sure
       "at most k transactions."

   O(N*K) time and O(K) space
  */
  public static int maxProfit(int K, int[] prices) {
    if (K == 0 || prices == null || prices.length <= 1) return 0;
    int[] c = new int[K]; // c[i] min cost for ith T ( transaction) it depends on m[i-1] when i>0
    Arrays.fill(c, Integer.MAX_VALUE); // default MAX

    int[] m = new int[K]; // m[i] max profit money for at most i times T, default 0

    // column by column update
    for (int p : prices) {
      for (int k = 0; k < K; k++) {
        c[k] = Math.min(c[k], p - (k == 0 ? 0 : m[k - 1]));
        m[k] = Math.max(m[k], p - c[k]);
      }
    }
    return m[K - 1];
  }
  /*
  The logic is same as above
            -1    day 0,    ...,      c-1,          c,           ..., N-1
   T  1
   T  2
   T  3
   ...       0   p(r-1)(0), ...,      p(r-1)(c-1)    p(r-1)(c)
   T  r                               p(r)  (c-1)    p(r)  (c)
   ...
   T  k

   current the max profit `p(r)` after r transaction in first c days depends on
         - the max profit after r transaction in first c-1 days `p(r)(c-1)`
         - the cost of one of the rth transaction which happens in first
           c days, the one here is the one happens today or sold with
           today's price. The cost is the minimum value:
             price(c day)   - p(r-1)(c)
             price(c-1 day) - p(r-1)(c-1)
             ...
             price(1 day)   - p(r-1)(0)
             price(0 day)   - 0
   It shows:
       - if calculate  max profit(r)(c) column by column and use another array to keep min cost
         for the one of the ith transaction, the one that sell with today's price.
         then only need 1 dimension array p(r) for current c day.
         So the space is O(K)
       - if calculate max profit(r)(c) row by row then need, and we keep the above `min cost`
         only in a variable to also use 1 dimension array to keep p(c) for current
         transaction. it is used in this method
         So the space is O(N)
  */

  public static int maxProfit2(int K, int[] prices) {
    if (K == 0 || prices == null || prices.length <= 1) return 0;
    int N = prices.length;
    int pro[] = new int[N]; // pro[i] max profit got after current k transaction in i days

    for (int k = 0; k < K; k++) {
      int c = Integer.MAX_VALUE; // candidate-cost
      for (int i = 0; i < N; i++) {
        int p = prices[i];
        c = Math.min(c, p - (k == 0 ? 0 : pro[i]));
        pro[i] = Math.max(i == 0 ? 0 : pro[i - 1], p - c);
      }
    }
    return pro[N - 1];
  }
}
