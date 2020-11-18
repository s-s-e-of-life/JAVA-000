package io.app;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bank {

    private String name;

    private Map<Integer, Integer> lib = new LinkedHashMap<Integer, Integer>(100);

    public Bank(String name) {
        this.name = name;
    }

    public boolean store(int cardNo, int money) {
        Integer total = this.lib.get(cardNo);

        if (null == total)
            throw new RuntimeException("The card not registed! ");

        total += money;

        this.lib.remove(cardNo);
        this.lib.put(cardNo, total);

        return true;
    }

    public int fetch(int cardNo) {
        Integer total = this.lib.get(cardNo);

        if (null == total) throw new RuntimeException("The card not registed! ");

        return total;
    }

    public boolean regist(int cardNo, int init) {
        Integer total = this.lib.get(cardNo);
        if (null == total) {
            this.lib.put(cardNo, init);
            return true;
        }

        return false;
    }
}
