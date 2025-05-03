package lk.ijse.dep13.eduforge.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LecturerType {
    FULL_TIME ("full-time"), VISITING ("visiting");

    private String type;

    LecturerType(String type){
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
