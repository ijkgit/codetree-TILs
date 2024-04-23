import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    private static int N;
    private static int[][] map, group;
    private static int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private static boolean[][] visited, groupGraph;
    private static ArrayDeque<Point> queue;
    private static int groupSize;
    private static int[] groupCount, groupValue;
    private static int ans = 0;
    private static int[][] copy;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());

        map = new int[N][N];
        for (int x = 0; x < N; x++) {
            st = new StringTokenizer(br.readLine());
            for (int y = 0; y < N; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        sol();

        sb.append(ans);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    private static void sol() {
        for (int i = 0; i < 4; i++) {
            getScore();
            map = rotate();
        }
    }

    private static int[][] rotate() {
        int[][] copy = new int[N][N];

        for (int x = 0; x < N; x++) {
            copy[N / 2][x] = map[x][N / 2];
            copy[N - 1 - x][N / 2] = map[N / 2][x];
        }

        for (int x = 0; x < N / 2; x++) {
            for (int y = 0; y < N / 2; y++) {
                copy[y][N / 2 - 1 - x] = map[x][y];
            }
        }

        for (int x = 0; x < N / 2; x++) {
            for (int y = N / 2 + 1; y < N; y++) {
                copy[y - (N / 2 + 1)][N - 1 - x] = map[x][y];
            }
        }

        for (int x = N / 2 + 1; x < N; x++) {
            for (int y = 0; y < N / 2; y++) {
                copy[y + N / 2 + 1][N - 1 - x] = map[x][y];
            }
        }

        for (int x = N / 2 + 1; x < N; x++) {
            for (int y = N / 2 + 1; y < N; y++) {
                copy[y][N - 1 - (x - (N / 2 + 1))] = map[x][y];
            }
        }

        return copy;
    }

    private static void getScore() {
        groupSize = makeGroup();
        getPairScore();
    }

    private static void getPairScore() {
        visited = new boolean[N][N];
        queue = new ArrayDeque<>();
        groupGraph = new boolean[groupSize][groupSize];

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (!visited[x][y]) findPair(x, y);
            }
        }
    }

    private static void findPair(int x, int y) {
        visited[x][y] = true;
        queue.offer(new Point(x, y));

        int[] pairScore = new int[groupSize];
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            for (int[] d: direction) {
                int nx = p.x + d[0];
                int ny = p.y + d[1];

                if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                if (visited[nx][ny]) continue;

                if (group[nx][ny] != group[x][y]) pairScore[group[nx][ny]]++;
                else {
                    visited[nx][ny] = true;
                    queue.offer(new Point(nx, ny));
                }
            }
        }

        for (int i = 0; i < groupSize; i++) {
            int ps = pairScore[i];

            if (ps == 0) continue;
            if (groupGraph[group[x][y]][i]) continue;

            groupGraph[group[x][y]][i] = true;
            groupGraph[i][group[x][y]] = true;

            ans += (groupCount[group[x][y]] + groupCount[i]) * groupValue[group[x][y]] * groupValue[i] * pairScore[i];
        }
    }

    private static int makeGroup() {
        visited = new boolean[N][N];
        queue = new ArrayDeque<>();

        int n = 0;
        groupCount = new int[N * N];
        groupValue = new int[N * N];
        group = new int[N][N];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (!visited[x][y]) findGroup(x, y, n++);
            }
        }

        return n;
    }

    private static void findGroup(int x, int y, int n) {
        ArrayList<Point> arrayList = new ArrayList<>();
        arrayList.add(new Point(x, y));
        queue.offer(new Point(x, y));
        visited[x][y] = true;
        group[x][y] = n;

        int count = 1;
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            for (int[] d: direction) {
                int nx = p.x + d[0];
                int ny = p.y + d[1];

                if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                if (visited[nx][ny] || map[nx][ny] != map[x][y]) continue;

                group[nx][ny] = n;
                visited[nx][ny] = true;
                queue.offer(new Point(nx, ny));
                count++;
                arrayList.add(new Point(nx, ny));
            }
        }

        groupCount[n] = count;
        groupValue[n] = map[x][y];
    }

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}