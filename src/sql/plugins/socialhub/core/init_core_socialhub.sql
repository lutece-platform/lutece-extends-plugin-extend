--
-- Init  table core_admin_right
--
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) 
VALUES ('RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE_TYPE','socialhub.adminFeature.resource_extenders_management_by_resource_type.name',2,'jsp/admin/plugins/socialhub/ManageResourceExtendersByResourceType.jsp','socialhub.adminFeature.resource_extenders_management_by_resource_type.description',0,'socialhub','CONTENT','images/admin/skin/plugins/socialhub/socialhub.png', 'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-socialhub');
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url, documentation_url) 
VALUES ('RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE','socialhub.adminFeature.resource_extenders_management_by_resource.name',2,'jsp/admin/plugins/socialhub/ManageResourceExtendersByResource.jsp','socialhub.adminFeature.resource_extenders_management_by_resource.description',0,'socialhub','CONTENT','images/admin/skin/plugins/socialhub/socialhub.png', 'jsp/admin/documentation/AdminDocumentation.jsp?doc=admin-socialhub');

--
-- Init  table core_user_right
--
INSERT INTO core_user_right (id_right,id_user) VALUES ('RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE_TYPE',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE_TYPE',2);
INSERT INTO core_user_right (id_right,id_user) VALUES ('RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE',2);

--
-- Dumping data for table core_admin_role
--
INSERT INTO core_admin_role (role_key,role_description) VALUES ('socialhub_manager','Gestion des type de ressource de Social Hub');

--
-- Dumping data for table core_admin_role_resource
--
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
 (990,'socialhub_manager','SOCIALHUB_EXTENDABLE_RESOURCE_TYPE','*','*');
INSERT INTO core_admin_role_resource (rbac_id,role_key,resource_type,resource_id,permission) VALUES 
 (991,'socialhub_manager','SOCIALHUB_EXTENDABLE_RESOURCE','*','*');
 
--
-- Dumping data for table core_user_role
--
INSERT INTO core_user_role (role_key,id_user) VALUES ('socialhub_manager',1);
INSERT INTO core_user_role (role_key,id_user) VALUES ('socialhub_manager',2);
