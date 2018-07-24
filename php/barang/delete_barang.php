<?php

$response = array();

if (isset($_POST['id_barang'])) {
	$id_barang = $_POST['id_barang'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$s = "select gambar from barang where id_barang = '$id_barang' ";
	$hasil = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($hasil) > 0) {
		$r["bar"] = array();
		while ($row = mysql_fetch_array($hasil)) {
			$bar = array();
			$bar["gambar"] = $row["gambar"];
			array_push($r["bar"], $bar);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "tidak ada gambar";
		echo json_encode($r);
	}

	$result = mysql_query("delete from barang where id_barang = '$id_barang' ");
	if (mysql_affected_rows() > 0) {
		$response["success"] = 1;
		$response["message"] = "successfully deleted";
		unlink('../barang/gambar/'.$bar["gambar"]);
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "no found";
		echo json_encode($response);

	}
}
// else{
// 	$response["success"] = 0;
// 	$response["message"] = "Required fields is missing";
// 	echo json_encode($response);
// }

  ?>