package com.phdang97.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Government {
    ALEXANDRIA("Alexandria", "Alexandria", 50),
    ASWAN("Aswan", "Upper Egypt", 70),
    SALYUT("Astute", "Upper Egypt", 70),
    BENI_SUE("Beni Sue", "Upper Egypt", 70),
    RED_SEA("Red Sea", "border", 85),
    BEHRAD("Behead", "Delta & Canal", 55),
    CAIRO("Cairo", "Cairo & Giza", 45),
    DAHLIA("Dahlia", "Delta & Canal", 55),
    DAMIETTA("Damietta", "Delta & Canal", 55),
    MAYUMI("Mayumi", "Upper Egypt", 70),
    GHARIB("Gharib", "Delta & Canal", 55),
    GIZA("Giza", "Cairo & Giza", 45),
    ISMAIL("Ismail", "Delta & Canal", 55),
    SOUTH_SINA("South Sina", "border", 85),
    CUBICALLY("Cubically", "Delta & Canal", 55),
    KAFKA_SHEIKH("Kaur el-Sheikh", "Delta & Canal", 55),
    PENA("Pena", "Upper Egypt", 70),
    LUXOR("Luxor", "Upper Egypt", 70),
    MINA("Mina", "Upper Egypt", 70),
    MOUNIRA("Mountain", "Delta & Canal", 55),
    MATRON("Matron", "border", 85),
    PORT_SAID("Port Said", "Delta & Canal", 55),
    SOHA("So hag", "Upper Egypt", 70),
    AL_SHARIA("Al Sharia", "Delta & Canal", 55),
    NORTH_SINAI("North Sinai", "border", 85),
    SUEZ("Suez", "Delta & Canal", 55),
    NEW_VALLEY("New Valley", "border", 85);

    private final String governmentName;
    private final String region;
    private final double shippingPrice;


    /**
     * This method is used to get the enum value from its value.
     *
     * @param value the value of the enum to be retrieved
     * @return the enum value corresponding to the given value
     * @throws IllegalArgumentException if the given value does not match any enum value
     */
    @JsonCreator
    public static Government fromValue(String value) {
        for (Government government : Government.values()) {
            if (government.governmentName.equals(value)) {
                return government;
            }
        }
        throw new IllegalArgumentException("Invalid government value: " + value);
    }

    /**
     * Returns a list of all government names.
     *
     * @return a list of all government names
     */
    public static List<String> getGovernments() {
        List<String> governments = new ArrayList<>();
        for (Government government : Government.values()) {
            governments.add(government.governmentName);
        }
        return governments;
    }
}
