admin_address:
```
ALTER TABLE admin_address DROP COLUMN  position;
```


coin_balance:
```
alter table coin_balance add recharge_account_balance DECIMAL(20,8); 
```


coin_config:
```
ALTER TABLE coin_config DROP COLUMN  main_account;
ALTER TABLE coin_config DROP COLUMN  main_addr;
ALTER TABLE coin_config add auto_draw_limit DECIMAL(20,8);
ALTER TABLE coin_config add auto_draw INT(11);
```

coin_recharge:
```
ALTER TABLE coin_recharge CHANGE mum amount  DECIMAL(20,8);
ALTER TABLE coin_recharge DROP COLUMN  fee;
ALTER TABLE coin_recharge DROP COLUMN  num;
ALTER TABLE coin_recharge DROP COLUMN  block_number;
```

coin_server:
```
ALTER TABLE coin_server CHANGE wallet_name coin_name  VARCHAR(50);
```

coin_withdraw:
```
ALTER TABLE coin_withdraw DROP COLUMN  wallet_name;
ALTER TABLE coin_withdraw ADD COLUMN  wallet_mark;
```