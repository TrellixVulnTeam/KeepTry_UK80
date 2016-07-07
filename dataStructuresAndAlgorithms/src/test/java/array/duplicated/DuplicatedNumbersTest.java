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

package array.duplicated;

import org.junit.Assert;
import org.junit.Test;

import static array.duplicated.DuplicatedNumbers.duplicatedNums;
import static array.duplicated.DuplicatedNumbers.duplicatedNumsByMarkSign;

public class DuplicatedNumbersTest {

    @Test(timeout = 1000L, expected = Test.None.class)
    public void test() {
        Assert.assertArrayEquals(new int[]{1, 3, 6}, duplicatedNums(new int[]{1, 2, 3, 1, 3, 6, 6}));
        Assert.assertArrayEquals(new int[]{1, 3, 6}, duplicatedNumsByMarkSign(new int[]{1, 2, 3, 1, 3, 6, 6}));
        Assert.assertArrayEquals(new int[]{4}, duplicatedNums(new int[]{2, 4, 6, 4, 1, 0, 5}));
        Assert.assertArrayEquals(new int[]{4}, duplicatedNumsByMarkSign(new int[]{2, 4, 6, 4, 1, 0, 5}));
    }
}
