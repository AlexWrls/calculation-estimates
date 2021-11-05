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
VALUES ('1@1', '$2y$10$PhENTDFlRwg5xoigqhZGb.MInDTe39tD2nlhx1FFuT8ehmfgmwA.C', 'ООО "Рога & копыта"',
        'Отдел по борьбе с котиками',
        'г.Новосибирск, ул. Улица 1, офис 1', 'www.site.ru', '8 (800) 800-80-80',
        'Генеральный директор', 'Иванов И.И.', true);


insert into role (id_account, role)
VALUES (1, 'USER');

insert into product (account_id, short_name, full_name, measurement, price)
VALUES (1, 'Установка роз. и вык.', 'Установка розеток и выключателей света', 'шт.', 130.00),
       (1, 'Установка роз. RJ45', 'Установка компьютерной розетки RJ45 (интернет)', 'шт.', 210.00),
       (1, 'Установка 2к вык.', 'Установка двухклавишного выключателя', 'шт.', 230.00),
       (1, 'Установка проходного вык.', 'Установка проходного выключателя', 'шт.', 550.00),
       (1, 'Ремонт розетки', 'Ремонт розетки', 'шт.', 280.00),
       (1, 'Ремонт выключателя', 'Ремонт выключателя', 'шт.', 350.00),
       (1, 'Установка точ. свет.', 'Установка точечного светильника', 'шт.', 180.00),
       (1, 'Монтаж спота', 'Монтаж спота', 'шт.', 200.00),
       (1, 'Подключение точ. свет.', 'Подключение точечного светильника', 'шт.', 150.00),
       (1, 'Установка пот. свет.', 'Установка потолочного светильника', 'шт.', 150.00),
       (1, 'Монтаж растрового свет.', 'Монтаж растрового светильника', 'шт.', 500.00),
       (1, 'Установка свет. Армстронг', 'Установка светильников Армстронг', 'шт.', 550.00),
       (1, 'Установка люстры до 5 кг.', 'Установка потолочной люстры на крючке (подвесной) до 5 кг.', 'шт.', 650.00),
       (1, 'Установка люстры от 5 кг.', 'Установка потолочной люстры от 5 кг.', 'шт.', 950.00),
       (1, 'Установка люстры от 10 кг.', 'Установка потолочной люстры от 10 кг.', 'шт.', 2200.00),
       (1, 'Установка крюка люстры', 'Установка крюка простого для подвесной люстры', 'шт.', 200.00),
       (1, 'Сборка простой люстры', 'Сборка простой люстры', 'шт.', 400.00),
       (1, 'Сборка сложной люстры', 'Сборка сложной люстры', 'шт.', 650.00),
       (1, 'Демонтаж светильника', 'Демонтаж светильника/люстры', 'шт.', 180.00),
       (1, 'Установка УЗО 2P', 'Установка УЗО двухполюсного', 'шт.', 350.00),
       (1, 'Установка УЗО 4P', 'Установка УЗО четырехполюсного', 'шт.', 450.00),
       (1, 'Замена УЗО', 'Замена УЗО', 'шт.', 400.00),
       (1, 'Монтаж авт 1P', 'Монтаж автоматического выключателя (однополюсного)', 'шт.', 200.00),
       (1, 'Монтаж авт 2P', 'Монтаж автоматического выключателя (двухполюсного)', 'шт.', 392.00),
       (1, 'Монтаж авт 3P', 'Монтаж автоматического выключателя (трехполюсного)', 'шт.', 686.00),
       (1, 'Замена авт.', 'Замена автоматического выключателя', 'шт.', 450.00),
       (1, 'Монтаж щитка на 6 мод.', 'Монтаж распределительного щитка на 6 модулей с раскрючением', 'шт.', 3150.00),
       (1, 'Монтаж щитка на 12 мод.', 'Монтаж распределительного щитка на 12 модулей с раскрючением', 'шт.', 4200.00),
       (1, 'Монтаж щитка на 18 мод.', 'Монтаж распределительного щитка на 18 модулей с раскрючением', 'шт.', 6300.00),
       (1, 'Монтаж щитка на 24 мод.', 'Монтаж распределительного щитка на 24 модулей с раскрючением', 'шт.', 7980.00),
       (1, 'Монтаж щитка на 36 мод.', 'Монтаж распределительного щитка на 36 модулей с раскрючением', 'шт.', 9240.00),
       (1, 'Монтаж щитка на 54 мод.', 'Монтаж распределительного щитка на 54 модулей с раскрючением', 'шт.', 11480.00),
       (1, 'Установка эл-сч 1Р', 'Установка электросчетчика однофазного с встроеным трансформатором тока', 'шт.',
        1190.00),
       (1, 'Установка эл-сч 3Р', 'Установка электросчетчика трехфазного с встроеным трансформатором тока', 'шт.',
        1400.00),
       (1, 'Демонтаж эл-сч', 'Демонтаж электросчетчика', 'шт.', 490.00),
       (1, 'Прокладка каб. 3ж до 4 кв.мм', 'Прокладка кабеля трехпроводного до 4 кв. мм', 'пог.м.', 80.00),
       (1, 'Прокладка каб. 3ж до 10 кв.мм', 'Прокладка кабеля трехпроводного до 10 кв. мм', 'пог.м.', 100.00),
       (1, 'Прокладка каб. 5ж до 4 кв.мм', 'Прокладка кабеля пятипроводного до 4 кв. мм', 'пог.м.', 80.00),
       (1, 'Прокладка каб. 5ж до 10 кв.мм', 'Прокладка кабеля пятипроводного до 10 кв. мм', 'пог.м.', 120.00),
       (1, 'Прокладка каб. 5ж до 16 кв.мм', 'Прокладка кабеля пятипроводного до 16 кв. мм', 'пог.м.', 120.00),
       (1, 'Прокладка каб. 5ж до 35 кв.мм', 'Прокладка кабеля пятипроводного до 35 кв. мм', 'пог.м.', 140.00),
       (1, 'Прокладка каб. 5ж до 50 кв.мм', 'Прокладка кабеля пятипроводного до 50 кв. мм', 'пог.м.', 140.00),
       (1, 'Прокладка каб. 5ж до 95 кв.мм', 'Прокладка кабеля пятипроводного до 95 кв. мм', 'пог.м.', 160.00),
       (1, 'Прокладка каб.5ж120кв.мм', 'Прокладка кабеля пятипроводного до 120 кв. мм', 'пог.м.', 180.00),
       (1, 'Прокладка короба по кирпичу', 'Прокладка короба под кабель по кирпичу', 'пог.м.', 105.00),
       (1, 'Прокладка короба по бетону', 'Прокладка короба под кабель по бетону', 'пог.м.', 126.00),
       (1, 'Прокладка кабеля в гофре', 'Прокладка кабеля в гофре на клипсах', 'пог.м.', 126.00),
       (1, 'Прокладка кабель-канала', 'Прокладка кабель-канала', 'пог.м.', 80.00),
       (1, 'Монтаж распаечных коробок', 'Монтаж распаечных коробок', 'пог.м.', 350.00);

