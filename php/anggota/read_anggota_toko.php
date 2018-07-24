<?php 

//ini buat cek anggota yg belum bikin toko
$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = $db::$svr . "/ukm_digital/anggota/gambar/";

$sql = "select * from anggota where id_anggota not in(select id_anggota from toko)";
$anggota_result = mysql_query ($sql) or die(mysql_error()); //run the query

if (mysql_num_rows($anggota_result) > 0) {
	$response["anggota"] = array();
	while ($row = mysql_fetch_array($anggota_result)) {
		$anggota = array();
		$anggota["id_anggota"]		= $row["id_anggota"];
		$anggota["nama"]		= $row["nama"];
		$anggota["alamat"]	= $row["alamat"];
		$anggota["no_hp"] = $row["no_hp"];
		$anggota["email"] = $row["email"];
		$anggota["gambar"]			= $sv.$row["gambar"];
		$anggota["username"] = $row["username"];
		$anggota["password"] = $row["password"];
		
		array_push($response["anggota"], $anggota);

	}
	$response["success"] = 1;
	echo json_encode($response);
} else{
	$response["success"] = 0;
	$response["message"] = "tidak ada anggota";
	echo json_encode($response);
}

 ?>