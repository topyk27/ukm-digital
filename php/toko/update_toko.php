<?php 

require_once '../db_config.php';

$upload_path = '../toko/gambar/';
$server_ip = '192.168.43.192';
$sv = 'http://'.$server_ip.'/ukm_digital/toko/gambar/';
require_once '../db_connect.php';
	$db = new DB_CONNECT();

$response = array();
if (isset($_POST['id_toko']) && isset($_POST['nama_toko']) && isset($_POST['alamat_toko']) && isset($_POST['telp'])
	&& isset($_POST['deskripsi_toko']) && isset($_POST['name']) && isset($_FILES['image']['name'])
	&& isset($_POST['id_kampung']) && isset($_POST['lat']) && isset($_POST['lng'])) {

	$nama_kampung = $_POST['id_kampung'];
	$sql = "select id_kampung from kampung where nama_kampung = '$nama_kampung'";
	$res = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($res) > 0) {
		$r['nama_kampung'] = array();
		while ($row = mysql_fetch_array($res)){
			$kampung = array();
			$kampung['id_kampung'] = $row['id_kampung'];
			array_push($r['nama_kampung'], $kampung);
		}
	}

	$id_toko = $_POST['id_toko'];
	$nama_toko = $_POST['nama_toko'];
	$alamat_toko = $_POST['alamat_toko'];
	$telp = $_POST['telp'];
	$deskripsi_toko = $_POST['deskripsi_toko'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name.'.'.$extension;
	$file_path = $upload_path.$name.'.'.$extension;
	$id_kampung = $kampung['id_kampung'];
	// $id_kampung = $_POST['id_kampung'];
	// $id_anggota = $_POST['id_anggota'];
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];

	$i = 1;
	while (file_exists($file_path)) {
		$newname = $_POST['name'].$i;
		$file_url = $newname.'.'.$extension;
		$file_path = $upload_path.$newname.'.'.$extension;
		$i++;
	}

	// require_once '../db_connect.php';
	// $db = new DB_CONNECT();
	$s = "select gambar from toko where id_toko = '$id_toko'";
	$hasil = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($hasil) > 0 ) {
		$r["gam"] = array();
		while ($row = mysql_fetch_array($hasil)) {
			$gam = array();
			$gam["gambar"] = $row["gambar"];
			array_push($r["gam"], $gam);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
    	$r["message"] = "tidak ada data gambar";
    	echo json_encode($r);
	}

	try {
		move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
		$result = mysql_query("update toko set
			nama_toko = '$nama_toko',
			alamat_toko = '$alamat_toko',
			telp = '$telp',
			deskripsi_toko = '$deskripsi_toko',
			gambar = '$file_url',
			id_kampung = '$id_kampung',
			lat = '$lat',
			lng = '$lng'
			where id_toko = '$id_toko' ");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "successfully created";
			echo json_encode($response);
			unlink('../toko/gambar/'.$gam["gambar"]);
        
		}
		else{
			$response["success"] = 0;
			$response["message"] = "Required field(s) is missing";
			echo json_encode($response);
		}
	} catch (Exception $e) {
		$response['error'] = true;
		$response['message'] = $e->getMessage();
	}
}

if (isset($_POST['id_toko']) && isset($_POST['nama_toko']) && isset($_POST['alamat_toko']) && isset($_POST['telp'])
	&& isset($_POST['deskripsi_toko']) && !isset($_POST['name']) && !isset($_FILES['image']['name'])
	&& isset($_POST['id_kampung']) && isset($_POST['lat']) && isset($_POST['lng'])) {

	$nama_kampung = $_POST['id_kampung'];
	$sql = "select id_kampung from kampung where nama_kampung = '$nama_kampung'";
	$res = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($res) > 0) {
		$r['nama_kampung'] = array();
		while ($row = mysql_fetch_array($res)){
			$kampung = array();
			$kampung['id_kampung'] = $row['id_kampung'];
			array_push($r['nama_kampung'], $kampung);
		}
	}

	$id_toko = $_POST['id_toko'];
	$nama_toko = $_POST['nama_toko'];
	$alamat_toko = $_POST['alamat_toko'];
	$telp = $_POST['telp'];
	$deskripsi_toko = $_POST['deskripsi_toko'];
	$id_kampung = $kampung['id_kampung'];
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];
	// $id_kampung = $_POST['id_kampung'];
	// $id_anggota = $_POST['id_anggota'];
	// require_once '../db_connect.php';
	// $db = new DB_CONNECT();
	$result = mysql_query("update toko set
		nama_toko = '$nama_toko',
		alamat_toko = '$alamat_toko',
		telp = '$telp',
		deskripsi_toko = '$deskripsi_toko',
		id_kampung = '$id_kampung',
		lat = '$lat',
		lng = '$lng'
		where id_toko = '$id_toko' ") or die(mysql_error());

	if ($result) {
		$response["success"] = 1;
		$response["message"] = "successfully created";
		echo json_encode($response);

	}
	else{
		$response["success"] = 0;
		$response["message"] = "Missing";
		echo json_encode($response);
	}
}


 ?>