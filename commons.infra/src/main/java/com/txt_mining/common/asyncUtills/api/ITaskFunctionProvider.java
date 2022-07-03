package com.txt_mining.common.asyncUtills.api;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * interface whihc provides evecutive task function,which performs the task
 */
public interface ITaskFunctionProvider {
    /**
     * returns the executive function
     * @return -the fuunction announced
     */
    Supplier<Function<?,String>> executiveFunction();
}
