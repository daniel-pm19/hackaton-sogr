package google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ChainReactions {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");
        StringBuilder out = new StringBuilder();

        st = refill(br, st);
        int t = Integer.parseInt(st.nextToken());
        for (int caso = 1; caso <= t; caso++) {
            st = refill(br, st);
            int n = Integer.parseInt(st.nextToken());
            long[] f = new long[n + 1];
            int[] p = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                st = refill(br, st);
                f[i] = Long.parseLong(st.nextToken());
            }
            for (int i = 1; i <= n; i++) {
                st = refill(br, st);
                p[i] = Integer.parseInt(st.nextToken());
            }

            long[] sumaHijos = new long[n + 1];
            long[] minHijo = new long[n + 1];
            Arrays.fill(minHijo, Long.MAX_VALUE);
            long total = 0;

            for (int i = n; i >= 1; i--) {
                long valor;
                if (minHijo[i] == Long.MAX_VALUE) {
                    valor = f[i];
                } else {
                    total += sumaHijos[i] - minHijo[i];
                    valor = Math.max(f[i], minHijo[i]);
                }
                if (p[i] == 0) {
                    total += valor;
                } else {
                    sumaHijos[p[i]] += valor;
                    minHijo[p[i]] = Math.min(minHijo[p[i]], valor);
                }
            }

            out.append("Case #").append(caso).append(": ").append(total).append('\n');
        }
        System.out.print(out);
    }

    private static StringTokenizer refill(BufferedReader br, StringTokenizer st) throws IOException {
        while (!st.hasMoreTokens()) {
            st = new StringTokenizer(br.readLine());
        }
        return st;
    }
}
