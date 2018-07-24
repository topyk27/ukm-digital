<?php 

$response = array();

require_once '../db_connect.php';
$db = new DB_CONNECT();
if (isset($_POST['id_anggota'])) {
	# code...
	$id_anggota = $_POST['id_anggota'];
	$sql = "SELECT id_anggota from toko where id_anggota = '$id_anggota' ";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$response['toko'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$toko = array();
			$toko['id_anggota'] = $row['id_anggota'];
			array_push($response['toko'], $toko);
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