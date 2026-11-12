package claude;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Json {
    private final String s;
    private int pos;

    public Json(String s) {
        this.s = s;
    }

    public static Object parse(String texto) {
        return new Json(texto.trim()).valor();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> obj(Object o) {
        return o instanceof Map ? (Map<String, Object>) o : new LinkedHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static List<Object> lista(Object o) {
        return o instanceof List ? (List<Object>) o : new ArrayList<>();
    }

    public static long num(Object o) {
        return o instanceof Number ? ((Number) o).longValue() : 0;
    }

    public Object valor() {
        espacios();
        char c = s.charAt(pos);
        if (c == '{') return objeto();
        if (c == '[') return arreglo();
        if (c == '"') return cadena();
        if (s.startsWith("true", pos)) { pos += 4; return Boolean.TRUE; }
        if (s.startsWith("false", pos)) { pos += 5; return Boolean.FALSE; }
        if (s.startsWith("null", pos)) { pos += 4; return null; }
        return numero();
    }

    private Map<String, Object> objeto() {
        Map<String, Object> m = new LinkedHashMap<>();
        pos++;
        espacios();
        if (s.charAt(pos) == '}') { pos++; return m; }
        while (true) {
            espacios();
            String clave = cadena();
            espacios();
            pos++;
            m.put(clave, valor());
            espacios();
            if (s.charAt(pos++) == '}') return m;
        }
    }

    private List<Object> arreglo() {
        List<Object> l = new ArrayList<>();
        pos++;
        espacios();
        if (s.charAt(pos) == ']') { pos++; return l; }
        while (true) {
            l.add(valor());
            espacios();
            if (s.charAt(pos++) == ']') return l;
        }
    }

    private String cadena() {
        StringBuilder sb = new StringBuilder();
        pos++;
        while (true) {
            char c = s.charAt(pos++);
            if (c == '"') return sb.toString();
            if (c != '\\') { sb.append(c); continue; }
            char e = s.charAt(pos++);
            switch (e) {
                case 'n': sb.append('\n'); break;
                case 't': sb.append('\t'); break;
                case 'r': sb.append('\r'); break;
                case 'b': sb.append('\b'); break;
                case 'f': sb.append('\f'); break;
                case 'u':
                    sb.append((char) Integer.parseInt(s.substring(pos, pos + 4), 16));
                    pos += 4;
                    break;
                default: sb.append(e);
            }
        }
    }

    private Number numero() {
        int inicio = pos;
        while (pos < s.length() && "+-0123456789.eE".indexOf(s.charAt(pos)) >= 0) pos++;
        String n = s.substring(inicio, pos);
        if (n.contains(".") || n.contains("e") || n.contains("E")) return Double.parseDouble(n);
        return Long.parseLong(n);
    }

    private void espacios() {
        while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++;
    }
}
