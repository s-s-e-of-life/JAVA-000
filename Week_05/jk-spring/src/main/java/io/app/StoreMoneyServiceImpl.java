package io.app;


import org.springframework.beans.factory.annotation.Autowired;

public class StoreMoneyServiceImpl implements StoreMonyService {

    @Autowired
    private Bank bank;

    public boolean regist(int cardNo) {
        return bank.regist(cardNo, 0);
    }

    /**
     * 存钱
     *
     * @param money
     * @return
     */
    public boolean store(int cardNo, int money) {
        return bank.store(cardNo, money);
    }

    /**
     * 取钱
     *
     * @return
     */
    public int fetch(int cardNo) {
        return bank.fetch(cardNo);
    }
}
