package claude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static claude.Json.lista;
import static claude.Json.num;
import static claude.Json.obj;

public class UsoDeHerramientas {

    private static final Map<String, Integer> TEMPERATURAS = Map.of(
            "Bogotá", 14,
            "Medellín", 24,
            "Cali", 26);

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(System.out, false, StandardCharsets.UTF_8);

        int llamadas = 0, errores = 0;
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) {
                continue;
            }
            Map<String, Object> respuesta = obj(Json.parse(linea));
            String stopReason = (String) respuesta.get("stop_reason");

            if ("tool_use".equals(stopReason)) {
                for (Object b : lista(respuesta.get("content"))) {
                    Map<String, Object> bloque = obj(b);
                    if (!"tool_use".equals(bloque.get("type"))) {
                        continue;
                    }
                    String resultado = ejecutar((String) bloque.get("name"), obj(bloque.get("input")));
                    llamadas++;
                    if (resultado.startsWith("error:")) {
                        errores++;
                    }
                    out.println("tool_result " + bloque.get("id") + " " + resultado);
                }
            } else if ("end_turn".equals(stopReason)) {
                StringBuilder texto = new StringBuilder();
                for (Object b : lista(respuesta.get("content"))) {
                    Map<String, Object> bloque = obj(b);
                    if ("text".equals(bloque.get("type"))) {
                        texto.append((String) bloque.get("text"));
                    }
                }
                out.println("final: " + texto);
            }
            out.flush();
        }
        out.println("resumen: " + llamadas + " llamadas, " + errores + " errores");
        out.flush();
    }

    private static String ejecutar(String nombre, Map<String, Object> input) {
        switch (nombre) {
            case "calculadora":
                return calculadora((String) input.get("operacion"), num(input.get("a")), num(input.get("b")));
            case "clima":
                return clima((String) input.get("ciudad"));
            default:
                return "error: herramienta desconocida: " + nombre;
        }
    }

    private static String calculadora(String operacion, long a, long b) {
        switch (operacion) {
            case "sumar":
                return String.valueOf(a + b);
            case "restar":
                return String.valueOf(a - b);
            case "multiplicar":
                return String.valueOf(a * b);
            case "dividir":
                if (b == 0) {
                    return "error: división por cero";
                }
                return String.valueOf(Math.floorDiv(a, b));
            default:
                return "error: operación desconocida: " + operacion;
        }
    }

    private static String clima(String ciudad) {
        Integer temperatura = TEMPERATURAS.get(ciudad);
        if (temperatura == null) {
            return "error: ciudad desconocida: " + ciudad;
        }
        return temperatura + " °C";
    }
}
