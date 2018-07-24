<?php 

$response = array();

if (isset($_POST['id'])) {
	# code...
	$id = $_POST['id'];
	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$s = "SELECT gambar from profil where id = '$id'";
	$hasil = mysql_query($s) or die(mysql_error());

	if (mysql_num_rows($hasil) > 0) {
		# code...
		$r['gbr'] = array();
		while ($row = mysql_fetch_array($hasil)) {
			# code...
			$gbr = array();
			$gbr['gambar'] = $row['gambar'];
			array_push($r['gbr'], $gbr);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "tidak ada gambar";
		echo json_encode($r);
	}

	$result = mysql_query("DELETE from profil where id = '$id'");
	if (mysql_affected_rows() > 0) {
		# code...
		$response["success"] = 1;
		$response["message"] = "successfully deleted";
		unlink('../profil/gambar/'.$gbr["gambar"]);
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "no found";
		echo json_encode($response);
	}
}


 ?>