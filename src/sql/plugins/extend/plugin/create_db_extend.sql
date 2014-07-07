--
-- Structure for table extend_resource_extender
-- This table is used to enable extender for the given resources
--
DROP TABLE IF EXISTS extend_resource_extender;
CREATE TABLE extend_resource_extender (
	id_extender INT DEFAULT 0 NOT NULL,
	extender_type VARCHAR(255) DEFAULT '' NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	 is_active INT DEFAULT 1 NOT NULL,
	PRIMARY KEY (id_extender)
);

--
-- Structure for table extend_default_extendable_resource
--
DROP TABLE IF EXISTS extend_default_extendable_resource;
CREATE TABLE extend_default_extendable_resource (
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	name VARCHAR(255) DEFAULT '' NOT NULL
);

--
-- Structure for table extend_extender_hit
--
DROP TABLE IF EXISTS extend_extender_hit;
CREATE TABLE extend_extender_hit (
	id_hit INT DEFAULT 0 NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	nb_hits INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (id_hit)
);

--
-- Structure for table extend_resource_extender_history
--
DROP TABLE IF EXISTS extend_resource_extender_history;
CREATE TABLE extend_resource_extender_history (
	id_history bigint DEFAULT 0 NOT NULL,
	extender_type VARCHAR(255) DEFAULT '' NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	user_guid VARCHAR(255) DEFAULT '' NOT NULL,
	ip_address VARCHAR(100) DEFAULT '' NOT NULL,
	date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	PRIMARY KEY (id_history)
);
