package claude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Map;
import java.util.TreeMap;

import static claude.Json.obj;

public class EvaluacionDeRespuestas {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(System.out, false, StandardCharsets.UTF_8);

        TreeMap<String, int[]> categorias = new TreeMap<>();
        int aprobadas = 0, total = 0;

        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) {
                continue;
            }
            Map<String, Object> caso = obj(Json.parse(linea));
            String categoria = String.valueOf(caso.get("categoria"));
            String esperado = normalizar(String.valueOf(caso.get("esperado")));
            String respuesta = normalizar(String.valueOf(caso.get("respuesta")));

            boolean pasa = (" " + respuesta + " ").contains(" " + esperado + " ");

            int[] conteo = categorias.computeIfAbsent(categoria, k -> new int[2]);
            conteo[1]++;
            total++;
            if (pasa) {
                conteo[0]++;
                aprobadas++;
            }
        }

        for (Map.Entry<String, int[]> e : categorias.entrySet()) {
            out.println(e.getKey() + ": " + e.getValue()[0] + "/" + e.getValue()[1]);
        }
        out.println("total: " + aprobadas + "/" + total + " (" + porcentaje(aprobadas, total) + "%)");
        out.flush();
    }

    private static String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFD)
                                     .replaceAll("\\p{M}", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sinTildes.length(); i++) {
            char c = sinTildes.charAt(i);
            sb.append(Character.isLetterOrDigit(c) ? c : ' ');
        }
        return sb.toString().trim().replaceAll(" +", " ");
    }

    private static String porcentaje(int a, int n) {
        if (n == 0) {
            return "0.0";
        }
        long decimas = (2000L * a + n) / (2L * n);
        return (decimas / 10) + "." + (decimas % 10);
    }
}
