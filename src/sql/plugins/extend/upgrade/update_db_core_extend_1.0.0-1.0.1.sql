--liquibase formatted sql
--changeset extend:update_db_core_extend_1.0.0-1.0.1.sql
--preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET id_right = 'RESOURCE_EXTENDER_MANAGEMENT', name = 'extend.adminFeature.resource_extenders_management.name', description = 'extend.adminFeature.resource_extenders_management.description' WHERE id_right = 'RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE';
UPDATE core_user_right SET id_right = 'RESOURCE_EXTENDER_MANAGEMENT' WHERE id_right = 'RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE';
DELETE FROM core_user_right WHERE id_right = 'RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE_TYPE';
DELETE FROM core_admin_right WHERE id_right = 'RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE_TYPE';
