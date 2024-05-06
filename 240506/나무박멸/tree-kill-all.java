// package edu.ssafy.im.CodeTree.treeKillAll;

import java.io.*;
import java.util.*;

public class Main {
    private static int N, M, K, C;
    private static int[][] map, copyMap, visited;
    private static List<Tree> trees, copyTrees;
    private static final int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static final int[][] diagonal = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
    private static int ans = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        copyMap = new int[N][N];
        visited = new int[N][N];
        trees = new ArrayList<>();
        for (int x = 0; x < N; x++) {
            st = new StringTokenizer(br.readLine());
            for (int y = 0; y < N; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
                if (map[x][y] > 0) trees.add(new Tree(x, y));
            }
        }

        sb.append(sol());
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    private static void updateVisited() {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (visited[x][y] > 0) visited[x][y]--;
                else if (map[x][y] == -2) map[x][y] = 0;
            }
        }
    }

    private static int sol() {
        for (int m = 0; m < M; m++) {
            updateVisited();
//            print("after updateVisited");
            grow();
//            print("after grow");
            breed();
//            print("after breed");
            kill();
//            print("after kill");
        }
        return ans;
    }

    private static void print(String msg) {
        System.out.println(msg);
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                System.out.print(map[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void kill() {
        spread(findMax());
    }

    private static void spread(Tree tree) {
//        System.out.println(tree.x + " " + tree.y);
        map[tree.x][tree.y] = -2;
        visited[tree.x][tree.y] = C;
        for (int[] d : diagonal) {
            for (int k = 1; k <= K; k++) {
                int nx = tree.x + d[0] * k;
                int ny = tree.y + d[1] * k;

                if (!checkRange(nx, ny)) break;
                int tmp = map[nx][ny];
                map[nx][ny] = -2;
                visited[nx][ny] = C;
                if (tmp == -1 || tmp == 0) break;
            }
        }
    }

    private static Tree findMax() {
        int rs = 0, rx = 0, ry = 0;
        for (Tree tree : trees) {
            int sum = map[tree.x][tree.y];
            for (int[] d : diagonal) {
                for (int k = 1; k <= K; k++) {
                    int nx = tree.x + d[0] * k;
                    int ny = tree.y + d[1] * k;

                    if (!checkRange(nx, ny) || map[nx][ny] <= 0) break;
                    sum += map[nx][ny];
                }
            }
            if (sum < rs || (sum == rs && tree.x > rx) || (sum == rs && tree.x == rx && tree.y > ry)) continue;
            rs = sum; rx = tree.x; ry = tree.y;
        }

        ans += rs;

        return new Tree(rx, ry);
    }

    private static void breed() {
        copy();

        boolean[][] visited = new boolean[N][N];
        for (Tree tree : trees) {
            if (map[tree.x][tree.y] == -2) continue;

            int count = 0;
            for (int[] d : direction) {
                int nx = tree.x + d[0];
                int ny = tree.y + d[1];

                if (!checkRange(nx, ny)) continue;
                if (map[nx][ny] == 0) count++;
            }

            if (count == 0) continue;
            int newTree = map[tree.x][tree.y] / count;

            for (int[] d : direction) {
                int nx = tree.x + d[0];
                int ny = tree.y + d[1];

                if (!checkRange(nx, ny)) continue;
                if (map[nx][ny] != 0) continue;

                copyMap[nx][ny] += newTree;
                if (!visited[nx][ny]) copyTrees.add(new Tree(nx, ny));
                visited[nx][ny] = true;
            }
        }

        update();
    }

    private static void update() {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                map[x][y] = copyMap[x][y];
            }
        }

        trees = new ArrayList<>();
        trees.addAll(copyTrees);
    }

    private static void copy() {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                copyMap[x][y] = map[x][y];
            }
        }

        copyTrees = new ArrayList<>();
        copyTrees.addAll(trees);
    }

    private static void grow() {
        for (Tree tree : trees) {
            if (map[tree.x][tree.y] == -2) continue;
            int count = 0;
            for (int[] d : direction) {
                int nx = tree.x + d[0];
                int ny = tree.y + d[1];

                if (!checkRange(nx, ny)) continue;
                if (map[nx][ny] > 0) count++;
            }
            map[tree.x][tree.y] += count;
        }
    }

    private static boolean checkRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }

    static class Tree {
        int x, y;

        public Tree(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}