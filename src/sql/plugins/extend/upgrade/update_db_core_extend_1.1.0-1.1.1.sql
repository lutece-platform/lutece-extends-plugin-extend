--liquibase formatted sql
--changeset extend:update_db_core_extend_1.1.0-1.1.1.sql
--preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE extend_resource_extender ADD COLUMN is_active INT DEFAULT 1 NOT NULL;