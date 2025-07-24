package dev.erick.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Status {
    ATIVO,
    INATIVO;

    private static Map<String, Status> enums = new ConcurrentHashMap<String, Status>();

    static {
        for (Status eenum : values()) {
            enums.put(eenum.toString(), eenum);
        }
    }

    public static Status fromString(String status) {
        return enums.get(status);
    }
}
