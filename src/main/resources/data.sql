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
INSERT INTO life_cycle_stage(id, name, description, status,icon_url)
VALUES
    ('823e4567-e89b-12d3-a456-426614174000', 'Raw Material', 'Resource extraction & transport to the production facility gate.', true,'<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pickaxe"><path d="M14.531 12.469 6.619 20.38a1 1 0 1 1-3-3l7.912-7.912"/><path d="M15.686 4.314A12.5 12.5 0 0 0 5.461 2.958 1 1 0 0 0 5.58 4.71a22 22 0 0 1 6.318 3.393"/><path d="M17.7 3.7a1 1 0 0 0-1.4 0l-4.6 4.6a1 1 0 0 0 0 1.4l2.6 2.6a1 1 0 0 0 1.4 0l4.6-4.6a1 1 0 0 0 0-1.4z"/><path d="M19.686 8.314a12.501 12.501 0 0 1 1.356 10.225 1 1 0 0 1-1.751-.119 22 22 0 0 0-3.393-6.319"/></svg>'),
    ('823e4567-e89b-12d3-a456-426614174001', 'Production', 'From input materials to output products of the production facility.', true,'<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-factory"><path d="M2 20a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V8l-7 5V8l-7 5V4a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2Z"/><path d="M17 18h1"/><path d="M12 18h1"/><path d="M7 18h1"/></svg>'),
    ('823e4567-e89b-12d3-a456-426614174002', 'Distribution', 'From the production facility gate to consumer possession.', true,'<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-truck"><path d="M14 18V6a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v11a1 1 0 0 0 1 1h2"/><path d="M15 18H9"/><path d="M19 18h2a1 1 0 0 0 1-1v-3.65a1 1 0 0 0-.22-.624l-3.48-4.35A1 1 0 0 0 17.52 8H14"/><circle cx="17" cy="18" r="2"/><circle cx="7" cy="18" r="2"/></svg>'),
    ('823e4567-e89b-12d3-a456-426614174003', 'Use', 'From start to finish of consumer possession and use.', true,'<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-user-round"><circle cx="12" cy="8" r="5"/><path d="M20 21a8 8 0 0 0-16 0"/></svg>'),
    ('823e4567-e89b-12d3-a456-426614174004', 'End-of-life', 'From consumer to recycling or returning to nature.', true,'<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-recycle"><path d="M7 19H4.815a1.83 1.83 0 0 1-1.57-.881 1.785 1.785 0 0 1-.004-1.784L7.196 9.5"/><path d="M11 19h8.203a1.83 1.83 0 0 0 1.556-.89 1.784 1.784 0 0 0 0-1.775l-1.226-2.12"/><path d="m14 16-3 3 3 3"/><path d="M8.293 13.596 7.196 9.5 3.1 10.598"/><path d="m9.344 5.811 1.093-1.892A1.83 1.83 0 0 1 11.985 3a1.784 1.784 0 0 1 1.546.888l3.943 6.843"/><path d="m13.378 9.633 4.096 1.098 1.097-4.096"/></svg>');

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
    ('723e4567-e89b-12d3-a456-426614174000', 'kg CO2-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174001', 'kg CFC-11-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174002', 'kBq Co-60-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174003', 'kg PM2.5-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174004', 'kg NOx-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174005', 'kg SO2-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174006', 'kg P-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174007', 'kg 1,4-DCB-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174008', 'm2×yr crop-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174009', 'm3', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174010', 'kg Cu-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
    ('723e4567-e89b-12d3-a456-426614174011', 'kg oil-Eq', '623e4567-e89b-12d3-a456-426614174000', true, true, 1),
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
    ('723e4567-e89b-12d3-a456-426614174036', 'oz av', '623e4567-e89b-12d3-a456-426614174001', false, true, 0.02);

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
    ('123e4567-e89b-12d3-a456-426614174000', 'Climate Change', 'Infra-red Radiative Forcing Increase', 'W×yr/m2', '423e4567-e89b-12d3-a456-426614174000', true, 'The increase in atmospheric temperature due to greenhouse gases trapping heat, contributing to global warming.', '234e4567-e89b-12d3-a456-426614174000', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-thermometer-sun"><path d="M12 9a4 4 0 0 0-2 7.5"/><path d="M12 3v2"/><path d="m6.6 18.4-1.4 1.4"/><path d="M20 4v10.54a4 4 0 1 1-4 0V4a2 2 0 0 1 4 0Z"/><path d="M4 13H2"/><path d="M6.34 7.34 4.93 5.93"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174001', 'Ozone Depletion', 'Stratospheric Ozone Decrease', 'ppt×yr', '423e4567-e89b-12d3-a456-426614174000', true, 'The depletion of the ozone layer, allowing more ultraviolet (UV) rays from the sun to reach the Earth''s surface, potentially harming humans and living organisms.', '234e4567-e89b-12d3-a456-426614174001', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-earth"><path d="M21.54 15H17a2 2 0 0 0-2 2v4.54"/><path d="M7 3.34V5a3 3 0 0 0 3 3a2 2 0 0 1 2 2c0 1.1.9 2 2 2a2 2 0 0 0 2-2c0-1.1.9-2 2-2h3.17"/><path d="M11 21.95V18a2 2 0 0 0-2-2a2 2 0 0 1-2-2v-1a2 2 0 0 0-2-2H2.05"/><circle cx="12" cy="12" r="10"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174002', 'Ionizing Radiation', 'Absorbed Dose Increase', 'man×Sv', '423e4567-e89b-12d3-a456-426614174000', true, 'The increase in the amount of harmful substances or radiation absorbed by the human body or environment.', '234e4567-e89b-12d3-a456-426614174002', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-radiation"><path d="M12 12h.01"/><path d="M7.5 4.2c-.3-.5-.9-.7-1.3-.4C3.9 5.5 2.3 8.1 2 11c-.1.5.4 1 1 1h5c0-1.5.8-2.8 2-3.4-1.1-1.9-2-3.5-2.5-4.4z"/><path d="M21 12c.6 0 1-.4 1-1-.3-2.9-1.8-5.5-4.1-7.1-.4-.3-1.1-.2-1.3.3-.6.9-1.5 2.5-2.6 4.3 1.2.7 2 2 2 3.5h5z"/><path d="M7.5 19.8c-.3.5-.1 1.1.4 1.3 2.6 1.2 5.6 1.2 8.2 0 .5-.2.7-.8.4-1.3-.5-.9-1.4-2.5-2.5-4.3-1.2.7-2.8.7-4 0-1.1 1.8-2 3.4-2.5 4.3z"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174003', 'Particulate Matter Formation', 'PM2.5 Population Intake Increase', 'kg', '423e4567-e89b-12d3-a456-426614174005', true, 'An increase in the number of fine particles PM2.5 inhaled by people, which can cause lung and respiratory problems.', '234e4567-e89b-12d3-a456-426614174003', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-wind"><path d="M17.7 7.7a2.5 2.5 0 1 1 1.8 4.3H2"/><path d="M9.6 4.6A2 2 0 1 1 11 8H2"/><path d="M12.6 19.4A2 2 0 1 0 14 16H2"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174004', 'Photochemical Oxidant Ecosystem', 'Tropospheric Ozone Increase (AOT40)', 'ppb.yr', '423e4567-e89b-12d3-a456-426614174000', true, 'The increase in ozone concentration in the troposphere (near the ground), which can harm plants and living organisms.', '234e4567-e89b-12d3-a456-426614174004', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-cloud-sun-rain"><path d="M12 2v2"/><path d="m4.93 4.93 1.41 1.41"/><path d="M20 12h2"/><path d="m19.07 4.93-1.41 1.41"/><path d="M15.947 12.65a4 4 0 0 0-5.925-4.128"/><path d="M3 20a5 5 0 1 1 8.9-4H13a3 3 0 0 1 2 5.24"/><path d="M11 20v2"/><path d="M7 19v2"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174005', 'Photochemical Oxidant Human', 'Tropospheric Ozone Population Intake Increase (M6M)', 'kg', '423e4567-e89b-12d3-a456-426614174000', true, 'An increase in the amount of ozone inhaled by humans, which can be harmful to health, especially the respiratory system.', '234e4567-e89b-12d3-a456-426614174005', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-cloud-sun-rain"><path d="M12 2v2"/><path d="m4.93 4.93 1.41 1.41"/><path d="M20 12h2"/><path d="m19.07 4.93-1.41 1.41"/><path d="M15.947 12.65a4 4 0 0 0-5.925-4.128"/><path d="M3 20a5 5 0 1 1 8.9-4H13a3 3 0 0 1 2 5.24"/><path d="M11 20v2"/><path d="M7 19v2"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174006', 'Acidification Terrestrial', 'Proton Increase in Natural Soils', 'yr×m2×mol/l', '423e4567-e89b-12d3-a456-426614174000', true, 'Soils become more acidic due to an increase in protons (H⁺), depleting nutrients and negatively affecting plants.', '234e4567-e89b-12d3-a456-426614174006', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-cloud-rain-wind"><path d="M4 14.899A7 7 0 1 1 15.71 8h1.79a4.5 4.5 0 0 1 2.5 8.242"/><path d="m9.2 22 3-7"/><path d="m9 13-3 7"/><path d="m17 13-3 7"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174007', 'Eutrophication Freshwater', 'Phosphorus Increase in Fresh Water', 'yr×m3', '423e4567-e89b-12d3-a456-426614174001', true, 'An increase in phosphorus in freshwater, leading to algal blooms and water pollution, harming aquatic animals and plants.', '234e4567-e89b-12d3-a456-426614174007', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-fish-off"><path d="M18 12.47v.03m0-.5v.47m-.475 5.056A6.744 6.744 0 0 1 15 18c-3.56 0-7.56-2.53-8.5-6 .348-1.28 1.114-2.433 2.121-3.38m3.444-2.088A8.802 8.802 0 0 1 15 6c3.56 0 6.06 2.54 7 6-.309 1.14-.786 2.177-1.413 3.058"/><path d="M7 10.67C7 8 5.58 5.97 2.73 5.5c-1 1.5-1 5 .23 6.5-1.24 1.5-1.24 5-.23 6.5C5.58 18.03 7 16 7 13.33m7.48-4.372A9.77 9.77 0 0 1 16 6.07m0 11.86a9.77 9.77 0 0 1-1.728-3.618"/><path d="m16.01 17.93-.23 1.4A2 2 0 0 1 13.8 21H9.5a5.96 5.96 0 0 0 1.49-3.98M8.53 3h5.27a2 2 0 0 1 1.98 1.67l.23 1.4M2 2l20 20"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174008', 'Human Toxicity Carcinogenic', 'Risk Increase of Cancer Disease Incidence', '-', '423e4567-e89b-12d3-a456-426614174006', true, 'Increased risk of cancer due to exposure to toxic substances in the environment.', '234e4567-e89b-12d3-a456-426614174008', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-skull"><path d="m12.5 17-.5-1-.5 1h1z"/><path d="M15 22a1 1 0 0 0 1-1v-1a2 2 0 0 0 1.56-3.25 8 8 0 1 0-11.12 0A2 2 0 0 0 8 20v1a1 1 0 0 0 1 1z"/><circle cx="15" cy="12" r="1"/><circle cx="9" cy="12" r="1"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174009', 'Human Toxicity Non-Carcinogenic', 'Risk Increase of Non-cancer Disease Incidence', '-','423e4567-e89b-12d3-a456-426614174006', true, 'An increased risk of non-cancer diseases, such as heart or respiratory diseases, due to environmental pollution.', '234e4567-e89b-12d3-a456-426614174009', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-angry"><circle cx="12" cy="12" r="10"/><path d="M16 16s-1.5-2-4-2-4 2-4 2"/><path d="M7.5 8 10 9"/><path d="m14 9 2.5-1"/><path d="M9 10h.01"/><path d="M15 10h.01"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174010', 'Ecotoxicity Terrestrial', 'Hazard Weighted Increase in Natural Soils', 'yr×m2', '423e4567-e89b-12d3-a456-426614174007', true, 'An increase in the amount of toxic substances in soil, potentially harming living organisms in the soil and humans.', '234e4567-e89b-12d3-a456-426614174010', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-trees"><path d="M10 10v.2A3 3 0 0 1 8.9 16H5a3 3 0 0 1-1-5.8V10a3 3 0 0 1 6 0Z"/><path d="M7 16v6"/><path d="M13 19v3"/><path d="M12 19h8.3a1 1 0 0 0 .7-1.7L18 14h.3a1 1 0 0 0 .7-1.7L16 9h.2a1 1 0 0 0 .8-1.7L13 3l-1.4 1.5"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174011', 'Ecotoxicity Freshwater', 'Hazard Weighted Increase in Fresh Water', 'yr×m3', '423e4567-e89b-12d3-a456-426614174008', true, 'An increase in the amount of toxic substances in freshwater, affecting drinking water sources and aquatic ecosystems.', '234e4567-e89b-12d3-a456-426614174011', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-fish-symbol"><path d="M2 16s9-15 20-4C11 23 2 8 2 8"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174012', 'Ecotoxicity Marine', 'Hazard Weighted Increase in Marine Water', 'yr×m3', '423e4567-e89b-12d3-a456-426614174002', true, 'An increase in toxic substances in seawater, affecting marine life and fish stocks.', '234e4567-e89b-12d3-a456-426614174012', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-fish-symbol"><path d="M2 16s9-15 20-4C11 23 2 8 2 8"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174013', 'Land Use', 'Occupation and Time Integrated Transformation', 'yr×m3', '423e4567-e89b-12d3-a456-426614174009', true, 'Measurement of land use over an extended period, showing the long-term impact of human activity on land.', '234e4567-e89b-12d3-a456-426614174013', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-tent-tree"><circle cx="4" cy="4" r="2"/><path d="m14 5 3-3 3 3"/><path d="m14 10 3-3 3 3"/><path d="M17 14V2"/><path d="M17 14H7l-5 8h20Z"/><path d="M8 14v8"/><path d="m9 14 5 8"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174014', 'Water Use', 'Increase of Water Consumed', 'm3', '423e4567-e89b-12d3-a456-426614174010', true, 'An increase in water consumption, assessing the impact of human activities on water resources.', '234e4567-e89b-12d3-a456-426614174014', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-droplets"><path d="M7 16.3c2.2 0 4-1.83 4-4.05 0-1.16-.57-2.26-1.71-3.19S7.29 6.75 7 5.3c-.29 1.45-1.14 2.84-2.29 3.76S3 11.1 3 12.25c0 2.22 1.8 4.05 4 4.05z"/><path d="M12.56 6.6A10.97 10.97 0 0 0 14 3.02c.5 2.5 2 4.9 4 6.5s3 3.5 3 5.5a6.98 6.98 0 0 1-11.91 4.97"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174015', 'Resource Scarity Mineral', 'Ore Grade Decrease', 'kg', null, true, 'A decrease in ore quality, meaning more extraction is required to obtain the same amount of minerals as before, harming the environment.', '234e4567-e89b-12d3-a456-426614174015', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-pickaxe"><path d="M14.531 12.469 6.619 20.38a1 1 0 1 1-3-3l7.912-7.912"/><path d="M15.686 4.314A12.5 12.5 0 0 0 5.461 2.958 1 1 0 0 0 5.58 4.71a22 22 0 0 1 6.318 3.393"/><path d="M17.7 3.7a1 1 0 0 0-1.4 0l-4.6 4.6a1 1 0 0 0 0 1.4l2.6 2.6a1 1 0 0 0 1.4 0l4.6-4.6a1 1 0 0 0 0-1.4z"/><path d="M19.686 8.314a12.501 12.501 0 0 1 1.356 10.225 1 1 0 0 1-1.751-.119 22 22 0 0 0-3.393-6.319"/></svg>'),
    ('123e4567-e89b-12d3-a456-426614174016', 'Resource Scarity Fossil', 'Upper Heating Value', 'MJ', null, true, 'The maximum energy a fuel can provide when fully burned.', '234e4567-e89b-12d3-a456-426614174016', '<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-flask-round"><path d="M10 2v7.31"/><path d="M14 9.3V1.99"/><path d="M8.5 2h7"/><path d="M14 9.3a6.5 6.5 0 1 1-4 0"/><path d="M5.52 16h12.96"/></svg>');

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