insert into estimate( account_id, contract, building, date, customer, position, estimate_price) VALUES
(1,'255/1','Магазин универсам',now(),'Магазин универсам','Иванов И.И.',2200.0),
(1,'248','Торговый центр',now(),'Торговый центр','Петров П.П.',10000.0),
(1,'e-118','Пищевое производственое предприятие',now(),'Пищевое производственое предприятие','Борисов Б.Б.',5000.0);
insert into calculation(estimate_id, address, calculation_price) VALUES
(1,'г. Город1, ул. Улица 1',2200.0),
(2,'г. Город2, ул. Улица 2',10000.0),
(3,'г. Город3, ул. Улица 3',5000.0);
insert into orders(calculation_id, short_name, full_name, measurement, price, count, summary) VALUES
(1,'Монтаж авт 1P','Монтаж автоматического выключателя (однополюсного)','шт.',200.0,5,1000.0),
(1,'Монтаж авт 3P','Монтаж автоматического выключателя (трехполюсного)','шт.',686.0,2,1200.0),
(2,'Монтаж авт 1P','Монтаж автоматического выключателя (однополюсного)','шт.',200.0,50,10000.0),
(3,'Монтаж авт 1P','Монтаж автоматического выключателя (однополюсного)','шт.',200.0,25,5000.0);


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
(1,'Электроустановка торгового помещения','11-568-54','Иванов И.И.','Петров И.И.',now());

insert into shield(building_id, phase, name_shield, name_protect, description_protect, current_protect, cable,
                   length, rated_current, rated_power)
VALUES (1, 3, 'ЩС-1', 'ВА 47-29', 'C', 40, 'ВВГнг-LS 5х10', 25, 32, 7.04);

insert into part_shield(shield_id, phase, name_protect, description_protect, current_protect, name, cable,
                        rated_current, usage_rate, length)
VALUES (1, 1, 'ВА 47-29', 'C', 16, 'Розетки', 'ВВГнг-LS 3х2,5', 5, 0.7, 20),
       (1, 2, 'ВА 47-29', 'C', 16, 'Бактерицидный рециркулятор', 'ВВГнг-LS 3х2,5', 5, 0.7, 10),
       (1, 3, 'ВА 47-29', 'C', 16, 'Розетки', 'ВВГнг-LS 5х2,5', 5, 0.7, 12);

insert into part_shield (shield_id, phase, name_protect, rcd_current, description_protect, current_protect, name, cable,
                         rated_current, usage_rate, length)
VALUES (1, 4, 'АВДТ-32', 30, 'C', 16, 'Розетки', 'ВВГнг-LS 5х2,5', 5, 0.7, 56);

insert into part_shield(shield_id, phase, name_protect, rcd_current, description_protect, current_protect)
VALUES (1, 2, 'АВДТ-32', 30, 'C', 16);