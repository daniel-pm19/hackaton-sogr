package google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class BathroomStalls {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        int t = Integer.parseInt(br.readLine().trim());
        for (int caso = 1; caso <= t; caso++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long n = Long.parseLong(st.nextToken());
            long k = Long.parseLong(st.nextToken());

            TreeMap<Long, Long> huecos = new TreeMap<>();
            huecos.put(n, 1L);
            long mayor = 0, menor = 0;

            while (true) {
                Map.Entry<Long, Long> grupo = huecos.pollLastEntry();
                long tam = grupo.getKey();
                long cantidad = grupo.getValue();
                mayor = tam / 2;
                menor = (tam - 1) / 2;
                if (k <= cantidad) {
                    break;
                }
                k -= cantidad;
                huecos.merge(mayor, cantidad, Long::sum);
                huecos.merge(menor, cantidad, Long::sum);
            }

            out.append("Case #").append(caso).append(": ")
               .append(mayor).append(' ').append(menor).append('\n');
        }
        System.out.print(out);
    }
}
