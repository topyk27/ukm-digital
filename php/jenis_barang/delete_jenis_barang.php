<?php 

$response = array();
if (isset($_POST['id_jenis_barang'])) {
	$id_jenis_barang = $_POST['id_jenis_barang'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("delete from jenis_barang where id_jenis_barang = '$id_jenis_barang' ");
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