<?php 

$response = array();
$response['max_ID'] = array();
require_once '../db_connect.php';
$db = new DB_CONNECT();
$sql = "SELECT MAX(id_transaksi) as id_transaksi from transaksi";
$result = mysql_query($sql) or die(mysql_error());
if (mysql_num_rows($result) > 0) {
	# code...
	while ($row = mysql_fetch_array($result)) {
		# code...
		$max = array();
		$max['id_transaksi'] = $row['id_transaksi'];
		array_push($response['max_ID'], $max);
	}
	$response['success'] = 1;
	echo json_encode($response);
}
else{
	$response['success'] = 0;
	echo json_encode($response);
}

 ?>