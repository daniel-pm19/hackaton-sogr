package google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MoonsAndUmbrellas {

    static final long INF = Long.MAX_VALUE / 4;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        int t = Integer.parseInt(br.readLine().trim());
        for (int caso = 1; caso <= t; caso++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long x = Long.parseLong(st.nextToken());
            long y = Long.parseLong(st.nextToken());
            String s = st.hasMoreTokens() ? st.nextToken() : "";

            long costoC = 0, costoJ = 0;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);
                long nuevoC = INF, nuevoJ = INF;
                if (i == 0) {
                    if (ch != 'J') nuevoC = 0;
                    if (ch != 'C') nuevoJ = 0;
                } else {
                    if (ch != 'J') nuevoC = Math.min(costoC, costoJ + y);
                    if (ch != 'C') nuevoJ = Math.min(costoJ, costoC + x);
                }
                costoC = nuevoC;
                costoJ = nuevoJ;
            }
            out.append("Case #").append(caso).append(": ").append(Math.min(costoC, costoJ)).append('\n');
        }
        System.out.print(out);
    }
}
