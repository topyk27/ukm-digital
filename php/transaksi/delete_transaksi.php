<?php 

$response = array();
if (isset($_POST['id_transaksi'])) {
	$id_transaksi = $_POST['id_transaksi'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("delete from transaksi where id_transaksi = '$id_transaksi' ");
	if (mysql_affected_rows() > 0) {
		$response["success"] = 1;
		$response["message"] = "successfully deleted";
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "no found";
		echo json_encode($response);
	}
}
else{
	$response["success"] = 0;
	$response["message"] = "Required fields is missing";
	echo json_encode($response);
}

 ?>