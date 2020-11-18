package io.app;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Money {
    private int total;

    public Money(int how){
        total = how;
    }
}
