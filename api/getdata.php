<?php

include_once './controllers/Datasource.php';

$connect = new Datasource();
if (!$connect->connection()) {
	echo "error connecting to databases\n";
}

$ds = $connect->connection();
	
if(empty($_POST["username"])||empty($_POST["password"])){
	echo 'data set is empty';
}

$dataRow = $ds->query("select username,password from users WHERE username='".$_POST["username"]."' AND password='".md5($_POST["password"])."'");

if(empty($dataRow)){
	echo "username password problem";
}
    $data;
    $i = 0;
foreach ($dataRow as $rows) {
    $data[$i++] = array("username" => $rows['username'], "password" => $rows['password']);
}
if(!empty($data)){
header('Content-Type: application/json;charset=utf-8');
echo json_encode($data);
}
//else{
	//echo "Authentication FAILED"
//}
