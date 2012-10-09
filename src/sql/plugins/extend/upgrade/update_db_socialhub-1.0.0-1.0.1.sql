--
-- Structure for table socialhub_resource_extender_history
--
DROP TABLE IF EXISTS socialhub_resource_extender_history;
CREATE TABLE socialhub_resource_extender_history (
	id_history bigint DEFAULT 0 NOT NULL,
	extender_type VARCHAR(255) DEFAULT '' NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	user_guid VARCHAR(255) DEFAULT '' NOT NULL,
	ip_address VARCHAR(100) DEFAULT '' NOT NULL,
	date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (id_history)
);
