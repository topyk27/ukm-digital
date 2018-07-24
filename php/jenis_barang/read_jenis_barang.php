<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();

if (!isset($_POST['id_jenis_barang'])) {
	# code...

$sql = "select * from jenis_barang";
$jenis_barang_result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($jenis_barang_result) > 0) {
	$response["jenis_barang"] = array();
	while ($row = mysql_fetch_array($jenis_barang_result)) {
		$jenis_barang = array();
		$jenis_barang["id_jenis_barang"]		= $row["id_jenis_barang"];
		$jenis_barang["jenis_barang"]		= $row["jenis_barang"];
		
		
		array_push($response["jenis_barang"], $jenis_barang);

	}
	$response["success"] = 1;
	echo json_encode($response);
} else{
	$response["success"] = 0;
	$response["message"] = "tidak ada jenis_barang";
	echo json_encode($response);
}
}

if (isset($_POST['id_jenis_barang'])) {
	$id_dari_hp = $_POST['id_jenis_barang'];
	
	$r = array();
	

	$s= "select jenis_barang from jenis_barang where id_jenis_barang = '$id_dari_hp'";
	$nama_jenis = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($nama_jenis) > 0) {
		$r["nama_jenis"] = array();
		while ($row = mysql_fetch_array($nama_jenis)) {
			$nama = array();
			$nama['jenis_barang'] = $row['jenis_barang'];
			array_push($r["nama_jenis"], $nama);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "gak ada";
		echo json_encode($r);
	}
}

 ?>