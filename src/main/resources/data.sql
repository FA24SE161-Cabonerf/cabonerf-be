-- Table: subscription_type
INSERT INTO subscription_type(id, subscription_name, description, project_limit, usage_limit, annual_cost, monthly_cost, can_create_organization, status)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Basic', 'Người dùng cá nhân với hạn chế cơ bản', 5, 1000, 0, 0, false, true),
    ('223e4567-e89b-12d3-a456-426614174001', 'Pro', 'Người dùng chuyên nghiệp với nhiều tính năng hơn', 20, 10000, 199.99, 19.99, true, true),
    ('323e4567-e89b-12d3-a456-426614174002', 'Enterprise', 'Doanh nghiệp với khả năng mở rộng tối đa', 100, 100000, 999.99, 99.99, true, true);

-- Table: user_verify_status
INSERT INTO user_verify_status(id, status_name, description, status)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Pending', 'Người dùng đã đăng ký nhưng chưa được xác minh', true),
    ('123e4567-e89b-12d3-a456-426614174001', 'Verified', 'Người dùng đã được xác minh thành công', true),
    ('123e4567-e89b-12d3-a456-426614174002', 'Suspended', 'Người dùng bị tạm ngừng xác minh', true);

-- Table: user_status
INSERT INTO user_status(id, status_name, description, status)
VALUES
    ('223e4567-e89b-12d3-a456-426614174000', 'Active', 'Người dùng đang hoạt động bình thường', true),
    ('223e4567-e89b-12d3-a456-426614174001', 'Inactive', 'Người dùng không hoạt động', true),
    ('223e4567-e89b-12d3-a456-426614174002', 'Banned', 'Người dùng đã bị cấm truy cập hệ thống', true);

-- Table: role
INSERT INTO role(id, name, status)
VALUES
    ('323e4567-e89b-12d3-a456-426614174000', 'System Admin', true),
    ('323e4567-e89b-12d3-a456-426614174001', 'Manager', true),
    ('323e4567-e89b-12d3-a456-426614174002', 'Organization Manager', true),
    ('323e4567-e89b-12d3-a456-426614174003', 'LCA Staff', true);

-- Table: emission_compartment
INSERT INTO emission_compartment(id, name, status)
VALUES
    ('423e4567-e89b-12d3-a456-426614174000', 'Air', true),
    ('423e4567-e89b-12d3-a456-426614174001', 'Fresh water', true),
    ('423e4567-e89b-12d3-a456-426614174002', 'Marine water', true),
    ('423e4567-e89b-12d3-a456-426614174003', 'Agricultural Soil', true),
    ('423e4567-e89b-12d3-a456-426614174004', 'Sea water', true),
    ('423e4567-e89b-12d3-a456-426614174005', 'PM2.5', true),
    ('423e4567-e89b-12d3-a456-426614174006', 'Urban Air', true),
    ('423e4567-e89b-12d3-a456-426614174007', 'Industrial Soil', true),
    ('423e4567-e89b-12d3-a456-426614174008', 'Industrial Fresh Water', true),
    ('423e4567-e89b-12d3-a456-426614174009', 'Annual Crop Land', true),
    ('423e4567-e89b-12d3-a456-426614174010', 'Water Consumed', true);

-- Table: perspective
INSERT INTO perspective(id, name, description, abbr, status)
VALUES
    ('523e4567-e89b-12d3-a456-426614174000', 'Individualist', '', 'I', true),
    ('523e4567-e89b-12d3-a456-426614174001', 'Hierarchist', '', 'H', true),
    ('523e4567-e89b-12d3-a456-426614174002', 'Egalitarian', '', 'E', true);

-- Table: unit_group
INSERT INTO unit_group(id, name, unit_group_type, status)
VALUES
    ('623e4567-e89b-12d3-a456-426614174000', 'Unit of Midpoint Impact Category', 'Impact', true),
    ('623e4567-e89b-12d3-a456-426614174001', 'Unit of mass', 'Normal', true);

-- Table: exchanges_type
INSERT INTO exchanges_type(id, name, status)
VALUES
    ('723e4567-e89b-12d3-a456-426614174000', 'Product', true),
    ('723e4567-e89b-12d3-a456-426614174001', 'Elementary', true);

