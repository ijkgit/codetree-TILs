import java.io.*;
import java.util.StringTokenizer;

public class Main {
    private static int N, ans;
    private static int[][] map;
    private static final int[][] direction = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    private static final int[][] sandDirection = {{-2, 0}, {-1, -1}, {-1, 0}, {-1, 1}, {0, -2}, {0, -1}, {0, 1}, {0, 2}, {1, -1}, {1, 0}, {1, 1}, {2, 0}};
    private static final double[][] ratio = {{0.02, 0.1, 0.07, 0.01, 0.05, 0, 0, 0, 0.1, 0.07, 0.01, 0.02},
            {0, 0.01, 0, 0.01, 0.02, 0.07, 0.07, 0.02, 0.1, 0, 0.1, 0.05},
            {0.02, 0.01, 0.07, 0.1, 0, 0, 0, 0.05, 0.01, 0.07, 0.1, 0.02},
            {0.05, 0.1, 0, 0.1, 0.02, 0.07, 0.07, 0.02, 0.01, 0, 0.01, 0}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(br.readLine());

        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                ans += map[i][j];
            }
        }

        sb.append(sol());
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

    private static int sol() {
        int x = N / 2, y = N / 2;
        L:
        for (int K = 1; K <= N; K++) {
            for (int d = 0; d < direction.length; d++) {
                if (d == 2) K++;
                for (int k = 0; k < K; k++) {
                    x += direction[d][0];
                    y += direction[d][1];

                    if (map[x][y] != 0) {
                        int sum = 0;
                        for (int j = 0; j < sandDirection.length; j++) {
                            int nx = x + sandDirection[j][0];
                            int ny = y + sandDirection[j][1];

                            int sand = (int) ((double) map[x][y] * ratio[d][j]);
                            if (0 <= nx && nx < N && 0 <= ny && ny < N) map[nx][ny] += sand;
                            sum += sand;
                        }

                        int rx = x + direction[d][0];
                        int ry = y + direction[d][1];

                        if (0 <= rx && rx < N && 0 <= ry && ry < N) map[rx][ry] += map[x][y] - sum;
                        map[x][y] = 0;
                    }
                    if (x == 0 && y == 0) break L;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                ans -= map[i][j];
            }
        }

        return ans;
    }
}