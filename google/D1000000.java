package google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;


public class D1000000 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");
        StringBuilder out = new StringBuilder();

        st = refill(br, st);
        int t = Integer.parseInt(st.nextToken());
        for (int caso = 1; caso <= t; caso++) {
            st = refill(br, st);
            int n = Integer.parseInt(st.nextToken());
            int[] s = new int[n];
            for (int i = 0; i < n; i++) {
                st = refill(br, st);
                s[i] = Integer.parseInt(st.nextToken());
            }
            Arrays.sort(s);

            int largo = 0;
            for (int caras : s) {
                if (caras > largo) {
                    largo++;
                }
            }
            out.append("Case #").append(caso).append(": ").append(largo).append('\n');
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
