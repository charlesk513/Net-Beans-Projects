package charlesk.com.hospital_management_system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic, serializable data-transfer object shared by every GUI frame.
 *
 * Each frame puts its own database fields into this object:
 *     DataDTO dto = new DataDTO();
 *     dto.put("first_name", txtFirstName.getText());
 *     dto.put("patient_id", Integer.valueOf(txtPatientId.getText()));
 *
 * The server reads the values by their exact database column names:
 *     String firstName = dto.getString("first_name");
 *     Integer patientId = dto.getInteger("patient_id");
 *
 * values serializable: String, Integer, Long, Double, BigDecimal,
 * Boolean, and other serializable types. For dates/times, ISO strings
 * such as yyyy-MM-dd and HH:mm:ss are a simple transport format.
 */
public class DataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Object> fields = new HashMap<>();

    /** Add or replace a field. Use the exact MySQL column name as the key. */
    public void put(String fieldName, Object value) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be blank.");
        }
        fields.put(fieldName, value);
    }

    /** Get a raw value when you need to perform your own conversion. */
    public Object get(String fieldName) {
        return fields.get(fieldName);
    }

    public String getString(String fieldName) {
        Object value = fields.get(fieldName);
        return value == null ? null : value.toString();
    }

    public Integer getInteger(String fieldName) {
        Object value = fields.get(fieldName);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.valueOf(value.toString());
    }

    public Long getLong(String fieldName) {
        Object value = fields.get(fieldName);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.valueOf(value.toString());
    }

    public Double getDouble(String fieldName) {
        Object value = fields.get(fieldName);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return Double.valueOf(value.toString());
    }

    public BigDecimal getBigDecimal(String fieldName) {
        Object value = fields.get(fieldName);
        if (value == null) return null;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        return new BigDecimal(value.toString());
    }

    public Boolean getBoolean(String fieldName) {
        Object value = fields.get(fieldName);
        if (value == null) return null;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.valueOf(value.toString());
    }

    public boolean contains(String fieldName) {
        return fields.containsKey(fieldName);
    }

    public boolean isEmpty() {
        return fields.isEmpty();
    }

    /** Returns an unmodifiable view; callers cannot alter the DTO's map directly.*/
    public Map<String, Object> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    @Override
    public String toString() {
        return "DataDTO" + fields;
    }
}
