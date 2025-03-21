package com.noureddine.WriteFlow.Utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class TextProcessing {

    // "Insert New Line After Symbol"
    public static String insertNewLineAfterSymbol(String text, String symbol) {
        String regex = "(?i)" + Pattern.quote(symbol);
        return text.replaceAll(regex, "$0\n");
    }

    // "Replace Newlines With Symbol"
    public static String replaceSymbolNewLine(String text, String symbol) {
        return text.replaceAll("\\s*\\n\\s*", " " + symbol.trim() + " ");
    }

    // "Repeat Text"
    public static String repeatText(String text, int num) {
        if (num <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(text.length() * num);
        for (int i = 0; i < num; i++) {
            sb.append(text);
        }
        return sb.toString();
    }

    // "Reverse Text"
    public static String reverserText(String text) {
        int lengthText = text.length()-1;
        StringBuilder sb = new StringBuilder();
        for ( int i = lengthText ; i >= 0 ; i-- ) {
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }

    // "Truncate Text"
    public static String truncatorText(String text, int num) {
        if (num <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(num);
        for (int i = 0; i < num; i++) {
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }

    // "Filter Text By Word"
    public static String filterTextByWord(String text, String word) {
        StringBuilder sb = new StringBuilder();
        String[] paragraphs = text.split("\\n\\s*\\n");
        for (String paragraph : paragraphs) {
            if (paragraph.toLowerCase().contains(word.toLowerCase())) {
                // Add each matching paragraph with a newline separator
                sb.append(paragraph.trim()).append("\n\n");
            }
        }
        return sb.toString().trim();
    }

    // "Sort Text By Alphabet"
    public static String sorterTextByAlphabet(String text) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(text.split("\\n\\s*\\n")));
        Collections.sort(list);
        for (String paragraph : list) {
            sb.append(paragraph.trim()).append("\n\n");
        }
        return sb.toString().trim();
    }


    // "Sort Text By Length"
    public static String sorterTextByLength(String text) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(text.split("\\n\\s*\\n")));
        list.sort(Comparator.comparingInt(String::length));
        for (String paragraph : list) {
            sb.append(paragraph.trim()).append("\n\n");
        }
        return sb.toString().trim();
    }


    // "Remove Duplicate Lines"
    public static String removerLineDuplicateText(String text) {
        return text.replaceAll("(\\r?\\n){2,}","\\n");
    }


    // "Remove Duplicate Spaces"
    public static String removerSpaceDuplicateText(String text) {
        return text.replaceAll(" {2,}"," ");
    }


    // "Wrap Text By Words"
    public static String wrapTextByWords(String text, int charsPerLine) {
        if (charsPerLine <= 0) {
            return text;
        }
        return text.replaceAll("(.{" + charsPerLine + "})", "$1\n").trim();
    }


    // "Wrap Text By Characters"
    public static String wrapTextByCharacters(String text, int charsPerLine) {
        String regex = String.format("(.{%d})", charsPerLine);
        return text.replaceAll(regex, "$1\n").trim();
    }


    // "Unwrap Text"
    public static String unWrapText(String text) {
        return text.replaceAll("\n","").trim();
    }

    // "Fix Text Spacing"
    public static String distanceFixerText(String text) {
        String normalizedNewlines = text.replaceAll("(\\r?\\n){2,}", "\n").trim();
        String normalizedSpaces = normalizedNewlines.replaceAll("[ \\t]{2,}", " ").trim();
        return normalizedSpaces;
    }


    // "Find and Replace Text"
    public static String findandReplaceText(String text,String find,String replace){
        return text.replaceAll(find,replace).trim();
    }


    // "Remove Punctuation"
    public static String removePunctuation(String text) {
        char[] punctuationChars = new char[]{
                '.', ',', ';', ':', '!', '?', '"', '\'', '`', '-', '–', '—',
                '_', '(', ')', '[', ']', '{', '}', '/', '\\', '|', '@', '#',
                '$', '%', '^', '&', '*', '+', '=', '<', '>', '~'
        };
        for (char c : punctuationChars) {
            text = text.replaceAll(Pattern.quote(String.valueOf(c)), "");
        }
        return text;
    }


    // "Remove Diacritics"
    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }


    // word counting implementation
    public static int countWords(String text) {
        // If the text is empty, count is 0, otherwise split by whitespace and count
        int wordCount = text.isEmpty() ? 0 : text.split("\\s+").length;
        // Update the word count display
        return wordCount;
    }


}
