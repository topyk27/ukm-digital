<?php 

require_once '../db_config.php';
$response = array();
if (isset($_POST['id_jenis_barang']) && isset($_POST['jenis_barang'])) {
	$id_jenis_barang = $_POST['id_jenis_barang'];
	$jenis_barang = $_POST['jenis_barang'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("update jenis_barang set
		jenis_barang = '$jenis_barang'
		where id_jenis_barang = '$id_jenis_barang' ");
	if ($result) {
		$response["success"] = 1;
			$response["message"] = "successfully created";
			echo json_encode($response);
	}
	else{
		$response["success"] = 0;
			$response["message"] = "Required field(s) is missing";
			echo json_encode($response);
	}
}

 ?>