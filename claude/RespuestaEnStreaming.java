package claude;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import static claude.Json.num;
import static claude.Json.obj;

public class RespuestaEnStreaming {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream out = new PrintStream(System.out, false, StandardCharsets.UTF_8);

        TreeMap<Long, StringBuilder> bloques = new TreeMap<>();
        String stopReason = null;
        long inputTokens = 0, outputTokens = 0;

        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (!linea.startsWith("data:")) {
                continue;
            }
            Map<String, Object> evento = obj(Json.parse(linea.substring(5)));
            String tipo = (String) evento.get("type");
            if (tipo == null) {
                continue;
            }
            switch (tipo) {
                case "message_start": {
                    bloques.clear();
                    stopReason = null;
                    outputTokens = 0;
                    Map<String, Object> usage = obj(obj(evento.get("message")).get("usage"));
                    inputTokens = num(usage.get("input_tokens"));
                    break;
                }
                case "content_block_delta": {
                    Map<String, Object> delta = obj(evento.get("delta"));
                    if ("text_delta".equals(delta.get("type"))) {
                        long index = num(evento.get("index"));
                        bloques.computeIfAbsent(index, k -> new StringBuilder())
                               .append((String) delta.get("text"));
                    }
                    break;
                }
                case "message_delta": {
                    Object razon = obj(evento.get("delta")).get("stop_reason");
                    if (razon != null) {
                        stopReason = (String) razon;
                    }
                    Map<String, Object> usage = obj(evento.get("usage"));
                    if (usage.containsKey("output_tokens")) {
                        outputTokens = num(usage.get("output_tokens"));
                    }
                    break;
                }
                case "message_stop": {
                    StringBuilder texto = new StringBuilder();
                    for (StringBuilder bloque : bloques.values()) {
                        texto.append(bloque);
                    }
                    out.println("texto: " + texto);
                    out.println("stop_reason: " + stopReason);
                    out.println("input_tokens: " + inputTokens);
                    out.println("output_tokens: " + outputTokens);
                    out.flush();
                    break;
                }
                default:
                    break;
            }
        }
        out.flush();
    }
}
