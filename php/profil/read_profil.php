<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
$sv = $db::$svr . "/ukm_digital/profil/gambar/";

if (isset($_POST['id'])) {
	# code...


$id = $_POST['id'];

$sql = "SELECT * from profil where id = '$id'";
$result = mysql_query($sql) or die(mysql_error());

if (mysql_num_rows($result) > 0) {
	# code...
	$response['profil'] = array();
	while ($row = mysql_fetch_array($result)) {
		# code...
		$profil = array();
		$profil['id'] = $row['id'];
		$profil['nama'] = $row['nama'];
		$profil['deskripsi'] = $row['deskripsi'];
		$profil['gambar'] = $sv.$row['gambar'];
		$profil['link'] = $row['link'];
		array_push($response['profil'], $profil);
	}
	$response["success"] = 1;
	echo json_encode($response);
}
else{
	$response["success"] = 0;
	$response["message"] = "tidak ada data";
	echo json_encode($response);
}

}

if (!isset($_POST['id'])) {
	# code...
	$sql = "SELECT * from profil";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['profil'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
		$profil = array();
		$profil['id'] = $row['id'];
		$profil['nama'] = $row['nama'];
		$profil['deskripsi'] = $row['deskripsi'];
		$profil['gambar'] = $sv.$row['gambar'];
		$profil['link'] = $row['link'];
		array_push($response['profil'], $profil);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
	$response["success"] = 0;
	$response["message"] = "tidak ada data";
	echo json_encode($response);
	}
}
 ?>