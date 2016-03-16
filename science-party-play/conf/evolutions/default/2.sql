# --- !Ups
create fulltext index livesearch_index on users (firstname,lastname,email);

# --- !Downs
drop index livesearch_index on users;



