-- insert into subscription_type(subscription_name,description,project_limit,usage_limit,annual_cost,monthly_cost,can_create_organization,status)
-- values ('Basic','Người dùng cá nhân với hạn chế cơ bản',5,1000,0,0,false,true),
--        ('Pro','Người dùng chuyên nghiệp với nhiều tính năng hơn',20,10000,199.99,19.99,true,true),
--        ('Enterprise', 'Doanh nghiệp với khả năng mở rộng tối đa',100,100000,999.99,99.99,true,true);
--
-- insert into user_verify_status(status_name,description,status)
-- values ('Pending','Người dùng đã đăng ký nhưng chưa được xác minh',true),
--        ('Verified','Người dùng đã được xác minh thành công',true),
--        ('Suspended','Người dùng bị tạm ngừng xác minh',true);
--
-- insert into user_status(status_name,description,status)
-- values ('Active','Người dùng đang hoạt động bình thường',true),
--        ('Inactive','Người dùng không hoạt động',true),
--        ('Banned','Người dùng đã bị cấm truy cập hệ thống',true);
--
-- insert into  role(name,status)
-- values ('System Admin',true),
--        ('Company Admin',true),
--        ('Organization Manager',true),
--        ('Organization LCA Staff',true);
--
-- insert into emission_compartment(name,status)
-- values ('Air',true),
--        ('Fresh water',true),
--        ('Marine water',true),
--        ('Agricultural Soil',true),
--        ('Sea water',true),
--        ('PM2.5',true),
--        ('Urban Air',true),
--        ('Industrial Soil',true),
--        ('Industrial Fresh Water',true),
--        ('Annual Crop Land',true),
--        ('Water Consumed',true);
--
-- insert into perspective(name,description,abbr,status)
-- values ('Individualist','','I',true),
--        ('Hierarchist','','H',true),
--        ('Egalitarian','','E',true);
--
-- insert into life_cycle_impact_assessment_method(name,description,version,reference,perspective_id,status)
-- values ('ReCiPe 2016 Midpoint','','v1.03','',1,true),
--        ('ReCiPe 2016 Midpoint','','v1.03','',2,true),
--        ('ReCiPe 2016 Midpoint','','v1.03','',3,true);
--
-- insert into unit_group(name,unit_group_type,status)
-- values ('Unit of Midpoint Impact Category','Impact',true),
--        ('Unit of mass','Normal',true);
--
-- insert into unit(name,unit_group_id,is_default,status,conversion_factor)
-- values ('kg CO2',1,true,true,1),
--        ('kg CFC-11',1,true,true,1),
--        ('kBq Co-60',1,true,true,1),
--        ('kg PM2.5',1,true,true,1),
--        ('kg NOx',1,true,true,1),
--        ('kg SO2',1,true,true,1),
--        ('kg P',1,true,true,1),
--        ('kg 1,4-DCB',1,true,true,1),
--        ('m2×yr',1,true,true,1),
--        ('m3',1,true,true,1),
--        ('kg Cu',1,true,true,1),
--        ('kg oil',1,true,true,1),
--        -- Unit of mass --
--        ('kg',2,true,true,1),
--        ('lb av',2,false,true,0.45),
--        ('cg',2,false,true,0.00001),
--        ('oz t',2,false,true,0.03),
--        ('cwt',2,false,true,45.359237),
--        ('dg',2,false,true,0.0001),
--        ('dr (Av)',2,false,true,0.001771845),
--        ('ng',2,false,true,1e-12),
--        ('pg',2,false,true,1e-15),
--        ('gr',2,false,true,0.0000648),
--        ('t',2,false,true,1000),
--        ('hg',2,false,true,0.1),
--        ('dag',2,false,true,0.01),
--        ('kg SWU',2,false,true,1),
--        ('Mt',2,false,true,1000000000),
--        ('dwt',2,false,true,0.001555174),
--        ('ct',2,false,true,0.0002),
--        ('mg',2,false,true,0.000001),
--        ('ug',2,false,true,1e-9),
--        ('Mg',2,false,true,1000),
--        ('sh tn',2,false,true,907.18),
--        ('g',2,false,true,0.001),
--        ('long tn',2,false,true,1016.04),
--        ('kt',2,false,true,1000000),
--        ('oz av',2,false,true,0.02),
--        -- Unit of factor
--        ('kg CO2-eq',1,true,true,1),
--        ('kg CFC-11-eq',1,true,true,1),
--        ('kBq Co-60-eq',1,true,true,1),
--        ('kg PM2.5-eq',1,true,true,1),
--        ('kg NOx-eq',1,true,true,1),
--        ('kg SO2-eq',1,true,true,1),
--        ('kg P-eq',1,true,true,1),
--        ('kg 1,4-DCB-eq',1,true,true,1),
--        ('m2×yr crop-eq',1,true,true,1),
--        ('m3',1,true,true,1),
--        ('kg Cu-eq',1,true,true,1),
--        ('kg oil-eq',1,true,true,1);
--
-- insert into midpoint_impact_category(name,description,abbr,unit_id,status)
-- values ('Global Warming Potential','','GWP',1,true),
--        ('Ozone Depletion Potential','','ODP',2,true),
--        ('Ionizing Radiation Potential','','IRP',3,true),
--        ('Particulate Matter Formation Potential','','PMFP',4,true),
--        ('Photochemical Oxidant Formation Potential: Ecosystems','','EOFP',5,true),
--        ('Photochemical Oxidant Formation Potential: Humans','','HOFP',5,true),
--        ('Terrestrial Acidification Potential','','TAP',6,true),
--        ('Freshwater Eutrophication Potential','','FEP',7,true),
--        ('Human Toxicity Potential','','HTPc',8,true),
--        ('Human Toxicity Potential','','HTPnc',8,true),
--        ('Terrestrial Ecotoxicity Potential','','TETP',8,true),
--        ('Freshwater Ecotoxicity Potential','','FETP',8,true),
--        ('Marine Ecotoxicity Potential','','METP',8,true),
--        ('Argicultural Land Occupation Potential','','LOP',9,true),
--        ('Water Consumption Potential','','WCP',10,true),
--        ('Surplus Ore Potential','','SOP',11,true),
--        ('Fossil Fuel Potential','','FFP',12,true);
--
-- insert into impact_category(name,indicator,unit,emission_compartment_id,status,indicator_description,midpoint_impact_category_id,icon_url)
-- values ('Climate Change','Infra-red Radiative Forcing Increase','W×yr/m2',1,true,'The increase in atmospheric temperature due to greenhouse gases trapping heat, contributing to global warming.',1,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/thermometer-sun.svg'),
--        ('Ozone Depletion','Stratospheric Ozone Decrease','ppt×yr',1,true,'The depletion of the ozone layer, allowing more ultraviolet (UV) rays from the sun to reach the Earth''s surface, potentially harming humans and living organisms.',2,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/earth.svg'),
--        ('Ionizing Radiation','Absorbed Dose Increase','man×Sv',1,true,'The increase in the amount of harmful substances or radiation absorbed by the human body or environment.',3,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/radiation.svg'),
--        ('Particulate Matter Formation','PM2.5 Population Intake Increase','kg',6,true,'An increase in the number of fine particles PM2.5 inhaled by people, which can cause lung and respiratory problems.',4,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/wind.svg'),
--        ('Photochemical Oxidant Ecosystem','Tropospheric Ozone Increase (AOT40)','ppb.yr',1,true,'The increase in ozone concentration in the troposphere (near the ground), which can harm plants and living organisms.',5,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/cloud-sun-rain.svg'),
--        ('Photochemical Oxidant Human','Tropospheric Ozone Population Intake Increase (M6M)','kg',1,true,'An increase in the amount of ozone inhaled by humans, which can be harmful to health, especially the respiratory system.',6,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/cloud-sun-rain.svg'),
--        ('Acidification Terrestrial','Proton Increase in Natural Soils','yr×m2×mol/l',1,true,'Soils become more acidic due to an increase in protons (H⁺), depleting nutrients and negatively affecting plants.',7,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/cloud-rain-wind.svg'),
--        ('Eutrophication Freshwater','Phosphorus Increase in Fresh Water','yr×m3',2,true,'An increase in phosphorus in freshwater, leading to algal blooms and water pollution, harming aquatic animals and plants.',8,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/fish-off.svg'),
--        ('Human Toxicity Carcinogenic','Risk Increase of Cancer Disease Incidence','-',7,true,'Increased risk of cancer due to exposure to toxic substances in the environment.',9,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/skull.svg'),
--        ('Human Toxicity Non-Carcinogenic','Risk Increase of Non-cancer Disease Incidence','-',7,true,'An increased risk of non-cancer diseases, such as heart or respiratory diseases, due to environmental pollution.',10,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/angry.svg'),
--        ('Ecotoxicity Terrestrial','Hazard Weighted Increase in Natural Soils','yr×m2',8,true,'An increase in the amount of toxic substances in soil, potentially harming living organisms in the soil and humans.',11,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/tree-pine.svg'),
--        ('Ecotoxicity Freshwater','Hazard Weighted Increase in Fresh Water','yr×m3',9,true,'An increase in the amount of toxic substances in freshwater, affecting drinking water sources and aquatic ecosystems.',12,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/fish-symbol.svg'),
--        ('Ecotoxicity Marine','Hazard Weighted Increase in Marine Water','yr×m3',3,true,'An increase in toxic substances in seawater, affecting marine life and fish stocks.',13,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/fish-symbol.svg'),
--        ('Land Use','Occupation and Time Integrated Transformation','yr×m3',10,true,'Measurement of land use over an extended period, showing the long-term impact of human activity on land.',14,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/droplet.svg'),
--        ('Water Use','Increase of Water Consumed','m3',11,true,'An increase in water consumption, assessing the impact of human activities on water resources.',15,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/trees.svg'),
--        ('Resource Scarity Mineral','Ore Grade Decrease','kg',null,true,'A decrease in ore quality, meaning more extraction is required to obtain the same amount of minerals as before, harming the environment.',16,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/pickaxe.svg'),
--        ('Resource Scarity Fossil','Upper Heating Value','MJ',null,true,'The maximum energy a fuel can provide when fully burned.',17,'https://s3.ap-southeast-1.amazonaws.com/cabonerf.icon.storage/amphora.svg');
--
-- insert into impact_method_category(life_cycle_impact_assessment_method_id,impact_category_id,status)
-- values (1,1,true),
--        (1,2,true),
--        (1,3,true),
--        (1,4,true),
--        (1,5,true),
--        (1,6,true),
--        (1,7,true),
--        (1,8,true),
--        (1,9,true),
--        (1,10,true),
--        (1,11,true),
--        (1,12,true),
--        (1,13,true),
--        (1,14,true),
--        (1,15,true),
--        (1,16,true),
--        (1,17,true),
--        (2,1,true),
--        (2,2,true),
--        (2,3,true),
--        (2,4,true),
--        (2,5,true),
--        (2,6,true),
--        (2,7,true),
--        (2,8,true),
--        (2,9,true),
--        (2,10,true),
--        (2,11,true),
--        (2,12,true),
--        (2,13,true),
--        (2,14,true),
--        (2,15,true),
--        (2,16,true),
--        (2,17,true),
--        (3,1,true),
--        (3,2,true),
--        (3,3,true),
--        (3,4,true),
--        (3,5,true),
--        (3,6,true),
--        (3,7,true),
--        (3,8,true),
--        (3,9,true),
--        (3,10,true),
--        (3,11,true),
--        (3,12,true),
--        (3,13,true),
--        (3,14,true),
--        (3,15,true),
--        (3,16,true),
--        (3,17,true);
--
-- insert into exchanges_type(name,status)
-- values ('Product',true),
--        ('Elementary',true);
--
-- insert into life_cycle_stage(name,description,status)
-- values ('Material Acquisition & Pre-Processing','Resource extraction & transport to the production facility gate.',true),
--        ('Production','From input materials to output products of the production facility.',true),
--        ('Distribution & Storage','From the production facility gate to consumer possession.',true),
--        ('Use','From start to finish of consumer possession and use.',true),
--        ('End-of-life','From consumer to recycling or returning to nature.',true),
--        ('Blank','',true);
--
-- insert into users(email, full_name, password, role_id, subscription_id, user_status_id, user_verify_status_id,status)
-- values ('company.admin.test@gmail.com','Organization Manager','$2a$10$DmzY62bQ0jHOcWKg5aAuk.LLUrxHYX.7Y/e/psbjNo10427cZiaxK',3,1,1,2,true),
--         ('system.admin.test@gmail.com','Admin','$2a$10$1mBdYye1JJF49P7nfUy0.u7.oT9a7CsirV07UNaQBwFW7nOf.kplO',1,1,1,2,true),
-- ('manager.test@gmail.com','Manager','$2a$10$f7a6U4tgbd8mw4Z.bYFXIeSVM3vtyJIms9d8sZlcaX42LSQI9Z1Yq',2,1,1,2,true),
-- ('qminh.workmode@gmail.com','Tran Quang Minh','$2a$10$XI2JxRZMsqk1aaNBMzflyunz76laG8wj0NBh6xwNa1IQG5o30wMFu',4,1,1,2,true);
--
-- insert into workspace(name, owner_id,status)
-- values ('Dev',1,true);