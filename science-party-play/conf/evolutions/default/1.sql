# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answers (
  id                            bigint auto_increment not null,
  text                          varchar(255),
  correct                       tinyint(1) default 0,
  question_id                   bigint,
  constraint pk_answers primary key (id)
);

create table chats (
  id                            bigint auto_increment not null,
  name                          varchar(50),
  constraint pk_chats primary key (id)
);

create table user_has_chats (
  chats_id                      bigint not null,
  users_id                      bigint not null,
  constraint pk_user_has_chats primary key (chats_id,users_id)
);

create table friends (
  id                            bigint auto_increment not null,
  user_send_req_id              bigint,
  user_get_req_id               bigint,
  request                       tinyint(1) default 0,
  date_request                  datetime not null,
  date_friends                  datetime not null,
  constraint pk_friends primary key (id)
);

create table games (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  active_player_id              bigint,
  active_question_id            bigint,
  game_status                   varchar(1),
  topic_id                      bigint,
  date_created                  datetime not null,
  date_updated                  datetime not null,
  constraint ck_games_game_status check (game_status in ('A','P','F')),
  constraint pk_games primary key (id)
);

create table messages (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  chat_id                       bigint,
  text                          varchar(1000),
  seen                          tinyint(1) default 0,
  date_created                  datetime not null,
  constraint pk_messages primary key (id)
);

create table notifications (
  id                            bigint auto_increment not null,
  private_text                  varchar(255),
  public_text                   varchar(255),
  public_available              tinyint(1) default 0,
  private_seen                  tinyint(1) default 0,
  user_id                       bigint,
  date_created                  datetime not null,
  constraint pk_notifications primary key (id)
);

create table perks (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint pk_perks primary key (id)
);

create table perks_per_player (
  id                            bigint auto_increment not null,
  player_id                     bigint,
  perk_per_user_id              bigint,
  used                          tinyint(1) default 0,
  constraint pk_perks_per_player primary key (id)
);

create table perks_per_topic (
  id                            bigint auto_increment not null,
  qr_code                       varchar(32),
  perk_id                       bigint,
  topic_id                      bigint,
  constraint pk_perks_per_topic primary key (id)
);

create table perks_per_user (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  perk_per_topic_id             bigint,
  constraint pk_perks_per_user primary key (id)
);

create table players (
  id                            bigint auto_increment not null,
  field_position                integer,
  player_status                 varchar(1),
  user_id                       bigint,
  game_id                       bigint,
  constraint ck_players_player_status check (player_status in ('A','I','L','F','P','D')),
  constraint pk_players primary key (id)
);

create table questions (
  id                            bigint auto_increment not null,
  text                          varchar(1000),
  difficulty                    integer,
  topic_id                      bigint,
  constraint pk_questions primary key (id)
);

create table topics (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  constraint uq_topics_name unique (name),
  constraint pk_topics primary key (id)
);

create table users (
  id                            bigint auto_increment not null,
  firstname                     varchar(45),
  lastname                      varchar(45),
  birthday                      datetime,
  email                         varchar(60),
  password                      varchar(32),
  author                        tinyint(1) default 0,
  points                        integer,
  locked                        tinyint(1) default 0,
  last_online                   datetime,
  date_created                  datetime not null,
  date_updated                  datetime not null,
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id)
);

alter table answers add constraint fk_answers_question_id foreign key (question_id) references questions (id) on delete restrict on update restrict;
create index ix_answers_question_id on answers (question_id);

alter table user_has_chats add constraint fk_user_has_chats_chats foreign key (chats_id) references chats (id) on delete restrict on update restrict;
create index ix_user_has_chats_chats on user_has_chats (chats_id);

alter table user_has_chats add constraint fk_user_has_chats_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_user_has_chats_users on user_has_chats (users_id);

alter table friends add constraint fk_friends_user_send_req_id foreign key (user_send_req_id) references users (id) on delete restrict on update restrict;
create index ix_friends_user_send_req_id on friends (user_send_req_id);

alter table friends add constraint fk_friends_user_get_req_id foreign key (user_get_req_id) references users (id) on delete restrict on update restrict;
create index ix_friends_user_get_req_id on friends (user_get_req_id);

alter table games add constraint fk_games_active_player_id foreign key (active_player_id) references players (id) on delete restrict on update restrict;
create index ix_games_active_player_id on games (active_player_id);

alter table games add constraint fk_games_active_question_id foreign key (active_question_id) references questions (id) on delete restrict on update restrict;
create index ix_games_active_question_id on games (active_question_id);

alter table games add constraint fk_games_topic_id foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_games_topic_id on games (topic_id);

alter table messages add constraint fk_messages_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_messages_user_id on messages (user_id);

