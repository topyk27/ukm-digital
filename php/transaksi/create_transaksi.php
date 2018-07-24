<?php 

require_once '../db_config.php';



$response = array();


if (isset($_POST['id_transaksi']) && isset($_POST['id_barang']) && isset($_POST['jumlah_barang']) && isset($_POST['total'])
	&& isset($_POST['id_anggota']) && isset($_POST['alamat']) && $_POST['stok']) {
	# code...
	$id_transaksi = $_POST['id_transaksi']; //nanti cek max id dolo di androidnya
	$id_barang = $_POST['id_barang']; //array
	$jumlah_barang = $_POST['jumlah_barang']; //array
	$total = $_POST['total']; //array
	$waktu = date("Y-m-d");
	//ini total transaksi
	$total_transaksi = array_sum($total); //total di tabel transaksi
	echo "total transaksi = " . $total_transaksi;
	//end ini total
	$id_anggota = $_POST['id_anggota'];
	
	$alamat = $_POST['alamat'];
	$stok = $_POST['stok']; //array

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	try {
		$result = mysql_query("INSERT INTO transaksi (id_transaksi, total, waktu, id_anggota, alamat)
		values ('$id_transaksi', '$total_transaksi', '$waktu', '$id_anggota', '$alamat')");
		

		if ($result) {
			# code...
			for ($i=0; $i < count($jumlah_barang) ; $i++) { 
				# code...
				$stok_baru = $stok[$i] - $jumlah_barang[$i];
				$sql = mysql_query("UPDATE barang SET
					stok = '$stok_baru'
					where id_barang = '$id_barang[$i]' ");
				$insert = mysql_query("INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah_barang, total)
				VALUES ('$id_transaksi', '$id_barang[$i]', '$jumlah_barang[$i]', '$total[$i]') ");
		
			}
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
		$response['success'] = 0;
		$response['error'] = true;
		$response['message'] = $e->getMessage();
	}

	
}



 ?>