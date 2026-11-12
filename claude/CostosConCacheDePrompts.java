package claude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static claude.Json.num;
import static claude.Json.obj;

public class CostosConCacheDePrompts {

    private static final long ENTRADA = 300;
    private static final long ESCRITURA_CACHE = 375;
    private static final long LECTURA_CACHE = 30;
    private static final long SALIDA = 1500;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(System.out, false, StandardCharsets.UTF_8);

        long total = 0, sinCache = 0;
        int solicitudes = 0, aciertos = 0;

        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) {
                continue;
            }
            Map<String, Object> usage = obj(Json.parse(linea));
            long entrada = num(usage.get("input_tokens"));
            long escritura = num(usage.get("cache_creation_input_tokens"));
            long lectura = num(usage.get("cache_read_input_tokens"));
            long salida = num(usage.get("output_tokens"));

            long costo = entrada * ENTRADA + escritura * ESCRITURA_CACHE
                       + lectura * LECTURA_CACHE + salida * SALIDA;
            long costoSinCache = (entrada + escritura + lectura) * ENTRADA + salida * SALIDA;

            solicitudes++;
            if (lectura > 0) {
                aciertos++;
            }
            total += costo;
            sinCache += costoSinCache;
            out.println("solicitud " + solicitudes + ": " + dolares(costo));
        }

        out.println("total: " + dolares(total));
        out.println("sin_cache: " + dolares(sinCache));
        out.println("ahorro: " + dolares(sinCache - total));
        out.println("aciertos_de_cache: " + aciertos + "/" + solicitudes);
        out.flush();
    }

    private static String dolares(long centesimasDeMillonesima) {
        String signo = centesimasDeMillonesima < 0 ? "-" : "";
        long millonesimas = (Math.abs(centesimasDeMillonesima) + 50) / 100;
        return signo + "$" + (millonesimas / 1_000_000) + "." + String.format("%06d", millonesimas % 1_000_000);
    }
}
