package vn.com.vtcc.apiExpose.entity.dto;

public class StructType {
    private String field;
    private String type;

    public StructType() {
    }

    public StructType(String field, String type) {
        this.field = field;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
