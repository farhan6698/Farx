package com.farx.studio;

import java.util.*;
import java.util.regex.*;

public final class FarxEngine {
    private FarxEngine() {}

    public static final class Result {
        public final String output;
        public final String summary;
        Result(String output, String summary) { this.output = output; this.summary = summary; }
    }

    public static final class FarxException extends RuntimeException {
        public final int line;
        FarxException(int line, String message) {
            super((line > 0 ? "Line " + line + ": " : "") + message);
            this.line = line;
        }
    }

    private static final Set<String> IMPLEMENTED_MODES =
        new HashSet<>(Arrays.asList("CODE", "UI", "DATA", "SYSTEM", "BRAIN", "AUDIO", "2D", "3D"));

    public static Result run(String source) {
        if (source == null) throw new FarxException(0, "Source cannot be null.");
        String[] lines = source.replace("\r", "").split("\n", -1);
        boolean inMode = false;
        int blocks = 0, commands = 0;
        Map<String, Double> vars = new HashMap<>();
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String raw = lines[i].trim();
            int line = i + 1;
            if (raw.isEmpty() || raw.startsWith("#") || raw.startsWith("//")) continue;
            String upper = raw.toUpperCase(Locale.ROOT);

            if (upper.startsWith("MODE ")) {
                if (inMode) throw new FarxException(line, "Nested MODE blocks are not supported yet.");
                String mode = raw.substring(5).trim().toUpperCase(Locale.ROOT);
                if (!IMPLEMENTED_MODES.contains(mode))
                    throw new FarxException(line, "Mode '" + mode + "' is registered/planned but not executable in this preview.");
                inMode = true; blocks++; commands++;
                out.append("[MODE ").append(mode).append("]\n");
                continue;
            }
            if (upper.equals("END MODE")) {
                if (!inMode) throw new FarxException(line, "END MODE without an open MODE block.");
                inMode = false; commands++;
                out.append("[END MODE]\n");
                continue;
            }
            if (!inMode) throw new FarxException(line, "Command must be inside MODE CODE ... END MODE.");
            if (upper.startsWith("PRINT ")) {
                String value = raw.substring(6).trim();
                out.append(unquote(value, line)).append('\n'); commands++;
            } else if (upper.startsWith("LET ")) {
                Matcher m = Pattern.compile("(?i)^LET\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*(-?\\d+(?:\\.\\d+)?)$").matcher(raw);
                if (!m.matches()) throw new FarxException(line, "Expected: LET name = number");
                vars.put(m.group(1).toLowerCase(Locale.ROOT), Double.parseDouble(m.group(2)));
                commands++;
            } else if (upper.startsWith("ADD ") || upper.startsWith("SUB ") ||
                       upper.startsWith("MUL ") || upper.startsWith("DIV ")) {
                String[] p = raw.split("\\s+");
                if (p.length != 3) throw new FarxException(line, "Expected: ADD|SUB|MUL|DIV variable variable");
                double a = get(vars, p[1], line), b = get(vars, p[2], line), value;
                switch (p[0].toUpperCase(Locale.ROOT)) {
                    case "ADD": value = a + b; break;
                    case "SUB": value = a - b; break;
                    case "MUL": value = a * b; break;
                    default:
                        if (b == 0) throw new FarxException(line, "Division by zero.");
                        value = a / b;
                }
                out.append(format(value)).append('\n'); commands++;
            } else if (upper.startsWith("LINK ")) {
                String target = raw.substring(5).trim();
                if (target.isEmpty()) throw new FarxException(line, "LINK requires a module identifier.");
                out.append("[LINK CHECK] ").append(target)
                   .append(" — declaration parsed; module loading is not implemented yet.\n");
                commands++;
            } else {
                throw new FarxException(line, "Unknown command: " + raw);
            }
        }
        if (inMode) throw new FarxException(lines.length, "Missing END MODE.");
        return new Result(out.toString(), "SUCCESS • " + blocks + " block(s) • " + commands + " instruction(s)");
    }

    private static double get(Map<String, Double> vars, String name, int line) {
        Double v = vars.get(name.toLowerCase(Locale.ROOT));
        if (v == null) throw new FarxException(line, "Unknown variable: " + name);
        return v;
    }

    private static String unquote(String s, int line) {
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\""))
            return s.substring(1, s.length() - 1).replace("\\n", "\n").replace("\\\"", "\"");
        if (s.matches("-?\\d+(?:\\.\\d+)?")) return s;
        if (s.matches("[A-Za-z_][A-Za-z0-9_]*")) return s;
        throw new FarxException(line, "PRINT accepts a quoted string, number, or variable name.");
    }

    private static String format(double d) {
        if (d == Math.rint(d)) return Long.toString((long)d);
        return Double.toString(d);
    }
}
