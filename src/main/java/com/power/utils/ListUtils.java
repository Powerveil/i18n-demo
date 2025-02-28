package com.power.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ListUtils {

    /**
     * 集合双重净化处理器（过滤空值 + 有序去重）
     *
     * <p>执行流程：
     * 1. 使用 {@link #getNonNullCollection} 过滤所有 null 元素
     * 2. 通过 {@link #removeDuplicates} 进行顺序保留型去重
     * </p>
     *
     * @param list 原始集合（允许null元素和重复项）
     * @param <T>  集合元素类型
     * @return 经过空值过滤和顺序保留去重的新ArrayList
     * @see #getNonNullCollection 空值过滤实现
     * @see #removeDuplicates 有序去重实现
     */
    public static <T> List<T> removeDuplicatesAndFilterNulls(List<T> list) {
        return removeDuplicates(getNonNullCollection(list, Collectors.toList()));
    }

    /**
     * 空值安全集合转换器
     *
     * <p>特性：
     * 1. 输入集合为null时返回空集合（避免NPE）
     * 2. 自动过滤所有null元素
     * 3. 支持自定义收集器类型转换
     * </p>
     *
     * @param sourceCollection 源集合（可能包含null）
     * @param collector        目标集合构造器（如Collectors.toList()）
     * @param <T>              元素类型
     * @param <C>              目标集合类型
     * @return 非空元素组成的新集合（类型由collector决定）
     */
    public static <T, C extends Collection<T>> C getNonNullCollection(Collection<T> sourceCollection, Collector<T, ?, C> collector) {
        return CollUtil.newArrayList(sourceCollection).stream()
                .filter(Objects::nonNull)
                .collect(collector);
    }

    /**
     * 顺序敏感型去重处理器
     *
     * <p>实现原理：
     * 使用 {@link LinkedHashSet} 的特性：
     * 1. 自动过滤重复元素
     * 2. 保留首次出现顺序
     * 3. 允许null元素（当输入列表允许null时）
     * </p>
     *
     * @param list 原始集合（可能包含重复项）
     * @param <T>  元素类型
     * @return 去重后的新ArrayList（保留原始顺序）
     */
    public static <T> List<T> removeDuplicates(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return list;
        }

        // 使用 LinkedHashSet 保持顺序
        Set<T> seen = new LinkedHashSet<>();

        for (T item : list) {
            // 自动过滤重复元素
            if (seen.add(item)) {
                // 此处无需额外操作，add() 返回值可用于判断是否重复
            }
        }
        return CollUtil.newArrayList(seen);
    }

}
