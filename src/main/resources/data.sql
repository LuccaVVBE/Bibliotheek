INSERT INTO users(username,password,enabled)
VALUES('nameUser', '$2y$10$E.442wS/c9QXLpkLcXaOY.Bet9jTm/aoOUi65yvtuvmJuBJJu1KcG', '1'),('admin', '$2y$10$xT2EKeAP.Ey84iy5dOwuOe5hxtRhvGVk6aLIpgAIpAzzu8xfJWPpO', '1');

INSERT INTO authorities(username,authority) 
VALUES ('nameUser', 'ROLE_USER'),('admin', 'ROLE_ADMIN');