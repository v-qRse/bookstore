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

delete from Status;
insert into Status (id, description, type) values ('CRTD', 'Создан у пользователя', 'CREATED');
insert into Status (id, description, type) values ('SAVD', 'Сохранен', 'SAVED');
insert into Status (id, description, type) values ('CMPL', 'Заказ завершен', 'COMPLETED');