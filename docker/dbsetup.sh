#!/bin/bash

/usr/bin/mysqld_safe
mysql -uroot -e "CREATE USER '${username}'@'%' IDENTIFIED BY '${password}'"
mysql -uroot -e "GRANT ALL PRIVILEGES ON '${database}'.* TO '${username}'@'%' WITH GRANT OPTION"
mysqladmin -uroot shutdown

