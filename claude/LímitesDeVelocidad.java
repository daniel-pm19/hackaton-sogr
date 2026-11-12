package claude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class LímitesDeVelocidad {

    private static final long CAPACIDAD_SOLICITUDES = 3;
    private static final long RECARGA_SOLICITUDES = 1;
    private static final long CAPACIDAD_TOKENS = 100;
    private static final long RECARGA_TOKENS = 20;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(System.out, false, StandardCharsets.UTF_8);

        long solicitudes = CAPACIDAD_SOLICITUDES;
        long tokens = CAPACIDAD_TOKENS;
        long ultimoTiempo = 0;

        String linea;
        while ((linea = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(linea);
            if (!st.hasMoreTokens() || !st.nextToken().equals("REQUEST")) {
                continue;
            }
            long t = Long.parseLong(st.nextToken());
            long nuevos = Long.parseLong(st.nextToken());

            long transcurrido = Math.max(0, t - ultimoTiempo);
            solicitudes = Math.min(CAPACIDAD_SOLICITUDES, solicitudes + transcurrido * RECARGA_SOLICITUDES);
            tokens = Math.min(CAPACIDAD_TOKENS, tokens + transcurrido * RECARGA_TOKENS);
            ultimoTiempo = Math.max(ultimoTiempo, t);

            if (solicitudes >= 1 && tokens >= nuevos) {
                solicitudes -= 1;
                tokens -= nuevos;
                out.println("ok");
            } else {
                long esperaSolicitudes = techo(Math.max(0, 1 - solicitudes), RECARGA_SOLICITUDES);
                long esperaTokens = techo(Math.max(0, nuevos - tokens), RECARGA_TOKENS);
                out.println("429 retry-after=" + Math.max(esperaSolicitudes, esperaTokens));
            }
            out.flush();
        }
    }

    private static long techo(long a, long b) {
        return (a + b - 1) / b;
    }
}
