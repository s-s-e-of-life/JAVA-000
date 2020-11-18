package io.app;

public interface StoreMonyService {

    boolean regist(int cardNo);

    /**
     * 存钱
     * @param money
     * @return
     */
    boolean store(int cardNo, int money);

    /**
     * 取钱
     * @return
     */
    int fetch(int cardNo);

}
