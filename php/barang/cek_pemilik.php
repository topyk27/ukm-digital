<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
if (isset($_POST['id_barang'])) {
	$id_barang = $_POST['id_barang'];
	
	$sql = "SELECT t.id_anggota, b.id_barang from toko as t, barang as b
	WHERE b.id_toko = t.id_toko && b.id_barang = '$id_barang'";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		$response['barang'] = array();
		while ($row = mysql_fetch_array($result)) {
			$barang = array();
			$barang['id_anggota'] = $row['id_anggota'];
			array_push($response['barang'], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		echo json_encode($response);
	}
}

 ?>