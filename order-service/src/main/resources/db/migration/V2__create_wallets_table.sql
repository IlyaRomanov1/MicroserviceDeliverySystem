CREATE TABLE IF NOT EXISTS wallets
(
    user_id int primary key,
    balance numeric(10, 2) not null default 0 check ( balance >= 0 )
);
