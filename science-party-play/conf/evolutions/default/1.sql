# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table perks (
  id                        bigint auto_increment not null,
  perk_name                 varchar(255),
  qr_code                   varchar(32),
  constraint pk_perks primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  firstname                 varchar(45),
  lastname                  varchar(45),
  birthday                  datetime(6),
  email                     varchar(60),
  password                  varchar(32),
  points                    integer,
  locked                    tinyint(1) default 0,
  date_created              datetime(6) not null,
  date_updated              datetime(6) not null,
  constraint pk_users primary key (id))
;


create table user_has_perks (
  perks_id                       bigint not null,
  users_id                       bigint not null,
  constraint pk_user_has_perks primary key (perks_id, users_id))
;



alter table user_has_perks add constraint fk_user_has_perks_perks_01 foreign key (perks_id) references perks (id) on delete restrict on update restrict;

alter table user_has_perks add constraint fk_user_has_perks_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table perks;

drop table user_has_perks;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

