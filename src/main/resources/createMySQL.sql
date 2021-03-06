DROP DATABASE IF EXISTS calculation;
CREATE DATABASE IF NOT EXISTS calculation DEFAULT CHARACTER SET utf8mb4;
USE calculation;

create table estimate
(
    id             bigint not null primary key auto_increment,
    account_id     bigint not null,
    contract       varchar(20),
    building       varchar(255),
    date           date,
    customer       varchar(255) default '',
    position       varchar(50)  default '',
    estimate_price double,
    discount       double,
    price          double,
    create_at datetime,
    last_modified datetime,
    constraint estimate_fk foreign key (account_id) references account (id) on delete cascade on UPDATE cascade
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

create table product
(
    id          bigint not null primary key auto_increment,
    account_id  bigint not null,
    short_name  varchar(50),
    full_name   varchar(255),
    measurement varchar(50),
    price       double,
    create_at datetime,
    last_modified datetime,
    constraint product_fk foreign key (account_id) references account (id) on delete cascade on UPDATE cascade
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

create table calculation
(
    id                bigint       not null primary key auto_increment,
    estimate_id       bigint       not null,
    address           varchar(250) not null,
    calculation_price double,
    create_at datetime,
    last_modified datetime,
    constraint calculation_fk foreign key (estimate_id) references estimate (id) on delete cascade on UPDATE cascade

) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

create table orders
(
    id             bigint not null primary key auto_increment,
    calculation_id bigint,
    short_name     varchar(50),
    full_name      varchar(255),
    measurement    varchar(50),
    price          double,
    count          int,
    summary        double,
    create_at datetime,
    last_modified datetime,
    constraint orders_fk_calculation foreign key (calculation_id) references calculation (id) on delete cascade
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;


create table account
(
    id              bigint       not null primary key auto_increment,
    email           varchar(100) not null,
    password        varchar(255) not null,
    company         varchar(50),
    department      varchar(150),
    address         varchar(150),
    site            varchar(150),
    phone           varchar(50),
    position        varchar(50),
    name            varchar(50),
    image           longblob,
    status          boolean,
    activation_code varchar(255),
       create_at datetime,
    last_modified datetime
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;


create table role
(
    id_account bigint       not null,
    role       varchar(100) not null,
    constraint foreign key (id_account) references account (id) on UPDATE cascade on DELETE cascade
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

insert into account(email, password, company, department, address, site, phone, position, name, status)
VALUES ('1@1', '$2y$10$PhENTDFlRwg5xoigqhZGb.MInDTe39tD2nlhx1FFuT8ehmfgmwA.C', '?????? "???????? & ????????????"',
        '?????????? ???? ???????????? ?? ????????????????',
        '??.??????????????????????, ????. ?????????? 1, ???????? 1', 'www.site.ru', '8 (800) 800-80-80',
        '?????????????????????? ????????????????', '???????????? ??.??.', true);


insert into role (id_account, role)
VALUES (1, 'USER');

insert into product (account_id, short_name, full_name, measurement, price)
VALUES (1, '?????????????????? ??????. ?? ??????.', '?????????????????? ?????????????? ?? ???????????????????????? ??????????', '????.', 130.00),
       (1, '?????????????????? ??????. RJ45', '?????????????????? ???????????????????????? ?????????????? RJ45 (????????????????)', '????.', 210.00),
       (1, '?????????????????? 2?? ??????.', '?????????????????? ???????????????????????????? ??????????????????????', '????.', 230.00),
       (1, '?????????????????? ???????????????????? ??????.', '?????????????????? ???????????????????? ??????????????????????', '????.', 550.00),
       (1, '???????????? ??????????????', '???????????? ??????????????', '????.', 280.00),
       (1, '???????????? ??????????????????????', '???????????? ??????????????????????', '????.', 350.00),
       (1, '?????????????????? ??????. ????????.', '?????????????????? ?????????????????? ??????????????????????', '????.', 180.00),
       (1, '???????????? ??????????', '???????????? ??????????', '????.', 200.00),
       (1, '?????????????????????? ??????. ????????.', '?????????????????????? ?????????????????? ??????????????????????', '????.', 150.00),
       (1, '?????????????????? ??????. ????????.', '?????????????????? ?????????????????????? ??????????????????????', '????.', 150.00),
       (1, '???????????? ???????????????????? ????????.', '???????????? ???????????????????? ??????????????????????', '????.', 500.00),
       (1, '?????????????????? ????????. ??????????????????', '?????????????????? ???????????????????????? ??????????????????', '????.', 550.00),
       (1, '?????????????????? ???????????? ???? 5 ????.', '?????????????????? ???????????????????? ???????????? ???? ???????????? (??????????????????) ???? 5 ????.', '????.', 650.00),
       (1, '?????????????????? ???????????? ???? 5 ????.', '?????????????????? ???????????????????? ???????????? ???? 5 ????.', '????.', 950.00),
       (1, '?????????????????? ???????????? ???? 10 ????.', '?????????????????? ???????????????????? ???????????? ???? 10 ????.', '????.', 2200.00),
       (1, '?????????????????? ?????????? ????????????', '?????????????????? ?????????? ???????????????? ?????? ?????????????????? ????????????', '????.', 200.00),
       (1, '???????????? ?????????????? ????????????', '???????????? ?????????????? ????????????', '????.', 400.00),
       (1, '???????????? ?????????????? ????????????', '???????????? ?????????????? ????????????', '????.', 650.00),
       (1, '???????????????? ??????????????????????', '???????????????? ??????????????????????/????????????', '????.', 180.00),
       (1, '?????????????????? ?????? 2P', '?????????????????? ?????? ??????????????????????????', '????.', 350.00),
       (1, '?????????????????? ?????? 4P', '?????????????????? ?????? ????????????????????????????????', '????.', 450.00),
       (1, '???????????? ??????', '???????????? ??????', '????.', 400.00),
       (1, '???????????? ?????? 1P', '???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)', '????.', 200.00),
       (1, '???????????? ?????? 2P', '???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)', '????.', 392.00),
       (1, '???????????? ?????? 3P', '???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)', '????.', 686.00),
       (1, '???????????? ??????.', '???????????? ?????????????????????????????? ??????????????????????', '????.', 450.00),
       (1, '???????????? ?????????? ???? 6 ??????.', '???????????? ???????????????????????????????????? ?????????? ???? 6 ?????????????? ?? ????????????????????????', '????.', 3150.00),
       (1, '???????????? ?????????? ???? 12 ??????.', '???????????? ???????????????????????????????????? ?????????? ???? 12 ?????????????? ?? ????????????????????????', '????.', 4200.00),
       (1, '???????????? ?????????? ???? 18 ??????.', '???????????? ???????????????????????????????????? ?????????? ???? 18 ?????????????? ?? ????????????????????????', '????.', 6300.00),
       (1, '???????????? ?????????? ???? 24 ??????.', '???????????? ???????????????????????????????????? ?????????? ???? 24 ?????????????? ?? ????????????????????????', '????.', 7980.00),
       (1, '???????????? ?????????? ???? 36 ??????.', '???????????? ???????????????????????????????????? ?????????? ???? 36 ?????????????? ?? ????????????????????????', '????.', 9240.00),
       (1, '???????????? ?????????? ???? 54 ??????.', '???????????? ???????????????????????????????????? ?????????? ???? 54 ?????????????? ?? ????????????????????????', '????.', 11480.00),
       (1, '?????????????????? ????-???? 1??', '?????????????????? ?????????????????????????????? ?????????????????????? ?? ?????????????????? ?????????????????????????????? ????????', '????.',
        1190.00),
       (1, '?????????????????? ????-???? 3??', '?????????????????? ?????????????????????????????? ?????????????????????? ?? ?????????????????? ?????????????????????????????? ????????', '????.',
        1400.00),
       (1, '???????????????? ????-????', '???????????????? ??????????????????????????????', '????.', 490.00),
       (1, '?????????????????? ??????. 3?? ???? 4 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 4 ????. ????', '??????.??.', 80.00),
       (1, '?????????????????? ??????. 3?? ???? 10 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 10 ????. ????', '??????.??.', 100.00),
       (1, '?????????????????? ??????. 5?? ???? 4 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 4 ????. ????', '??????.??.', 80.00),
       (1, '?????????????????? ??????. 5?? ???? 10 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 10 ????. ????', '??????.??.', 120.00),
       (1, '?????????????????? ??????. 5?? ???? 16 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 16 ????. ????', '??????.??.', 120.00),
       (1, '?????????????????? ??????. 5?? ???? 35 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 35 ????. ????', '??????.??.', 140.00),
       (1, '?????????????????? ??????. 5?? ???? 50 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 50 ????. ????', '??????.??.', 140.00),
       (1, '?????????????????? ??????. 5?? ???? 95 ????.????', '?????????????????? ???????????? ???????????????????????????? ???? 95 ????. ????', '??????.??.', 160.00),
       (1, '?????????????????? ??????.5??120????.????', '?????????????????? ???????????? ???????????????????????????? ???? 120 ????. ????', '??????.??.', 180.00),
       (1, '?????????????????? ???????????? ???? ??????????????', '?????????????????? ???????????? ?????? ???????????? ???? ??????????????', '??????.??.', 105.00),
       (1, '?????????????????? ???????????? ???? ????????????', '?????????????????? ???????????? ?????? ???????????? ???? ????????????', '??????.??.', 126.00),
       (1, '?????????????????? ???????????? ?? ??????????', '?????????????????? ???????????? ?? ?????????? ???? ??????????????', '??????.??.', 126.00),
       (1, '?????????????????? ????????????-????????????', '?????????????????? ????????????-????????????', '??????.??.', 80.00),
       (1, '???????????? ???????????????????? ??????????????', '???????????? ???????????????????? ??????????????', '??????.??.', 350.00);

insert into estimate( account_id, contract, building, date, customer, position, estimate_price) VALUES
(1,'255/1','?????????????? ??????????????????',now(),'?????????????? ??????????????????','???????????? ??.??.',2200.0),
(1,'248','???????????????? ??????????',now(),'???????????????? ??????????','???????????? ??.??.',10000.0),
(1,'e-118','?????????????? ?????????????????????????????? ??????????????????????',now(),'?????????????? ?????????????????????????????? ??????????????????????','?????????????? ??.??.',5000.0);
insert into calculation(estimate_id, address, calculation_price) VALUES
(1,'??. ??????????1, ????. ?????????? 1',2200.0),
(2,'??. ??????????2, ????. ?????????? 2',10000.0),
(3,'??. ??????????3, ????. ?????????? 3',5000.0);
insert into orders(calculation_id, short_name, full_name, measurement, price, count, summary) VALUES
(1,'???????????? ?????? 1P','???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)','????.',200.0,5,1000.0),
(1,'???????????? ?????? 3P','???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)','????.',686.0,2,1200.0),
(2,'???????????? ?????? 1P','???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)','????.',200.0,50,10000.0),
(3,'???????????? ?????? 1P','???????????? ?????????????????????????????? ?????????????????????? (??????????????????????????)','????.',200.0,25,5000.0);


# SHIELDS
drop table if exists building;
create table if not exists building
(
    id           bigint primary key auto_increment not null,
    account_id   bigint not null ,
    name         varchar(150) not null,
    project      varchar(50),
    name_create  varchar(50),
    name_control varchar(50),
    date         date,
    create_at datetime,
    last_modified datetime,
    constraint building_fk foreign key (account_id) references account (id) on UPDATE cascade on DELETE cascade
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

drop table if exists shield;
create table if not exists shield
(
    id                  bigint primary key auto_increment not null,
    building_id         bigint                            not null,
    phase               int,
    name_shield         varchar(50),
    name_protect        varchar(50),
    rcd_current         int,
    description_protect varchar(50),
    current_protect     int,
    cable               varchar(50),
    length              int,
    rated_current       double,
    rated_power         double,
    create_at datetime,
    last_modified datetime,
    constraint shield_fk foreign key (building_id) references building (id) on UPDATE cascade on DELETE cascade

) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

drop table if exists part_shield;
create table if not exists part_shield
(
    id                  bigint primary key auto_increment not null,
    shield_id           bigint                            not null,
    phase               int,
    name_protect        varchar(50),
    rcd_current         int,
    description_protect varchar(50),
    current_protect     int,
    name                varchar(100),
    cable               varchar(50),
    length              int,
    rated_current       double,
    usage_rate         double,
    create_at datetime,
    last_modified datetime,
    constraint shield_fk foreign key (shield_id) references shield (id) on UPDATE cascade on DELETE cascade
) ENGINE = myisam
  DEFAULT CHARSET = UTF8MB4;

insert into building(account_id, name, project, name_create, name_control, date) VALUES
(1,'???????????????????????????????? ?????????????????? ??????????????????','11-568-54','???????????? ??.??.','???????????? ??.??.',now());

insert into shield(building_id, phase, name_shield, name_protect, description_protect, current_protect, cable,
                   length, rated_current, rated_power)
VALUES (1, 3, '????-1', '???? 47-29', 'C', 40, '??????????-LS 5??10', 25, 32, 7.04);

insert into part_shield(shield_id, phase, name_protect, description_protect, current_protect, name, cable,
                        rated_current, usage_rate, length)
VALUES (1, 1, '???? 47-29', 'C', 16, '??????????????', '??????????-LS 3??2,5', 5, 0.7, 20),
       (1, 2, '???? 47-29', 'C', 16, '?????????????????????????? ????????????????????????', '??????????-LS 3??2,5', 5, 0.7, 10),
       (1, 3, '???? 47-29', 'C', 16, '??????????????', '??????????-LS 5??2,5', 5, 0.7, 12);

insert into part_shield (shield_id, phase, name_protect, rcd_current, description_protect, current_protect, name, cable,
                         rated_current, usage_rate, length)
VALUES (1, 4, '????????-32', 30, 'C', 16, '??????????????', '??????????-LS 5??2,5', 5, 0.7, 56);

insert into part_shield(shield_id, phase, name_protect, rcd_current, description_protect, current_protect)
VALUES (1, 2, '????????-32', 30, 'C', 16);