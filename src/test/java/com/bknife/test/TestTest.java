package com.bknife.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.bknife.orm.util.FilterVisitor;
import com.bknife.orm.util.Filter;

public class TestTest {
    @Test
    public void test() throws Exception {
        Collection<Integer> vals = new ArrayList<Integer>();
        for (int i = 0; i < 100; i++)
            vals.add(i);
        Iterable<Integer> filter = new Filter<Integer>(vals, new FilterVisitor<Integer>() {
            @Override
            public boolean filter(Integer object) {
                return object.intValue() % 5 == 0;
            }
        });

        for (Integer integer : filter) {
            System.out.println(integer);
            Thread.sleep(50);
        }
    }
}
