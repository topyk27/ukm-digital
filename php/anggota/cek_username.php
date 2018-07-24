<?php 

$response = array();
require_once '../db_connect.php';
$db = new DB_CONNECT();

if (isset($_POST['username'])) {
	# code...
	$username = $_POST['username'];
	$sql = "SELECT username from anggota where username = '$username'";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		echo json_encode($response);
	}
}

 ?>