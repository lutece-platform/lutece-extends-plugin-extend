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
