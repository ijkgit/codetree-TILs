import java.io.*;
import java.util.StringTokenizer;

public class Main {
    private static int N, M, K;
    private static int[][] maze;
    private static Point[] runners;
    private static Point exit;
    private static final int[][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static int distance;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        maze = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                maze[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        runners = new Point[M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            runners[i] = new Point(x - 1, y - 1);
        }

        st = new StringTokenizer(br.readLine());
        exit = new Point(Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()) - 1);
        
        sol();

        sb.append(distance).append("\n").append(exit.x).append(" ").append(exit.y);
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    private static void print() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void sol() {
        distance = 0;
        int count = 0;
        for (int k = 0; k < K; k++) {
            print();
            System.out.println(count + " " + distance);
            // 참가자 이동
            for (Point runner : runners) {
                for (int[] d : direction) {
                    int nx = runner.x + d[0];
                    int ny = runner.y + d[1];

                    if (nx < 0 || ny < 0 || nx >= N || ny >= N) continue;
                    if (maze[nx][ny] != 0) continue;
                    if (new Point(nx, ny).getDistance() >= runner.getDistance()) continue;

                    distance++;

                    if (nx == exit.x && ny == exit.y) {
                        nx = -1; ny = -1;
                        count++;

                        if (count == runners.length) return;
                    }

                    runner.x = nx;
                    runner.y = ny;

                    break;
                }
            }

            // 미로 회전
            L: for (int i = 1; i <= N; i++) {
                int[][] dir = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
                for (int[] d : dir) {
                    int nx = exit.x + d[0] * i;
                    int ny = exit.y + d[1] * i;
                    System.out.println("nx : " + nx + " ny : " + ny);
                    for (int x = exit.x; x != nx + d[0]; x += d[0]) {
                        for (int y = exit.y; y != ny + d[1]; y += d[1]) {
                            System.out.println(x + " " + y);
                            for (Point runner : runners) {
                                if (x == runner.x && y == runner.y) {
                                    System.out.println(nx + " " + ny + " " + d[0] + " " + d[1]);
                                    rotate(nx, ny, d[0], d[1]);
                                    break L;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void rotate(int nx, int ny, int dx, int dy) {
        /*
        1 2 3   7 4 1
        4 5 6   8 5 2
        7 8 9   9 6 3

        1 2     3 1
        3 4     4 2
         */

        int kx = exit.x, ky = exit.y;

        int[][] copy = new int[N][N];
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                copy[x][y] = maze[x][y];
                if (copy[x][y] >= 1) copy[x][y]--;
            }
        }

        Point[] runnersCopy = new Point[M];
        for (int i = 0; i < M; i++) {
            runnersCopy[i] = new Point(runners[i].x, runners[i].y);
        }

        for (int x = exit.x; x != nx + dx; x += dx) {
            for (int y = exit.y; y != ny + dy; y += dy) {
                System.out.println("x = " + x + " y = " + y + " x + dx * x = " + x + dx * x + " y + dy * y = " + y + dy * y);
                maze[x + dx * x][y + dy * y] = copy[x][y];
                if (x == exit.x && y == exit.y) ky += dy * y;
                for (int i = 0; i < M; i++) {
                    if (x == runnersCopy[i].x && y == runnersCopy[i].y) {
                        runners[i].y += dy * y;
                    }
                }
            }
            if (x == exit.x) kx += dx * x;
            for (int i = 0; i < M; i++) {
                if (x == runnersCopy[i].x) {
                    runners[i].x += dx * x;
                }
            }
        }

        exit.x = kx;
        exit.y = ky;
    }

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getDistance() {
            return Math.abs(this.x - exit.x) + Math.abs(this.y - exit.y);
        }
    }
}