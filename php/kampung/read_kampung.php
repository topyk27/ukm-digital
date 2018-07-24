<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = $db::$svr . "/ukm_digital/kampung/gambar/";

if (!isset($_POST['id_kampung'])) {
	
	$sql = "select * from kampung";
$kampung_result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($kampung_result) > 0) {
	$response["kampung"] = array();
	while ($row = mysql_fetch_array($kampung_result)) {
		$kampung = array();
		$kampung["id_kampung"]		= $row["id_kampung"];
		$kampung["nama_kampung"]		= $row["nama_kampung"];
		$kampung["alamat_kampung"]	= $row["alamat_kampung"];
		$kampung["gambar"]			= $sv.$row["gambar"];
		$kampung['lat'] = $row['lat'];
		$kampung['lng'] = $row['lng'];
		array_push($response["kampung"], $kampung);

	}
	$response["success"] = 1;
	echo json_encode($response);
} else{
	$response["success"] = 0;
	$response["message"] = "tidak ada kampung";
	echo json_encode($response);
}
}
else if (isset($_POST['id_kampung'])) {
	$id_dari_hp = $_POST['id_kampung'];
	$r = array();

	$s = "select nama_kampung from kampung where id_kampung = '$id_dari_hp'";
	$nama_kampung = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($nama_kampung) > 0) {
		$r['nama_kampung'] = array();
		while ($row = mysql_fetch_array($nama_kampung)) {
			$nama = array();
			$nama['nama_kampung'] = $row['nama_kampung'];
			array_push($r['nama_kampung'], $nama);
		}
		$r['success'] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "gak ada";
		echo json_encode($r);
	}
}


 ?>