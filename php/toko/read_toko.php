<?php

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = $db::$svr . "/ukm_digital/toko/gambar/";

if (!isset($_POST['id_anggota']) && !isset($_POST['id_toko']) && !isset($_POST['id_kampung'])) {
	# code...

// $sql = "select * from toko";
$sql = "SELECT a.nama,
t.id_toko, t.nama_toko, t.alamat_toko, t.telp, t.deskripsi_toko, t.gambar, t.id_kampung, t.id_anggota, t.lat, t.lng
from anggota as a, toko as t
WHERE a.id_anggota = t.id_anggota";
$toko_result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($toko_result) > 0) {
	$response["toko"] = array();
	while ($row = mysql_fetch_array($toko_result)) {
		$toko = array();
		$toko['nama'] = $row['nama'];
		$toko["id_toko"]		= $row["id_toko"];
		$toko["nama_toko"]		= $row["nama_toko"];
		$toko["alamat_toko"]	= $row["alamat_toko"];
		$toko["telp"]			= $row["telp"];
		$toko["deskripsi_toko"]	= $row["deskripsi_toko"];
		$toko["gambar"]			= $sv.$row["gambar"];
		$toko["id_kampung"]		= $row["id_kampung"];
		$toko["id_anggota"]		= $row["id_anggota"];
		$toko['lat'] = $row['lat'];
		$toko['lng'] = $row['lng'];
		array_push($response["toko"], $toko);

	}
	$response["success"] = 1;
	echo json_encode($response);
} else{
	$response["success"] = 0;
	$response["message"] = "tidak ada toko";
	echo json_encode($response);
}
}

if (isset($_POST['id_anggota'])) {
	$id_anggota = $_POST['id_anggota'];
	// $sql = "select id_toko from toko where id_anggota = '$id_anggota'";
	$sql = "select * from toko where id_anggota = '$id_anggota'";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
	$response["toko"] = array();
	while ($row = mysql_fetch_array($result)) {
		$toko = array();
		$toko["id_toko"]		= $row["id_toko"];
		$toko["nama_toko"]		= $row["nama_toko"];
		$toko["alamat_toko"]	= $row["alamat_toko"];
		$toko["telp"]			= $row["telp"];
		$toko["deskripsi_toko"]	= $row["deskripsi_toko"];
		$toko["gambar"]			= $sv.$row["gambar"];
		$toko["id_kampung"]		= $row["id_kampung"];
		$toko["id_anggota"]		= $row["id_anggota"];
		$toko['lat'] = $row['lat'];
		$toko['lng'] = $row['lng'];
		array_push($response["toko"], $toko);

	}
		$response['success'] = 1;
		echo json_encode($response);

	}
	else{
		$response['success'] = 0;
		echo json_encode($response);
	}
}

if (isset($_POST['id_toko'])) {
	$id_toko = $_POST['id_toko'];
	// $sql = "select * from toko where id_toko = '$id_toko'";
	$sql = "SELECT a.nama,
t.id_toko, t.nama_toko, t.alamat_toko, t.telp, t.deskripsi_toko, t.gambar, t.id_kampung, t.id_anggota, t.lat, t.lng
from anggota as a, toko as t
WHERE a.id_anggota = t.id_anggota
&& t.id_toko = '$id_toko'";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		$response['toko'] = array();
		while ($row = mysql_fetch_array($result)) {
			$toko = array();
			$toko['nama'] = $row['nama'];
			$toko["id_toko"]		= $row["id_toko"];
			$toko["nama_toko"]		= $row["nama_toko"];
			$toko["alamat_toko"]	= $row["alamat_toko"];
			$toko["telp"]			= $row["telp"];
			$toko["deskripsi_toko"]	= $row["deskripsi_toko"];
			$toko["gambar"]			= $sv.$row["gambar"];
			$toko["id_kampung"]		= $row["id_kampung"];
			$toko["id_anggota"]		= $row["id_anggota"];
			$toko['lat'] = $row['lat'];
			$toko['lng'] = $row['lng'];
			array_push($response["toko"], $toko);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		echo json_encode($response);
	}
}

if (isset($_POST['id_kampung'])) {
	# code...
	$id_kampung = $_POST['id_kampung'];
	// $sql = "SELECT * from toko where id_kampung = '$id_kampung'";
	$sql = "SELECT
a.nama,
t.id_toko, t.nama_toko, t.alamat_toko, t.telp, t.deskripsi_toko, t.gambar, t.id_kampung, t.id_anggota, t.lat, t.lng
from toko as t, anggota as a
WHERE
a.id_anggota = t.id_anggota &&
t.id_kampung = '$id_kampung'";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['toko'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$toko = array();
			$toko['nama'] = $row['nama'];
			$toko["id_toko"]		= $row["id_toko"];
			$toko["nama_toko"]		= $row["nama_toko"];
			$toko["alamat_toko"]	= $row["alamat_toko"];
			$toko["telp"]			= $row["telp"];
			$toko["deskripsi_toko"]	= $row["deskripsi_toko"];
			$toko["gambar"]			= $sv.$row["gambar"];
			$toko["id_kampung"]		= $row["id_kampung"];
			$toko["id_anggota"]		= $row["id_anggota"];
			$toko['lat'] = $row['lat'];
			$toko['lng'] = $row['lng'];
			array_push($response["toko"], $toko);
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