package vn.com.vtcc.apiExpose.utils;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.UUID;

public class IDGenerator {

    public static String genID() {
        UUID uuid = UuidCreator.getRandomBased();
        return uuid.toString();
    }
}
