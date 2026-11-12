package google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ParentingPartneringReturns {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");
        StringBuilder out = new StringBuilder();

        st = refill(br, st);
        int t = Integer.parseInt(st.nextToken());
        for (int caso = 1; caso <= t; caso++) {
            st = refill(br, st);
            int n = Integer.parseInt(st.nextToken());
            int[] inicio = new int[n];
            int[] fin = new int[n];
            Integer[] orden = new Integer[n];
            for (int i = 0; i < n; i++) {
                st = refill(br, st);
                inicio[i] = Integer.parseInt(st.nextToken());
                st = refill(br, st);
                fin[i] = Integer.parseInt(st.nextToken());
                orden[i] = i;
            }
            Arrays.sort(orden, (a, b) -> Integer.compare(inicio[a], inicio[b]));

            char[] asignacion = new char[n];
            int libreC = 0, libreJ = 0;
            boolean posible = true;
            for (int idx : orden) {
                if (libreC <= inicio[idx]) {
                    asignacion[idx] = 'C';
                    libreC = fin[idx];
                } else if (libreJ <= inicio[idx]) {
                    asignacion[idx] = 'J';
                    libreJ = fin[idx];
                } else {
                    posible = false;
                    break;
                }
            }

            out.append("Case #").append(caso).append(": ")
               .append(posible ? new String(asignacion) : "IMPOSSIBLE").append('\n');
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
