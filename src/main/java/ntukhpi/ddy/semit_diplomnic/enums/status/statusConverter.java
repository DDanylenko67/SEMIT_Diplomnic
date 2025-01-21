package ntukhpi.ddy.semit_diplomnic.enums.status;

import jakarta.persistence.AttributeConverter;

public class statusConverter implements AttributeConverter<status, Integer> {
    @Override
    public Integer convertToDatabaseColumn(status status) {
        return status.ordinal();
    }

    @Override
    public status convertToEntityAttribute(Integer codVR) {
        return status.values()[codVR];
    }
}
