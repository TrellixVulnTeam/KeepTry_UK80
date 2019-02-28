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

package java_util_concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


public class VolatileTest {
    //volatile only guarantee the visibility, and kind of ordering, but not sure the atomic.

    private static volatile int v = 0;
    private static volatile long vl = 0;
    private static int a = 0;

    public static synchronized void syncincrease() {
        a++; // with 'syncronized' to make sure atomic operation
    }

    public static synchronized int readSynced() {
        return a; // read is also need synchronized
    }

    public static void main(String[] args) throws InterruptedException {
        int testNumber = 1800;
        CountDownLatch c = new CountDownLatch(1);
        CyclicBarrier b =
                new CyclicBarrier(
                        testNumber,
                        () -> {
                            System.out.println("All are done");
                            System.out.println("v: " + v);
                            System.out.println("vl: " + vl);
                            System.out.println("a: " + readSynced());
                        });

        for (int i = 0; i < testNumber; i++) {
            new Thread(
                            new Runnable() {
                                CyclicBarrier barrier;
                                CountDownLatch c;

                                public Runnable set(CountDownLatch cdl, CyclicBarrier barrier) {
                                    this.c = cdl;
                                    this.barrier = barrier;
                                    return this;
                                }

                                @Override
                                public void run() {
                                    try {
                                        c.await();
                                        Thread.currentThread().sleep(100l);
                                        v++;vl++;
                                        syncincrease();
                                        barrier.await();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.set(c, b))
                    .start();
        }
        c.countDown();
    }
}
