-- liquibase formatted sql
-- changeset extend:update_db_extend-2.0.1-2.0.2.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET icon_url = 'ti ti-puzzle' WHERE id_right = 'RESOURCE_EXTENDER_MANAGEMENT';

-- changeset extend:update_db_extend-2.0.1-2.0.2-rev1-mariaDB-mySQL.sql dbms:mariadb,mysql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'extend_resource_extender' AND column_name = 'id_extender' AND extra NOT LIKE '%auto_increment%'
SET @extend_sql_mode = @@SESSION.sql_mode;
SET SESSION sql_mode = CONCAT_WS( ',', NULLIF( @@SESSION.sql_mode, '' ), 'NO_AUTO_VALUE_ON_ZERO' );
ALTER TABLE extend_resource_extender MODIFY COLUMN id_extender INT AUTO_INCREMENT;
SET SESSION sql_mode = @extend_sql_mode;

-- changeset extend:update_db_extend-2.0.1-2.0.2-rev2-mariaDB-mySQL.sql dbms:mariadb,mysql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'extend_extender_hit' AND column_name = 'id_hit' AND extra NOT LIKE '%auto_increment%'
SET @extend_sql_mode = @@SESSION.sql_mode;
SET SESSION sql_mode = CONCAT_WS( ',', NULLIF( @@SESSION.sql_mode, '' ), 'NO_AUTO_VALUE_ON_ZERO' );
ALTER TABLE extend_extender_hit MODIFY COLUMN id_hit INT AUTO_INCREMENT;
SET SESSION sql_mode = @extend_sql_mode;

-- changeset extend:update_db_extend-2.0.1-2.0.2-rev3-mariaDB-mySQL.sql dbms:mariadb,mysql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'extend_resource_extender_history' AND column_name = 'id_history' AND extra NOT LIKE '%auto_increment%'
SET @extend_sql_mode = @@SESSION.sql_mode;
SET SESSION sql_mode = CONCAT_WS( ',', NULLIF( @@SESSION.sql_mode, '' ), 'NO_AUTO_VALUE_ON_ZERO' );
ALTER TABLE extend_resource_extender_history MODIFY COLUMN id_history BIGINT AUTO_INCREMENT;
SET SESSION sql_mode = @extend_sql_mode;

-- changeset extend:update_db_extend-2.0.1-2.0.2-rev4.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM core_admin_role WHERE role_key = 'extend_manager' AND role_description = 'Gestion des type de ressource d''extend'
UPDATE core_admin_role SET role_description = 'Gestion des types de ressources d''extend' WHERE role_key = 'extend_manager' AND role_description = 'Gestion des type de ressource d''extend';

-- changeset extend:update_db_extend-2.0.1-2.0.2-rev5.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment LUT-34129 : decodes, once, the HTML entities the removed input filter of lutece-core 8 (sanitizeFilterMode, LUT-34109) wrote into the plain text values of plugin-extend: the name of a default extendable resource and the id of the extended resource, typed by the administrator or read from a request, in every table that holds it so that the extenders, the hits and the history still match the same resource. The type columns (codes chosen in a list), the user guid and the IP address (set by the server) are not typed and are left as they are; plugin-extend stores no rich text.
UPDATE extend_default_extendable_resource SET name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE name LIKE '%&%;%';
UPDATE extend_default_extendable_resource SET id_resource = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(id_resource, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE id_resource LIKE '%&%;%';
UPDATE extend_resource_extender SET id_resource = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(id_resource, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE id_resource LIKE '%&%;%';
UPDATE extend_extender_hit SET id_resource = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(id_resource, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE id_resource LIKE '%&%;%';
UPDATE extend_resource_extender_history SET id_resource = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(id_resource, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE id_resource LIKE '%&%;%';
