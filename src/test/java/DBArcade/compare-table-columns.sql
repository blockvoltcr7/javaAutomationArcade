SELECT
    NVL(a.column_name, b.column_name) AS column_name,
    CASE
        WHEN a.column_name IS NOT NULL AND b.column_name IS NOT NULL THEN 'Exists in both schemas'
        WHEN a.column_name IS NOT NULL THEN 'Only in PORTA'
        WHEN b.column_name IS NOT NULL THEN 'Only in PORTB'
    END AS presence,
    a.data_type AS porta_data_type,
    b.data_type AS portb_data_type,
    a.data_length AS porta_data_length,
    b.data_length AS portb_data_length,
    a.nullable AS porta_nullable,
    b.nullable AS portb_nullable,
    CASE
        WHEN a.data_type = b.data_type AND a.data_length = b.data_length THEN 'Match'
        ELSE 'Mismatch'
    END AS column_match
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
