--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) 
VALUES ('RESOURCE_EXTENDER_MANAGEMENT','extend.adminFeature.resource_extenders_management.name',2,'jsp/admin/plugins/extend/ManageResourceExtendersByResource.jsp','extend.adminFeature.resource_extenders_management.description',0,'extend','CONTENT','images/admin/skin/plugins/extend/extend.png', 'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-extend');

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('RESOURCE_EXTENDER_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('RESOURCE_EXTENDER_MANAGEMENT',2);

--
-- Dumping data for table core_admin_role
--
INSERT INTO core_admin_role (role_key,role_description) VALUES ('extend_manager','Gestion des type de ressource d\'extend');

--
-- Dumping data for table core_admin_role_resource '
--
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
 (990,'extend_manager','EXTEND_EXTENDABLE_RESOURCE_TYPE','*','*');
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
 (991,'extend_manager','EXTEND_EXTENDABLE_RESOURCE','*','*');
 
--
-- Dumping data for table core_user_role
--
INSERT INTO core_user_role (role_key,id_user) VALUES ('extend_manager',1);
INSERT INTO core_user_role (role_key,id_user) VALUES ('extend_manager',2);
