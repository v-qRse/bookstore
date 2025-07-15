delete from Book;
delete from Books_Order;
delete from Book_On_Order;

delete from Genre;
insert into Genre (id, name, type) values ('NONE', 'Without genre', 'NONE');
insert into Genre (id, name, type) values ('FCTN', 'Fiction', 'FICTION');
insert into Genre (id, name, type) values ('FNTS', 'Fantasy', 'FANTASY');
insert into Genre (id, name, type) values ('HRRR', 'Horror', 'HORROR');
insert into Genre (id, name, type) values ('DICT', 'Dictionary', 'DICTIONARY');
insert into Genre (id, name, type) values ('RMNC', 'Romance', 'ROMANCE');