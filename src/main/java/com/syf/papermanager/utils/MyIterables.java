package com.syf.papermanager.utils;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.utils
 * @description: 代索引index的lambda遍历工具
 * @author: songyafeng
 * @create_time: 2020/12/9 21:41
 */
public class MyIterables {
    public static <E> void forEach(Iterable<? extends E> elements,
                                   BiConsumer<Integer, ? super E> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);
        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}
