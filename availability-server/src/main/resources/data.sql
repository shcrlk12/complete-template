--User Table
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_USER', 'kim garden', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon2@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_MANAGER', 'kim jae woo', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon3@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_ADMIN', 'kim won', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon4@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_USER', 'kim garden', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon5@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_MANAGER', 'kim jae woo', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon6@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_ADMIN', 'kim won', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon7@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_USER', 'kim garden', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon8@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_MANAGER', 'kim jae woo', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon9@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_ADMIN', 'kim won', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon10@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_USER', 'kim garden', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon11@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_MANAGER', 'kim jae woo', CURRENT_TIMESTAMP());
INSERT INTO users (id, pw, role, name, created_at) VALUES ('kjwon12@unison.co.kr', '$2a$12$Vxiv/v2kiznDm1QmQv6fVO8bIrqNiChKWOQeV4Ke0l4vvLEQ0mdvm', 'ROLE_ADMIN', 'kim won', CURRENT_TIMESTAMP());

-- Memo Table
INSERT INTO memo (register_time, turbine_id,engineer_name, work_time, created_at, created_by) VALUES (CURRENT_TIMESTAMP(), 1, 'korean', CURRENT_TIMESTAMP() , CURRENT_TIMESTAMP(), 'korean name');
INSERT INTO memo (register_time, turbine_id,engineer_name, work_time, created_at, created_by) VALUES (CURRENT_TIMESTAMP(), 2, 'korean2', CURRENT_TIMESTAMP() , CURRENT_TIMESTAMP(), 'korean name2');
INSERT INTO memo (register_time, turbine_id,engineer_name, work_time, created_at, created_by) VALUES (CURRENT_TIMESTAMP(), 3, 'korean3', CURRENT_TIMESTAMP() , CURRENT_TIMESTAMP(), 'korean name3');
INSERT INTO memo (register_time, turbine_id,engineer_name, work_time, created_at, created_by) VALUES (CURRENT_TIMESTAMP(), 4, 'korean4', CURRENT_TIMESTAMP() , CURRENT_TIMESTAMP(), 'korean name4');
INSERT INTO memo (register_time, turbine_id,engineer_name, work_time, created_at, created_by) VALUES (CURRENT_TIMESTAMP(), 2, 'korean5', CURRENT_TIMESTAMP() , CURRENT_TIMESTAMP(), 'korean name5');

-- Variable Table
--INSERT INTO variable (name, group_name, mapping_opc_id, is_opc_communicate, created_at) VALUES ('Full performance', 'Availability', '100');

-- AvailabilityType Table
SET @normal_uuid = UUID();
SET @forced_uuid = UUID();
SET @scheduled_uuid = UUID();
SET @information_uuid = UUID();
SET @requested_uuid = UUID();
SET @etc_uuid = UUID();

INSERT INTO availability_type (uuid, name, created_at) VALUES (@normal_uuid, 'normal status', CURRENT_TIMESTAMP());
INSERT INTO availability_type (uuid, name, created_at) VALUES (@forced_uuid, 'forced outage', CURRENT_TIMESTAMP());
INSERT INTO availability_type (uuid, name, created_at) VALUES (@scheduled_uuid, 'scheduled maintenance', CURRENT_TIMESTAMP());
INSERT INTO availability_type (uuid, name, created_at) VALUES (@information_uuid, 'information unavailable', CURRENT_TIMESTAMP());
INSERT INTO availability_type (uuid, name, created_at) VALUES (@requested_uuid, 'requested shutdown', CURRENT_TIMESTAMP());
INSERT INTO availability_type (uuid, name, created_at) VALUES (@etc_uuid, 'etc', CURRENT_TIMESTAMP());

-- AvailabilityData Table
INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) VALUES (CURRENT_TIMESTAMP(), 1,  UUID(), 2000, @normal_uuid, CURRENT_TIMESTAMP());
INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) VALUES (CURRENT_TIMESTAMP(), 1,  UUID(), 450, @forced_uuid, CURRENT_TIMESTAMP());
INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) VALUES (CURRENT_TIMESTAMP(), 1,  UUID(), 550, @scheduled_uuid, CURRENT_TIMESTAMP());
INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) VALUES (CURRENT_TIMESTAMP(), 1,  UUID(), 100, @information_uuid, CURRENT_TIMESTAMP());
INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) VALUES (CURRENT_TIMESTAMP(), 1,  UUID(), 300, @requested_uuid, CURRENT_TIMESTAMP());
INSERT INTO availability_data (timestamp, turbine_id, uuid, time, availability_type_uuid, created_at) VALUES (CURRENT_TIMESTAMP(), 1,  UUID(), 200, @etc_uuid, CURRENT_TIMESTAMP());

