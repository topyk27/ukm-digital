<?php 

require_once '../db_config.php';



$response = array();
if (isset($_POST['jenis_barang'])) {
	
	$jenis_barang = $_POST['jenis_barang'];
		require_once '../db_connect.php';
$db = new DB_CONNECT();
try {
	
	$result = mysql_query("insert into jenis_barang (jenis_barang)
		values ('$jenis_barang')");
	if ($result) {
		$response["success"] = 1;
		$response["message"] = "successfully created";
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "oops!";
		echo json_encode($response);
	}
} catch (Exception $e) {
	$response['error'] = true;
	$response['message'] = $e->getMessage();
}
	}


 ?>