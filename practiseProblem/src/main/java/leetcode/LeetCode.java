//  Copyright 2016 The Minorminor Open Source Project
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

package leetcode;

public class LeetCode {
    // Leetcode 27 Remove Element
    // runtime beats 73.63% of java submissions.
    public static int removeElement(int[] nums, int val) {
        int newIndex = 0;
        int i = 0;
        while (i < nums.length) {
            int current = nums[i];
            if (current != val) {
                nums[newIndex++] = nums[i];
            }
            i++;
        }
        return newIndex;
    }


    // Leetcode 50 pow(x,n)
    // recursion
    // the total number of operations is O(logn)
    // its memory usage is O(logn) as well.
    public static double power(double x, int n) {
        if (x == 1) {
            return 1;
        }

        if (x == -1) {
            return (n & 1) == 1 ? -1 : 1;
        }

        boolean negative = false;
        if (n < 0) {
            negative = true;
            n = -n;
        }

        if (n == 0) {
            return 1;
        }
        double result = power(x, n >> 1);
        result *= result;
        if ((n & 1) == 1) {
            result *= x;
        }
        return negative ? 1 / result : result;
    }

    // Leetcode 50 pow(x,n)
    // nonrecursive implementation
    // its not the fast one, only runtime beats 29.88% of java submissions on 2/1/2016.
    static double power2(double x, int n) {
        if (x == 1) {
            return 1;
        }

        if (x == -1) {
            return (n & 1) == 1 ? -1 : 1;
        }

        boolean negative = false;
        if (n < 0) {
            negative = true;
            n = -n;
        }

        double result = 1;
        while (n > 0) {
            if ((n & 1) == 1) {
                result *= x;
            }
            x *= x;
            n >>= 1;
        }
        return negative ? 1 / result : result;
    }
}