-- Table: life_cycle_stage
INSERT INTO life_cycle_stage(id, name, description, status)
VALUES
    ('823e4567-e89b-12d3-a456-426614174000', 'Material Acquisition & Pre-Processing', 'Resource extraction & transport to the production facility gate.', true),
    ('823e4567-e89b-12d3-a456-426614174001', 'Production', 'From input materials to output products of the production facility.', true),
    ('823e4567-e89b-12d3-a456-426614174002', 'Distribution & Storage', 'From the production facility gate to consumer possession.', true),
    ('823e4567-e89b-12d3-a456-426614174003', 'Use', 'From start to finish of consumer possession and use.', true),
    ('823e4567-e89b-12d3-a456-426614174004', 'End-of-life', 'From consumer to recycling or returning to nature.', true),
    ('823e4567-e89b-12d3-a456-426614174005', 'Blank', '', true);

-- Table: users
INSERT INTO users(id, email, full_name, password, role_id, subscription_id, user_status_id, user_verify_status_id, status)
VALUES
    ('923e4567-e89b-12d3-a456-426614174000', 'company.admin.test@gmail.com', 'Organization Manager', '$2a$10$DmzY62bQ0jHOcWKg5aAuk.LLUrxHYX.7Y/e/psbjNo10427cZiaxK', '323e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174001', true),
    ('923e4567-e89b-12d3-a456-426614174001', 'system.admin.test@gmail.com', 'Admin', '$2a$10$1mBdYye1JJF49P7nfUy0.u7.oT9a7CsirV07UNaQBwFW7nOf.kplO', '323e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174001', true),
    ('923e4567-e89b-12d3-a456-426614174002', 'manager.test@gmail.com', 'Manager', '$2a$10$f7a6U4tgbd8mw4Z.bYFXIeSVM3vtyJIms9d8sZlcaX42LSQI9Z1Yq', '323e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174000', '223e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174001', true),
    ('923e4567-e89b-12d3-a456-426614174003', 'qminh.workmode@gmail.com', 'Tran Quang Minh', '$2a$10$XI2JxRZMsqk1aaNBMzflyunz76laG8wj0NBh6xwNa1IQG5o30wMFu','323e4567-e89b-12d3-a456-426614174003','123e4567-e89b-12d3-a456-426614174000','223e4567-e89b-12d3-a456-426614174000','123e4567-e89b-12d3-a456-426614174001', true);

insert into workspace(id ,name, owner_id,status)
values ( '6ccbff7f-9653-44c0-8ddb-e7728f12e5a0','Dev','923e4567-e89b-12d3-a456-426614174003',true);

INSERT INTO life_cycle_impact_assessment_method (id, name, description, version, reference, perspective_id, status)
VALUES
    ('923e4567-e89b-12d3-a456-426614174000', 'ReCiPe 2016 Midpoint', '', 'v1.03', '', '523e4567-e89b-12d3-a456-426614174000', true),
    ('923e4567-e89b-12d3-a456-426614174001', 'ReCiPe 2016 Midpoint', '', 'v1.03', '', '523e4567-e89b-12d3-a456-426614174001', true),
    ('923e4567-e89b-12d3-a456-426614174002', 'ReCiPe 2016 Midpoint', '', 'v1.03', '', '523e4567-e89b-12d3-a456-426614174002', true);


