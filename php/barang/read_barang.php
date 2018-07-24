<?php

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
// $sv = "http://192.168.43.192/ukm_digital/barang/gambar/";
$sv = $db::$svr . "/ukm_digital/barang/gambar/";
if (!isset($_POST['id_toko']) && !isset($_POST['id_kampung'])) {
	

$sql = "select * from barang order by id_barang DESC";
$barang_result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($barang_result) > 0) {
	$response["barang"] = array();
	while ($row = mysql_fetch_array($barang_result)) {
		$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		//$barang["kode_barang"]		= $row["kode_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);

	}
	$response["success"] = 1;
	echo json_encode($response);
	
} else{
	$response["success"] = 0;
	$response["message"] = "tidak ada barang";
	echo json_encode($response);
}

}
if (isset($_POST['id_toko']) && !isset($_POST['id_kampung'])) {
	$id_toko = $_POST['id_toko'];
	$sql = "select * from barang where id_toko = '$id_toko' order by id_barang DESC";
$barang_result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($barang_result) > 0) {
	$response["barang"] = array();
	while ($row = mysql_fetch_array($barang_result)) {
		$barang = array();
		$barang["id_barang"]		= $row["id_barang"];
		//$barang["kode_barang"]		= $row["kode_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);

	}
	$response["success"] = 1;
	echo json_encode($response);
} else{
	$response["success"] = 0;
	$response["message"] = "tidak ada barang";
	echo json_encode($response);
}
}

if (!isset($_POST['id_toko']) && isset($_POST['id_kampung'])) {
	$id_kampung = $_POST['id_kampung'];
	$sql = "select b.id_barang, b.nama_barang, b.ukuran, b.merk, b.khusus, b.harga, b.stok, b.deskripsi_barang, b.gambar, b.id_jenis_barang, b.id_toko
from barang as b, toko as t where
b.id_toko = t.id_toko &&
t.id_kampung in (SELECT id_kampung from kampung WHERE id_kampung = '$id_kampung') order by b.id_barang desc";
	$barang_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($barang_result) > 0) {
		$response['barang'] = array();
		while ($row = mysql_fetch_array($barang_result)) {
			$barang = array();
			$barang["id_barang"]		= $row["id_barang"];
		//$barang["kode_barang"]		= $row["kode_barang"];
		$barang["nama_barang"]		= $row["nama_barang"];
		$barang['ukuran'] = $row['ukuran'];
		$barang['merk'] = $row['merk'];
		$barang['khusus'] = $row['khusus'];
		$barang["harga"]			= $row["harga"];
		$barang["stok"]				= $row["stok"];
		$barang["deskripsi_barang"]	= $row["deskripsi_barang"];
		$barang["gambar"]			= $sv.$row["gambar"];
		$barang["id_jenis_barang"]	= $row["id_jenis_barang"];
		$barang["id_toko"]			= $row["id_toko"];
		array_push($response["barang"], $barang);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		$response['message'] = "tidak ada barang";
		echo json_encode($response);
	}
}



  ?>