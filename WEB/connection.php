<?php

define('hostname', 'localhost');
define('user', 'root');
define('password', '654123erto.');
define('databaseName', 'cevapp');


$connect = mysqli_connect(hostname, user, password, databaseName);
$connect->set_charset("utf8")
?>

