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

package array.permutation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Leetcode47PermutationsII3 {
    private int[] ms;
    private List<List<Integer>> r;

    private void rotateNextChoice(int si, int ei) {
        int siv = ms[si];
        for (int i = si; i < ei; i++) {
            ms[i] = ms[i + 1];
        }
        ms[ei] = siv;
    }

    private void pNextNumber(int si, int ei) {
        if (si == ei) {
            List<Integer> p = new ArrayList(ms.length);
            for (int i = 0; i < ms.length; i++) {
                p.add(ms[i]);
            }
            r.add(p);
            return;
        }
        int choices = ei - si + 1;
        Set used = new HashSet(); // do not need sort input firstly
        while (choices-- >= 1) {
            if (!used.contains(ms[si])) {
                used.add(ms[si]);
                pNextNumber(si + 1, ei);
            }
            rotateNextChoice(si, ei);
        }
    }

    public List<List<Integer>> permuteUnique3(int[] in) {
        if (in == null) {
            return null;
        }
        ms = in;
        r = new ArrayList();
        pNextNumber(0, ms.length - 1);
        return r;
    }
}
