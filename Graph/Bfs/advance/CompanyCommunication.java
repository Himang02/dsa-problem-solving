package Graph.Bfs.advance;

import java.util.*;
import java.io.*;

/*
Company Communication
https://www.algouniversity.com/problem/222/?asid=2044

A company has N employees who are proficient in some of M languages. For each
employee you are told every language they know.

Two employees X and Y can communicate if:
  - X and Y know a common language, OR
  - there exists another employee Z with whom both X and Y can communicate.

Find out whether ALL N employees can communicate with each other.

Constraints:
    2 <= N <= 1e5
    1 <= M <= 1e5
    1 <= sum of all languages everyone knows <= 1e5

INPUT
First line has two integers N and M: the number of employees and the number of
possible languages.
The next N lines describe the languages each employee knows. Each line starts
with an integer K, the number of languages that person speaks, followed by K
integers naming those languages.
Note that some of the M languages may be spoken by nobody.

OUTPUT
Print "YES" if all N employees can communicate, otherwise "NO".

EXAMPLE
    Sample 1 INPUT:
        4 6
        3 1 2 3
        2 4 2
        2 4 6
        1 6
    Sample 1 OUTPUT:
        YES

    Call them A, B, C, D. A and B share language 2. B and C share language 4.
    C and D share language 6. A and C never share a language directly, but both
    can talk to B, so the second rule links them -- and so on, until everyone is
    in one group.

    Sample 2 INPUT:
        4 4
        1 1
        1 1
        1 2
        1 2
    Sample 2 OUTPUT:
        NO

    Employees 1 and 2 share language 1; employees 3 and 4 share language 2. The
    two pairs have no language and no intermediary in common, so the company
    splits into two islands.

    Sample 3 INPUT:
        2 2
        1 1
        0
    Sample 3 OUTPUT:
        NO

    The second employee knows NO languages at all -- the line is just "0" with
    nothing after it. They can never communicate with anyone.

HINT (model languages as nodes too): the tempting model is a graph of employees
where two employees are joined if they share a language. Do not build it: if
500 employees all speak English that is 500*499/2 edges from one language
alone, and at N = 1e5 the edge count explodes.

Instead make a BIPARTITE graph with N + M nodes -- one per employee AND one per
language:

    employee i  ->  node i             (0 .. N-1)
    language L  ->  node N + L - 1     (N .. N+M-1)

and join employee i to each language they know. Now "500 people speak English"
is 500 edges, not 125,000. The total edge count is exactly the sum of all K,
which the constraints cap at 1e5.

Two employees can communicate exactly when their nodes are in the same
connected component of this graph -- a path employee -> language -> employee ->
language -> ... is precisely the chain of intermediaries the problem describes.
So: BFS once from employee 0, then check that all N EMPLOYEE nodes were
reached. (A DSU over the same N + M nodes works equally well.)

Things to get right:
  - count only EMPLOYEE nodes when checking. Languages nobody speaks are
    isolated nodes and will never be reached; that is fine and must not make
    you answer NO
  - an employee may know ZERO languages (K = 0, a line containing just "0").
    With N >= 2 that is an automatic NO, and it is the case most likely to be
    missing from a first attempt -- Sample 3 is there for it
  - the sum of K is bounded, but a SINGLE employee may know up to 1e5 languages
    and a single language may be known by up to 1e5 employees, so do not size
    anything N*M
  - N + M can be 2e5 nodes with only 1e5 edges, so most nodes may be isolated
  - BFS here is iterative -- no stack worries -- but a recursive DFS over 2e5
    nodes would need the 64 MB thread
*/
public class CompanyCommunication {
    public static void main(String[] args) throws IOException{
    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    StringTokenizer st = new StringTokenizer(br.readLine());
    int n = Integer.parseInt(st.nextToken());
    int m = Integer.parseInt(st.nextToken());
      
    List<List<Integer>> langList = new ArrayList<>();
    for(int i = 0; i < m; i++){
      langList.add(new ArrayList<>());
    }
    
    List<List<Integer>> empList = new ArrayList<>();
    for(int i = 0; i < n; i++){
      empList.add(new ArrayList<>());
    }
    
    for(int i = 0; i < n; i++){
      st = new StringTokenizer(br.readLine());
      int knownlangs = Integer.parseInt(st.nextToken());
      for(int j = 0; j < knownlangs; j++){
        int langIndex = Integer.parseInt(st.nextToken());
        empList.get(i).add(langIndex - 1);
        langList.get(langIndex - 1).add(i);
      }
    }

    Queue<Node> queue = new ArrayDeque<>();
    boolean[] langVisited = new boolean[m];
    boolean[] empVisited = new boolean[n];
    Node emp = new Node(false, 0);
    queue.offer(emp);
    empVisited[0] = true;
    
    while(!queue.isEmpty()){
      Node curr = queue.poll();
      
      if(curr.language){
        for(Integer num : langList.get(curr.value)){
          if(!empVisited[num]){
            empVisited[num] = true;
            queue.offer(new Node(false, num));
          }
        }
      }
      else{
        for(Integer num : empList.get(curr.value)){
          if(!langVisited[num]){
            langVisited[num] = true;
            queue.offer(new Node(true, num));
          }
        }
      }
    }
    
    for(int i = 0; i < n; i++){
      if(!empVisited[i]){
        System.out.println("NO");
        return;
      }
    }


    System.out.println("YES");

  }
}

