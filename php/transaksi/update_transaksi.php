<?php 

require_once '../db_config.php';
$response = array();
if (isset($_POST['id_transaksi']) && isset($_POST['id_barang']) && isset($_POST['jumlah_barang']) && isset($_POST['total'])
	&& isset($_POST['waktu']) && isset($_POST['id_anggota']) && isset($_POST['status']) && isset($_POST['alamat'])) {
	$id_transaksi = $_POST['id_transaksi'];
	$id_barang = $_POST['id_barang'];
	$jumlah_barang = $_POST['jumlah_barang'];
	$total = $_POST['total'];
	$waktu = $_POST['waktu'];
	$id_anggota = $_POST['id_anggota'];
	$status = $_POST['status'];
	$alamat = $_POST['alamat'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("update transaksi set
		id_barang = '$id_barang',
		jumlah_barang = '$jumlah_barang',
		total = '$total',
		waktu = '$waktu',
		id_anggota = '$id_anggota',
		status = '$status',
		alamat = '$alamat'
		where id_transaksi = '$id_transaksi' ");
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

if (isset($_POST['id_transaksi']) && !isset($_POST['id_barang']) && !isset($_POST['jumlah_barang']) && !isset($_POST['total'])
	&& !isset($_POST['waktu']) && !isset($_POST['id_anggota']) && isset($_POST['status']) && isset($_POST['alamat'])) {
	$id_transaksi = $_POST['id_transaksi'];
	// $id_barang = $_POST['id_barang'];
	// $jumlah_barang = $_POST['jumlah_barang'];
	// $total = $_POST['total'];
	// $waktu = $_POST['waktu'];
	// $id_anggota = $_POST['id_anggota'];
	$status = $_POST['status'];
	$alamat = $_POST['alamat'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("update transaksi set
		status = '$status',
		alamat = '$alamat'
		where id_transaksi = '$id_transaksi' ");
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