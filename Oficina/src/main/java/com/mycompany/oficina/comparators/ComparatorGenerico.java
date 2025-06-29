package com.mycompany.oficina.comparators;

import java.util.Comparator;
import java.util.function.Function;

public class ComparatorGenerico<T> implements Comparator<T> {
    private final Function<T, String> extratorAtributo;
    public ComparatorGenerico(Function<T, String> extratorAtributo) {
        this.extratorAtributo = extratorAtributo;
    }
    @Override
    public int compare(T o1, T o2) {
        // Extrai as strings dos objetos usando a função fornecida
        String str1 = extratorAtributo.apply(o1);
        String str2 = extratorAtributo.apply(o2);

        // Lógica de comparação de strings (idêntica à anterior)
        int len1 = str1.length();
        int len2 = str2.length();
        int lim = Math.min(len1, len2);

        for (int k = 0; k < lim; k++) {
            char ch1 = str1.charAt(k);
            char ch2 = str2.charAt(k);
            if (ch1 != ch2) {
                return ch1 - ch2;
            }
        }
        return len1 - len2;
    }
}
