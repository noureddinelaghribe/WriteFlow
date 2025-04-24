package com.noureddine.WriteFlow.Utils;

public class RemoveOutsideBraces {
    public static String removeOutsideBraces(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideBraces = false;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '{') {
                insideBraces = true;
                result.append(c);
            } else if (c == '}') {
                insideBraces = false;
                result.append(c);
            } else if (insideBraces) {
                result.append(c);
            }
        }

        return result.toString();
    }

}