alter table messages add constraint fk_messages_chat_id foreign key (chat_id) references chats (id) on delete restrict on update restrict;
create index ix_messages_chat_id on messages (chat_id);

alter table notifications add constraint fk_notifications_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_notifications_user_id on notifications (user_id);

alter table perks_per_player add constraint fk_perks_per_player_player_id foreign key (player_id) references players (id) on delete restrict on update restrict;
create index ix_perks_per_player_player_id on perks_per_player (player_id);

alter table perks_per_player add constraint fk_perks_per_player_perk_per_user_id foreign key (perk_per_user_id) references perks_per_user (id) on delete restrict on update restrict;
create index ix_perks_per_player_perk_per_user_id on perks_per_player (perk_per_user_id);

alter table perks_per_topic add constraint fk_perks_per_topic_perk_id foreign key (perk_id) references perks (id) on delete restrict on update restrict;
create index ix_perks_per_topic_perk_id on perks_per_topic (perk_id);

alter table perks_per_topic add constraint fk_perks_per_topic_topic_id foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_perks_per_topic_topic_id on perks_per_topic (topic_id);

alter table perks_per_user add constraint fk_perks_per_user_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_perks_per_user_user_id on perks_per_user (user_id);

alter table perks_per_user add constraint fk_perks_per_user_perk_per_topic_id foreign key (perk_per_topic_id) references perks_per_topic (id) on delete restrict on update restrict;
create index ix_perks_per_user_perk_per_topic_id on perks_per_user (perk_per_topic_id);

alter table players add constraint fk_players_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_players_user_id on players (user_id);

alter table players add constraint fk_players_game_id foreign key (game_id) references games (id) on delete restrict on update restrict;
create index ix_players_game_id on players (game_id);

alter table questions add constraint fk_questions_topic_id foreign key (topic_id) references topics (id) on delete restrict on update restrict;
create index ix_questions_topic_id on questions (topic_id);


# --- !Downs

alter table answers drop foreign key fk_answers_question_id;
drop index ix_answers_question_id on answers;

alter table user_has_chats drop foreign key fk_user_has_chats_chats;
drop index ix_user_has_chats_chats on user_has_chats;

alter table user_has_chats drop foreign key fk_user_has_chats_users;
drop index ix_user_has_chats_users on user_has_chats;

alter table friends drop foreign key fk_friends_user_send_req_id;
drop index ix_friends_user_send_req_id on friends;

alter table friends drop foreign key fk_friends_user_get_req_id;
drop index ix_friends_user_get_req_id on friends;

alter table games drop foreign key fk_games_active_player_id;
drop index ix_games_active_player_id on games;

alter table games drop foreign key fk_games_active_question_id;
drop index ix_games_active_question_id on games;

alter table games drop foreign key fk_games_topic_id;
drop index ix_games_topic_id on games;

alter table messages drop foreign key fk_messages_user_id;
drop index ix_messages_user_id on messages;

alter table messages drop foreign key fk_messages_chat_id;
drop index ix_messages_chat_id on messages;

alter table notifications drop foreign key fk_notifications_user_id;
drop index ix_notifications_user_id on notifications;

alter table perks_per_player drop foreign key fk_perks_per_player_player_id;
drop index ix_perks_per_player_player_id on perks_per_player;

alter table perks_per_player drop foreign key fk_perks_per_player_perk_per_user_id;
drop index ix_perks_per_player_perk_per_user_id on perks_per_player;

alter table perks_per_topic drop foreign key fk_perks_per_topic_perk_id;
drop index ix_perks_per_topic_perk_id on perks_per_topic;

alter table perks_per_topic drop foreign key fk_perks_per_topic_topic_id;
drop index ix_perks_per_topic_topic_id on perks_per_topic;

alter table perks_per_user drop foreign key fk_perks_per_user_user_id;
drop index ix_perks_per_user_user_id on perks_per_user;

alter table perks_per_user drop foreign key fk_perks_per_user_perk_per_topic_id;
drop index ix_perks_per_user_perk_per_topic_id on perks_per_user;

alter table players drop foreign key fk_players_user_id;
drop index ix_players_user_id on players;

alter table players drop foreign key fk_players_game_id;
drop index ix_players_game_id on players;

alter table questions drop foreign key fk_questions_topic_id;
drop index ix_questions_topic_id on questions;

drop table if exists answers;

drop table if exists chats;

drop table if exists user_has_chats;

drop table if exists friends;

drop table if exists games;

drop table if exists messages;

drop table if exists notifications;

drop table if exists perks;

drop table if exists perks_per_player;

drop table if exists perks_per_topic;

drop table if exists perks_per_user;

drop table if exists players;

drop table if exists questions;

drop table if exists topics;

drop table if exists users;

