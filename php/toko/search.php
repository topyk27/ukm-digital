<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = $db::$svr . "/ukm_digital/toko/gambar/";

if (isset($_POST['nama'])) {
	# code...
	$nama = $_POST['nama'];
	// $sql = "SELECT * from toko where nama_toko like '%$nama%' ";
	$sql = "SELECT a.nama,
t.id_toko, t.nama_toko, t.alamat_toko, t.telp, t.deskripsi_toko, t.gambar, t.id_kampung, t.id_anggota, t.lat, t.lng
from anggota as a, toko as t
WHERE a.id_anggota = t.id_anggota &&
t.nama_toko like '%$nama%' ";
	$toko_result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($toko_result) > 0) {
		# code...
		$response['toko'] = array();
		while ($row = mysql_fetch_array($toko_result)) {
			# code...
			$toko = array();
			$toko['nama'] = $row['nama'];
			$toko['id_toko'] = $row['id_toko'];
			$toko["nama_toko"] = $row["nama_toko"];
			$toko["alamat_toko"] = $row["alamat_toko"];
			$toko["telp"] = $row["telp"];
			$toko["deskripsi_toko"] = $row["deskripsi_toko"];
			$toko["gambar"] = $sv.$row["gambar"];
			$toko["id_kampung"] = $row["id_kampung"];
			$toko["id_anggota"] = $row["id_anggota"];
			$toko['lat'] = $row['lat'];
			$toko['lng'] = $row['lng'];
			array_push($response["toko"], $toko);
		}
		$response["success"] = 1;
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "tidak ada toko";
		echo json_encode($response);
	}
}

 ?>