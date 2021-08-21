//  Copyright 2021 The KeepTry Open Source Project
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

package graph.directed_graphs.topological_sort;

import java.util.*;

/**
 * <pre>
 * Condition: Directed Acyclic Graph(DAG)
 * Only one result condition:
 *          If a topological sort has the property that all pairs of consecutive vertices in
 *          the sorted order  are connected by edges, then these edges form a directed
 *          Hamiltonian path in the DAG.
 *          If a Hamiltonian path exists, the topological sort order is unique;
 *
 * 1> Kahn's algorithm(BFS)
 *    See used in {@link Leetcode269AlienDictionary}
 *    Data structures:
 *      2 map<node, set<node>>: 'ins'(1:n), 'outs' (1:n)
 *                               ins can be map<node, Integer>. But this is not good choice as this need
 *                               to avoid duplicated operations to increase the Integer value
 *                               for 2 same relation or use map<node, list<ode> data structure for `out`
 *      set<node> all: all nodes in graph, this can be merged with indegree maps `ins`, but
 *                     not good as concept is not clean
 *      queue: nodes with no incoming edges.
 *      list/String (keep result)
 *
 *    Algorithm steps:
 *      initial `all` set
 *      find out all edge to initial `in` and `out`
 *      initial queue by all.removeAll(in.keySet())
 *      while(  queue  is not  empty) {
 *        BFS:
 *          if current node `k` has not out set `value`, provide a empty set, do not run into null pointer.
 *                                                       or initial out with an empty set during initialize the `all`
 *          remove key in ins if its value set is empty: no ins for this node
 *      }
 *      if  `ins` set is not empty
 *          return error (graph has at least one cycle)
 *
 *
 *    Algorithm  cons:
 *           need has ins;
 *           need find out all nodes(Kept in `all` set) and edges(kept in and out maps).
 *           need update the ins
 *
 *    Algorithm pros: can detect circle. do not need "visited" Set to check circle
 *    O(V+E) time and space
 *      todo A variation of Kahn's algorithm that breaks ties lexicographically forms a key component
 *      of the Coffman–Graham algorithm for parallel scheduling and layered graph drawing.
 *
 *  2> Depth-first search(Assume it is DAG. no circle, need
 *    - checking circle before running this algorithm)
 *    - or merge  checking circle with  topological sort order together see
 *      See used in {@link Leetcode269AlienDictionary}
 *
 *  data structure:
 *    Set: keep all nodes
 *    map: only outs
 *    stack:  to keep the result during on the back forward path
 *    Set: visited. avoid duplicated at merge point.
 *         Not be used to checking circle.
 *
 *   steps:  from each node to try DFS +  post order to keep result
 *
 *    cons:  need check circle firstly
 *    pros:  need not update the edge/connection relationship in indegree map, so no indegree map
 *
 *   O(V+E) time and space
 *  todo The topological order can also be used to quickly compute shortest paths through
 *  a weighted directed(negative wight is allowed) acyclic graph.
 *
 *   <a href="https://en.wikipedia.org/wiki/Topological_sorting"> wiki </a>
 *   <a href="https://www.youtube.com/watch?v=ddTC4Zovtbc"> DFS from youtube </a>
 *   <a href="http://www.cse.cuhk.edu.hk/~taoyf/course/2100sum11/lec14.pdf"> DFS </a>
 *
 *  --- circle ---
 *   o
 *    \
 *     o -o
 *     |  |
 *     o- o
 *    /
 *   o
 *
 *  --- no circle ---
 *    o
 *     \
 * o    o
 *  \ / \
 *   o   o
 *   |   |
 *   o   o
 *    \ /
 *     o
 */
public class TopologicalSort {
  // refer Leetcode210CourseScheduleii; Leetcode269AlienDictionary
  // 1> Kahn's algorithm  BFS -------------------------------------------------
  static class G {
    private List<Integer>[] out;
    private int[] in; // number of in degree nodes of the node whose ID is index of array in
    private int V; // number of Vertex which has ID from 0 to V-1

    public G(int V) {
      this.V = V;
      out = new List[V];
      for (int i = 0; i < V; i++) out[i] = new ArrayList();
      in = new int[V];
    }

    public G edge(int from, int to) {
      out[from].add(to);
      in[to]++;
      return this;
    }

    public List topologicalOrder() {
      Queue<Integer> q = new LinkedList();
      for (int i = 0; i < V; i++)
        if (in[i] == 0) q.add(i); // those nodes who have no in degree node(s)

      List<Integer> r = new ArrayList<>(V);
      // node Id
      while (!q.isEmpty()) {
        Integer n = q.poll();
        r.add(n);
        for (int o : out[n]) if (--in[o] == 0) q.add(o);
      }

      if (r.size() != V) return null; // there is circle
      return r;
    }
  }

  /*
  2> DFS -------------------------------------------------------------------
    DFS used only for DAG. So need check circle
    as do not care edge weight, make the data structure simple.
  */
  class Node {
    public char v; // unique ID
  }

  public String topologicalSortOrder(Map<Node, Set<Node>> out) {
    Map<Node, Boolean> v = new HashMap<>();
    for (Node o : out.keySet()) if (hasCircle(o, out, v)) return "";

    StringBuilder r = new StringBuilder();
    Set<Node> set = new HashSet(); // visited
    for (Node o : out.keySet()) dfs(o, out, r, set);
    return r.reverse().toString();
  }
  /*
   hasCircle`(1) with  `topological sort order`(2)
   Both (1) and（2）
     - start each node to try
     - visit all nodes and edges

   But:
   (1) requires to exit once meet visited node
   (2) requires stop once meet visited node.

   (1) requires clean tracks on the back forward path
   (2) requires never clean the visited record

   both function can be merged together
  */
  void dfs(Node n, Map<Node, Set<Node>> out, StringBuilder r, Set<Node> v) {
    if (v.contains(n)) return;
    v.add(n);
    for (Node o : out.get(n)) dfs(o, out, r, v);
    r.append(n); // Topological Order: Only all subtree are recorded can record current node.
  }

  // use a set to keep visited node in current path is enough, here use a map to compare
  // hasCircle() with dfs_merged()
  boolean hasCircle(Node n, Map<Node, Set<Node>> out, Map<Node, Boolean> v) {
    if (v.containsKey(n)) return v.get(n);
    v.put(n, true);
    for (Node o : out.get(n)) if (hasCircle(o, out, v)) return true;
    v.put(n, false);
    return false;
  }
  // merged 2 function: find circle(1) + calculate Topological Sort Order(2)
  boolean dfs_merged(Node n, Map<Node, Boolean> v, StringBuilder r, Map<Node, Set<Node>> out) {
    if (v.containsKey(n)) return v.get(n);
    v.put(n, true);
    for (Node o : out.get(n)) if (dfs_merged(o, v, r, out)) return true;
    v.put(n, false);
    r.append(n.v);
    return false;
  }
  // --------------------------------------------------------------------------
  public static void main(String[] args) {
    // graph is a case in algs4.cs.princeton.edu
    G g = new G(8);
    g.edge(0, 1)
        .edge(0, 7)
        .edge(0, 4)
        .edge(1, 3)
        .edge(1, 2)
        .edge(1, 7)
        .edge(4, 5)
        .edge(4, 6)
        .edge(4, 7)
        .edge(7, 2)
        .edge(7, 5)
        .edge(5, 2)
        .edge(5, 6)
        .edge(2, 3)
        .edge(2, 6)
        .edge(3, 6);
    System.out.println(g.topologicalOrder());
    // [0, 1, 4, 7, 5, 2, 3, 6]
  }
}
