-- auto-generated definition
create table organization
(
    org_id      varchar(64) not null comment '组织id'
        primary key,
    main_locale varchar(16) null
);

INSERT INTO `i18n-demo`.organization (org_id, main_locale) VALUES ('ORG_01', 'zh-CN');
INSERT INTO `i18n-demo`.organization (org_id, main_locale) VALUES ('ORG_03', 'en-US');

-- auto-generated definition
create table business_cat
(
    id          bigint auto_increment comment '业务唯一标识'
        primary key,
    org_id      varchar(64)                         not null comment '所属租户ID（与国际化表租户体系一致）',
    name        varchar(128)                        not null comment '业务名称',
    description text                                null comment '业务简介',
    deleted     int       default 0                 null comment '1:已删除 0:正常',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    row_format = DYNAMIC;

INSERT INTO `i18n-demo`.business_cat (id, org_id, name, description, deleted, create_time, update_time) VALUES (1, 'ORG_01', '小猫1默认值(ORG_01)', '第一只小猫(ORG_01)', 0, '2025-03-03 11:05:43', '2025-03-03 11:05:58');
INSERT INTO `i18n-demo`.business_cat (id, org_id, name, description, deleted, create_time, update_time) VALUES (2, 'ORG_01', '小猫2默认值(ORG_01)', '第二只小猫(ORG_01)', 0, '2025-03-03 11:05:43', '2025-03-03 11:05:58');
INSERT INTO `i18n-demo`.business_cat (id, org_id, name, description, deleted, create_time, update_time) VALUES (3, 'ORG_01', '小猫3默认值(ORG_01)', '第三只小猫(ORG_01)', 0, '2025-03-03 11:05:43', '2025-03-03 11:05:58');
INSERT INTO `i18n-demo`.business_cat (id, org_id, name, description, deleted, create_time, update_time) VALUES (4, 'ORG_02', '小猫1默认值(ORG_02)', '第一只小猫(ORG_02)', 0, '2025-03-03 11:05:43', '2025-03-03 11:05:58');