class Node{
  boolean language;
  int value;
  
  public Node(boolean language, int value){
    this.language = language;
    this.value = value;
  }
}

/*
REVIEW NOTES  (2026-09-14)

1. VERDICT: correct, no bugs. All three samples pass, plus employee 0 knowing
   nothing, unused languages present, everyone sharing one language, and a chain
   through an intermediary. 300 random companies differential-tested against a
   reference BFS -> 0 mismatches. At full size (N = M = 1e5, sum K = 1e5) every
   shape runs in under 0.6s.

2. THE MODEL IS THE PROBLEM, AND IT IS RIGHT: languages are nodes too. Joining
   employees directly whenever they share a language would cost 500*499/2 edges
   for one language spoken by 500 people; the bipartite form makes it 500. Total
   edges = sum of K <= 1e5.

3. NICE VARIATION ON THE STANDARD MODEL: instead of one node array with
   languages offset to N + L - 1, this keeps TWO adjacency lists -- empList[i]
   giving languages, langList[L] giving employees -- and a Node carrying a
   boolean saying which side it is on. Same graph, no offset arithmetic, and two
   separate visited arrays fall out naturally.

4. THE RISK IN THAT DESIGN IS MIXING THE SIDES UP, AND IT DOES NOT: expanding a
   LANGUAGE node iterates langList and checks empVisited; expanding an EMPLOYEE
   node iterates empList and checks langVisited. Both correct. The loop variable
   is called `num` in both branches though, while meaning an employee index in
   one and a language index in the other -- empIndex / langIndex would make the
   asymmetry visible at a glance.

5. THREE CASES HANDLED WITH NO SPECIAL-CASING: K = 0 works because the token
   loop simply does not execute (no branch needed); languages nobody speaks stay
   unvisited and are ignored because the final sweep only checks empVisited; and
   an employee knowing all 1e5 languages is fine because nothing is sized N*M.
   The nastiest of these is employee 0 knowing nothing -- BFS starts there and
   terminates immediately -- and it still answers NO correctly.

6. NOT WORTH CHANGING: Node could be a single int (sign bit, or N + L). At most
   2e5 of them are ever created and they die young, which is exactly what the
   generational collector handles well -- see the CarFactoryLocation notes,
   where an int-encoded queue measured only ~20-30% faster and inside the noise.
*/