-- Insert into unit
INSERT INTO unit (id, name, unit_group_id, is_default, status, conversion_factor)
VALUES
    ('723e4567-e89b-12d3-a456-426614174000', 'kg CO2', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174001', 'kg CFC-11', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174002', 'kBq Co-60', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174003', 'kg PM2.5', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174004', 'kg NOx', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174005', 'kg SO2', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174006', 'kg P', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174007', 'kg 1,4-DCB', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174008', 'm2×yr', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174009', 'm3', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174010', 'kg Cu', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174011', 'kg oil', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    -- Unit of mass
    ('723e4567-e89b-12d3-a456-426614174012', 'kg', '623e4567-e89b-12d3-a456-426614174001', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174013', 'lb av', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.45),
    ('723e4567-e89b-12d3-a456-426614174014', 'cg', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.00001),
    ('723e4567-e89b-12d3-a456-426614174015', 'oz t', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.03),
    ('723e4567-e89b-12d3-a456-426614174016', 'cwt', '623e4567-e89b-12d3-a456-426614174001', false, true, 45.359237),
    ('723e4567-e89b-12d3-a456-426614174017', 'dg', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.0001),
    ('723e4567-e89b-12d3-a456-426614174018', 'dr (Av)', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.001771845),
    ('723e4567-e89b-12d3-a456-426614174019', 'ng', '623e4567-e89b-12d3-a456-426614174001', false, true, 1e-12),
    ('723e4567-e89b-12d3-a456-426614174020', 'pg', '623e4567-e89b-12d3-a456-426614174001', false, true, 1e-15),
    ('723e4567-e89b-12d3-a456-426614174021', 'gr', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.0000648),
    ('723e4567-e89b-12d3-a456-426614174022', 't', '623e4567-e89b-12d3-a456-426614174001', false, true, 1000),
    ('723e4567-e89b-12d3-a456-426614174023', 'hg', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.1),
    ('723e4567-e89b-12d3-a456-426614174024', 'dag', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.01),
    ('723e4567-e89b-12d3-a456-426614174025', 'kg SWU', '623e4567-e89b-12d3-a456-426614174001', false, true, 1),
    ('723e4567-e89b-12d3-a456-426614174026', 'Mt', '623e4567-e89b-12d3-a456-426614174001', false, true, 1000000000),
    ('723e4567-e89b-12d3-a456-426614174027', 'dwt', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.001555174),
    ('723e4567-e89b-12d3-a456-426614174028', 'ct', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.0002),
    ('723e4567-e89b-12d3-a456-426614174029', 'mg', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.000001),
    ('723e4567-e89b-12d3-a456-426614174030', 'ug', '623e4567-e89b-12d3-a456-426614174001', false, true, 1e-9),
    ('723e4567-e89b-12d3-a456-426614174031', 'Mg', '623e4567-e89b-12d3-a456-426614174001', false, true, 1000),
    ('723e4567-e89b-12d3-a456-426614174032', 'sh tn', '623e4567-e89b-12d3-a456-426614174001', false, true, 907.18),
    ('723e4567-e89b-12d3-a456-426614174033', 'g', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.001),
    ('723e4567-e89b-12d3-a456-426614174034', 'long tn', '623e4567-e89b-12d3-a456-426614174001', false, true, 1016.04),
    ('723e4567-e89b-12d3-a456-426614174035', 'kt', '623e4567-e89b-12d3-a456-426614174001', false, true, 1000000),
    ('723e4567-e89b-12d3-a456-426614174036', 'oz av', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.02),
    -- Unit of factor
    ('723e4567-e89b-12d3-a456-426614174037', 'kg CO2-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174038', 'kg CFC-11-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174039', 'kBq Co-60-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174040', 'kg PM2.5-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174041', 'kg NOx-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174042', 'kg SO2-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174043', 'kg P-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174044', 'kg 1,4-DCB-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174045', 'm2×yr crop-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174046', 'm3', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174047', 'kg Cu-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174048', 'kg oil-eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1);

INSERT INTO midpoint_impact_category(id, name, description, abbr, unit_id, status)
VALUES
    ('234e4567-e89b-12d3-a456-426614174000', 'Global Warming Potential', '', 'GWP', '723e4567-e89b-12d3-a456-426614174000', true),
    ('234e4567-e89b-12d3-a456-426614174001', 'Ozone Depletion Potential', '', 'ODP', '723e4567-e89b-12d3-a456-426614174001', true),
    ('234e4567-e89b-12d3-a456-426614174002', 'Ionizing Radiation Potential', '', 'IRP', '723e4567-e89b-12d3-a456-426614174002', true),
    ('234e4567-e89b-12d3-a456-426614174003', 'Particulate Matter Formation Potential', '', 'PMFP', '723e4567-e89b-12d3-a456-426614174003', true),
    ('234e4567-e89b-12d3-a456-426614174004', 'Photochemical Oxidant Formation Potential: Ecosystems', '', 'EOFP', '723e4567-e89b-12d3-a456-426614174004', true),
    ('234e4567-e89b-12d3-a456-426614174005', 'Photochemical Oxidant Formation Potential: Humans', '', 'HOFP', '723e4567-e89b-12d3-a456-426614174004', true),
    ('234e4567-e89b-12d3-a456-426614174006', 'Terrestrial Acidification Potential', '', 'TAP', '723e4567-e89b-12d3-a456-426614174005', true),
    ('234e4567-e89b-12d3-a456-426614174007', 'Freshwater Eutrophication Potential', '', 'FEP', '723e4567-e89b-12d3-a456-426614174006', true),
    ('234e4567-e89b-12d3-a456-426614174008', 'Human Toxicity Potential', '', 'HTPc', '723e4567-e89b-12d3-a456-426614174007', true),
    ('234e4567-e89b-12d3-a456-426614174009', 'Human Toxicity Potential', '', 'HTPnc', '723e4567-e89b-12d3-a456-426614174007', true),
    ('234e4567-e89b-12d3-a456-426614174010', 'Terrestrial Ecotoxicity Potential', '', 'TETP', '723e4567-e89b-12d3-a456-426614174007', true),
    ('234e4567-e89b-12d3-a456-426614174011', 'Freshwater Ecotoxicity Potential', '', 'FETP', '723e4567-e89b-12d3-a456-426614174007', true),
    ('234e4567-e89b-12d3-a456-426614174012', 'Marine Ecotoxicity Potential', '', 'METP', '723e4567-e89b-12d3-a456-426614174007', true),
    ('234e4567-e89b-12d3-a456-426614174013', 'Argicultural Land Occupation Potential', '', 'LOP', '723e4567-e89b-12d3-a456-426614174008', true),
    ('234e4567-e89b-12d3-a456-426614174014', 'Water Consumption Potential', '', 'WCP', '723e4567-e89b-12d3-a456-426614174009', true),
    ('234e4567-e89b-12d3-a456-426614174015', 'Surplus Ore Potential', '', 'SOP', '723e4567-e89b-12d3-a456-426614174010', true),
    ('234e4567-e89b-12d3-a456-426614174016', 'Fossil Fuel Potential', '', 'FFP', '723e4567-e89b-12d3-a456-426614174011', true);

INSERT INTO impact_category(id, name, indicator, unit, emission_compartment_id, status, indicator_description, midpoint_impact_category_id, icon_url)
VALUES
    ('123e4567-e89b-12d3-a456-426614174000', 'Climate Change', 'Infra-red Radiative Forcing Increase', 'W×yr/m2', '423e4567-e89b-12d3-a456-426614174000', true, 'The increase in atmospheric temperature due to greenhouse gases trapping heat, contributing to global warming.', '234e4567-e89b-12d3-a456-426614174000', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/thermometer-sun.svg'),
    ('123e4567-e89b-12d3-a456-426614174001', 'Ozone Depletion', 'Stratospheric Ozone Decrease', 'ppt×yr', '423e4567-e89b-12d3-a456-426614174000', true, 'The depletion of the ozone layer, allowing more ultraviolet (UV) rays from the sun to reach the Earth''s surface, potentially harming humans and living organisms.', '234e4567-e89b-12d3-a456-426614174001', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/earth.svg'),
    ('123e4567-e89b-12d3-a456-426614174002', 'Ionizing Radiation', 'Absorbed Dose Increase', 'man×Sv', '423e4567-e89b-12d3-a456-426614174000', true, 'The increase in the amount of harmful substances or radiation absorbed by the human body or environment.', '234e4567-e89b-12d3-a456-426614174002', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/radiation.svg'),
    ('123e4567-e89b-12d3-a456-426614174003', 'Particulate Matter Formation', 'PM2.5 Population Intake Increase', 'kg', '423e4567-e89b-12d3-a456-426614174005', true, 'An increase in the number of fine particles PM2.5 inhaled by people, which can cause lung and respiratory problems.', '234e4567-e89b-12d3-a456-426614174003', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/wind.svg'),
    ('123e4567-e89b-12d3-a456-426614174004', 'Photochemical Oxidant Ecosystem', 'Tropospheric Ozone Increase (AOT40)', 'ppb.yr', '423e4567-e89b-12d3-a456-426614174000', true, 'The increase in ozone concentration in the troposphere (near the ground), which can harm plants and living organisms.', '234e4567-e89b-12d3-a456-426614174004', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/cloud-sun-rain.svg'),
    ('123e4567-e89b-12d3-a456-426614174005', 'Photochemical Oxidant Human', 'Tropospheric Ozone Population Intake Increase (M6M)', 'kg', '423e4567-e89b-12d3-a456-426614174000', true, 'An increase in the amount of ozone inhaled by humans, which can be harmful to health, especially the respiratory system.', '234e4567-e89b-12d3-a456-426614174005', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/cloud-sun-rain.svg'),
    ('123e4567-e89b-12d3-a456-426614174006', 'Acidification Terrestrial', 'Proton Increase in Natural Soils', 'yr×m2×mol/l', '423e4567-e89b-12d3-a456-426614174000', true, 'Soils become more acidic due to an increase in protons (H⁺), depleting nutrients and negatively affecting plants.', '234e4567-e89b-12d3-a456-426614174006', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/cloud-rain-wind.svg'),
    ('123e4567-e89b-12d3-a456-426614174007', 'Eutrophication Freshwater', 'Phosphorus Increase in Fresh Water', 'yr×m3', '423e4567-e89b-12d3-a456-426614174001', true, 'An increase in phosphorus in freshwater, leading to algal blooms and water pollution, harming aquatic animals and plants.', '234e4567-e89b-12d3-a456-426614174007', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/fish-off.svg'),
    ('123e4567-e89b-12d3-a456-426614174008', 'Human Toxicity Carcinogenic', 'Risk Increase of Cancer Disease Incidence', '-', '423e4567-e89b-12d3-a456-426614174006', true, 'Increased risk of cancer due to exposure to toxic substances in the environment.', '234e4567-e89b-12d3-a456-426614174008', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/skull.svg'),
    ('123e4567-e89b-12d3-a456-426614174009', 'Human Toxicity Non-Carcinogenic', 'Risk Increase of Non-cancer Disease Incidence', '-','423e4567-e89b-12d3-a456-426614174006', true, 'An increased risk of non-cancer diseases, such as heart or respiratory diseases, due to environmental pollution.', '234e4567-e89b-12d3-a456-426614174009', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/angry.svg'),
    ('123e4567-e89b-12d3-a456-426614174010', 'Ecotoxicity Terrestrial', 'Hazard Weighted Increase in Natural Soils', 'yr×m2', '423e4567-e89b-12d3-a456-426614174007', true, 'An increase in the amount of toxic substances in soil, potentially harming living organisms in the soil and humans.', '234e4567-e89b-12d3-a456-426614174010', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/tree-pine.svg'),
    ('123e4567-e89b-12d3-a456-426614174011', 'Ecotoxicity Freshwater', 'Hazard Weighted Increase in Fresh Water', 'yr×m3', '423e4567-e89b-12d3-a456-426614174008', true, 'An increase in the amount of toxic substances in freshwater, affecting drinking water sources and aquatic ecosystems.', '234e4567-e89b-12d3-a456-426614174011', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/fish-symbol.svg'),
    ('123e4567-e89b-12d3-a456-426614174012', 'Ecotoxicity Marine', 'Hazard Weighted Increase in Marine Water', 'yr×m3', '423e4567-e89b-12d3-a456-426614174002', true, 'An increase in toxic substances in seawater, affecting marine life and fish stocks.', '234e4567-e89b-12d3-a456-426614174012', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/fish-symbol.svg'),
    ('123e4567-e89b-12d3-a456-426614174013', 'Land Use', 'Occupation and Time Integrated Transformation', 'yr×m3', '423e4567-e89b-12d3-a456-426614174009', true, 'Measurement of land use over an extended period, showing the long-term impact of human activity on land.', '234e4567-e89b-12d3-a456-426614174013', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/droplet.svg'),
    ('123e4567-e89b-12d3-a456-426614174014', 'Water Use', 'Increase of Water Consumed', 'm3', '423e4567-e89b-12d3-a456-426614174010', true, 'An increase in water consumption, assessing the impact of human activities on water resources.', '234e4567-e89b-12d3-a456-426614174014', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/trees.svg'),
    ('123e4567-e89b-12d3-a456-426614174015', 'Resource Scarity Mineral', 'Ore Grade Decrease', 'kg', null, true, 'A decrease in ore quality, meaning more extraction is required to obtain the same amount of minerals as before, harming the environment.', '234e4567-e89b-12d3-a456-426614174015', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/pickaxe.svg'),
    ('123e4567-e89b-12d3-a456-426614174016', 'Resource Scarity Fossil', 'Upper Heating Value', 'MJ', null, true, 'The maximum energy a fuel can provide when fully burned.', '234e4567-e89b-12d3-a456-426614174016', 'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/amphora.svg');

INSERT INTO impact_method_category(id, life_cycle_impact_assessment_method_id, impact_category_id, status)
VALUES
    ('123e4567-e89b-12d3-a456-426614174100', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174000', true),
    ('123e4567-e89b-12d3-a456-426614174101', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174001', true),
    ('123e4567-e89b-12d3-a456-426614174102', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174002', true),
    ('123e4567-e89b-12d3-a456-426614174103', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174003', true),
    ('123e4567-e89b-12d3-a456-426614174104', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174004', true),
    ('123e4567-e89b-12d3-a456-426614174105', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174005', true),
    ('123e4567-e89b-12d3-a456-426614174106', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174006', true),
    ('123e4567-e89b-12d3-a456-426614174107', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174007', true),
    ('123e4567-e89b-12d3-a456-426614174108', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174008', true),
    ('123e4567-e89b-12d3-a456-426614174109', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174009', true),
    ('123e4567-e89b-12d3-a456-426614174110', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174010', true),
    ('123e4567-e89b-12d3-a456-426614174111', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174011', true),
    ('123e4567-e89b-12d3-a456-426614174112', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174012', true),
    ('123e4567-e89b-12d3-a456-426614174113', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174013', true),
    ('123e4567-e89b-12d3-a456-426614174114', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174014', true),
    ('123e4567-e89b-12d3-a456-426614174115', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174015', true),
    ('123e4567-e89b-12d3-a456-426614174116', '923e4567-e89b-12d3-a456-426614174000', '123e4567-e89b-12d3-a456-426614174016', true),
    ('123e4567-e89b-12d3-a456-426614174117', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174000', true),
    ('123e4567-e89b-12d3-a456-426614174118', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174001', true),
    ('123e4567-e89b-12d3-a456-426614174119', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174002', true),
    ('123e4567-e89b-12d3-a456-426614174120', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174003', true),
    ('123e4567-e89b-12d3-a456-426614174121', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174004', true),
    ('123e4567-e89b-12d3-a456-426614174122', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174005', true),
    ('123e4567-e89b-12d3-a456-426614174123', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174006', true),
    ('123e4567-e89b-12d3-a456-426614174124', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174007', true),
    ('123e4567-e89b-12d3-a456-426614174125', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174008', true),
    ('123e4567-e89b-12d3-a456-426614174126', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174009', true),
    ('123e4567-e89b-12d3-a456-426614174127', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174010', true),
    ('123e4567-e89b-12d3-a456-426614174128', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174011', true),
    ('123e4567-e89b-12d3-a456-426614174129', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174012', true),
    ('123e4567-e89b-12d3-a456-426614174130', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174013', true),
    ('123e4567-e89b-12d3-a456-426614174131', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174014', true),
    ('123e4567-e89b-12d3-a456-426614174132', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174015', true),
    ('123e4567-e89b-12d3-a456-426614174133', '923e4567-e89b-12d3-a456-426614174001', '123e4567-e89b-12d3-a456-426614174016', true),
    ('123e4567-e89b-12d3-a456-426614174134', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174000', true),
    ('123e4567-e89b-12d3-a456-426614174135', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174001', true),
    ('123e4567-e89b-12d3-a456-426614174136', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174002', true),
    ('123e4567-e89b-12d3-a456-426614174137', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174003', true),
    ('123e4567-e89b-12d3-a456-426614174138', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174004', true),
    ('123e4567-e89b-12d3-a456-426614174139', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174005', true),
    ('123e4567-e89b-12d3-a456-426614174140', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174006', true),
    ('123e4567-e89b-12d3-a456-426614174141', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174007', true),
    ('123e4567-e89b-12d3-a456-426614174142', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174008', true),
    ('123e4567-e89b-12d3-a456-426614174143', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174009', true),
    ('123e4567-e89b-12d3-a456-426614174144', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174010', true),
    ('123e4567-e89b-12d3-a456-426614174145', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174011', true),
    ('123e4567-e89b-12d3-a456-426614174146', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174012', true),
    ('123e4567-e89b-12d3-a456-426614174147', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174013', true),
    ('123e4567-e89b-12d3-a456-426614174148', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174014', true),
    ('123e4567-e89b-12d3-a456-426614174149', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174015', true),
    ('123e4567-e89b-12d3-a456-426614174150', '923e4567-e89b-12d3-a456-426614174002', '123e4567-e89b-12d3-a456-426614174016', true);
