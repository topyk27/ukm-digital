<?php 

$response = array();

if (isset($_POST['id_kampung'])) {
	$id_kampung = $_POST['id_kampung'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$s = "select gambar from kampung where id_kampung = '$id_kampung' ";
	$hasil = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($hasil) > 0) {
		$r["gbr"] = array();
		while ($row = mysql_fetch_array($hasil)) {
			$gbr = array();
			$gbr["gambar"] = $row["gambar"];
			array_push($r["gbr"], $gbr);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "tidak ada gambar";
		echo json_encode($r);
	}

	$result = mysql_query("delete from kampung where id_kampung = '$id_kampung' ");
	if (mysql_affected_rows() > 0) {
		$response["success"] = 1;
		$response["message"] = "successfully deleted";
		unlink('../kampung/gambar/'.$gbr["gambar"]);
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "no found";
		echo json_encode($response);

	}
}
else{
	$response["success"] = 0;
	$response["message"] = "Required fields is missing";
	echo json_encode($response);
}

 ?>