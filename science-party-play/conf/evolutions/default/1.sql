# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table users;

SET FOREIGN_KEY_CHECKS=1;

