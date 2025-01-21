package ntukhpi.ddy.semit_diplomnic.enums.groupType;

import jakarta.persistence.AttributeConverter;

public class groupTypeConverter implements AttributeConverter<groupType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(groupType groupType) {
        return groupType.ordinal();
    }

    @Override
    public groupType convertToEntityAttribute(Integer codVR) {
        return groupType.values()[codVR];
    }
}
