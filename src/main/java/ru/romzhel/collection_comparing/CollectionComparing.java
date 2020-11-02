package ru.romzhel.collection_comparing;

import java.util.ArrayList;
import java.util.List;

public class CollectionComparing {
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(4);

        System.out.println(simpleListsCompare(list1, list2));
    }

    /**
     * Метод простого сравнения двух списков
     * Возвращает новый список с элементами, присутствующими в одном, но отсутствующими в другом списке
     * <p>
     * Примечание: дубликаты не учитываются
     */
    public static <T> List<T> simpleListsCompare(List<T> list1, List<T> list2) {
        List<T> tl1 = new ArrayList<>(list1);
        List<T> tl2 = new ArrayList<>(list2);

        tl1.removeAll(list2);
        tl2.removeAll(list1);

        List<T> result = new ArrayList<>(tl1);
        result.addAll(tl2);

        return result;
    }
}
