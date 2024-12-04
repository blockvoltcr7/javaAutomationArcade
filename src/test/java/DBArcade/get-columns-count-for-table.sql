SELECT
    'PORTA' AS schema_name,
    COUNT(column_name) AS column_count
FROM
    all_tab_columns
WHERE
    owner = 'PORTA'
    AND table_name = 'USERS_TABLE'
UNION ALL
SELECT
    'PORTB' AS schema_name,
    COUNT(column_name) AS column_count
FROM
    all_tab_columns
WHERE
    owner = 'PORTB'
    AND table_name = 'USERS_TABLE';
