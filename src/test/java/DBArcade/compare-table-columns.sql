SELECT
    NVL(a.column_name, b.column_name) AS column_name,
    CASE
        WHEN a.column_name IS NOT NULL AND b.column_name IS NOT NULL THEN 'Exists in both schemas'
        WHEN a.column_name IS NOT NULL THEN 'Only in PORTA'
        WHEN b.column_name IS NOT NULL THEN 'Only in PORTB'
    END AS presence,
    NVL(a.data_type, b.data_type) AS data_type,
    NVL(a.data_length, b.data_length) AS data_length,
    NVL(a.nullable, b.nullable) AS nullable
FROM
    (SELECT column_name, data_type, data_length, nullable
     FROM all_tab_columns
     WHERE owner = 'PORTA' AND table_name = 'USERS_TABLE') a
FULL OUTER JOIN
    (SELECT column_name, data_type, data_length, nullable
     FROM all_tab_columns
     WHERE owner = 'PORTB' AND table_name = 'USERS_TABLE') b
ON
    a.column_name = b.column_name
ORDER BY
    column_name;
