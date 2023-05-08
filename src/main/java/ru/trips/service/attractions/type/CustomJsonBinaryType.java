package ru.trips.service.attractions.type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import java.util.Properties;

/**
 * Используется для null-безопасной установки значений типа JSON.
 */
public class CustomJsonBinaryType extends JsonBinaryType {

    @Override
    public void setParameterValues(Properties parameters) {
        if (parameters.containsKey(PARAMETER_TYPE)) {
            super.setParameterValues(parameters);
        }
    }
}
