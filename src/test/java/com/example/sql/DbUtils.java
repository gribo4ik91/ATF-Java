package com.example.sql;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class DbUtils {




    private final JdbcTemplate jdbcTemplate;

    // Белый список разрешённых таблиц — защита от SQL-инъекций
    private static final Set<String> ALLOWED_TABLES = Set.of("Accounts", "Contact");

    public DbUtils(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findByEmail(String tableName, String email) {
        if (!ALLOWED_TABLES.contains(tableName)) {
            throw new IllegalArgumentException("Неверное имя таблицы: " + tableName);
        }

        String sql = String.format("SELECT * FROM %s WHERE email = ?", tableName);
        return jdbcTemplate.queryForList(sql, email);
    }



    public List<Map<String, Object>> findAll(String tableName){
        if (!ALLOWED_TABLES.contains(tableName)) {
            throw new IllegalArgumentException("Неверное имя таблицы: " + tableName);
        }

        String sql = String.format("SELECT * FROM %s ", tableName);
        return jdbcTemplate.queryForList(sql);
    }


}
