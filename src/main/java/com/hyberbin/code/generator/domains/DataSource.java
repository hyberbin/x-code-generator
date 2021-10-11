package com.hyberbin.code.generator.domains;

import lombok.Data;

import java.util.Objects;

@Data
public class DataSource extends BaseDo {

    private String datasource;
    private String url;
    private String username;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        DataSource that = (DataSource) o;

        if (!Objects.equals(datasource, that.datasource)) {
            return false;
        }
        if (!Objects.equals(url, that.url)) {
            return false;
        }
        if (!Objects.equals(username, that.username)) {
            return false;
        }
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + datasource.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }
}
