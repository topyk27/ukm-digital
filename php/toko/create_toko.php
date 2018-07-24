create_toko.php
<?php 

require_once '../db_config.php';
$upload_path = '../toko/gambar/';
$server_ip = '192.168.43.192';


$response = array();
if (isset($_POST['nama_toko']) && isset($_POST['alamat_toko']) && isset($_POST['telp']) && isset($_POST['deskripsi_toko'])
	&& isset($_FILES['image']['name']) && isset($_POST['id_kampung']) && isset($_POST['id_anggota'])
	&& isset($_POST['lat']) && isset($_POST['lng'])) {

require_once '../db_connect.php';
$db = new DB_CONNECT();
//ambil id kampung dari db
$nama_kampung = $_POST['id_kampung'];
$sql = "select id_kampung from kampung where nama_kampung = '$nama_kampung'";
$res = mysql_query($sql) or die(mysql_error());
if (mysql_num_rows($res) > 0) {
	$r["kampung"] = array();
	while ($row = mysql_fetch_array($res)) {
		$kampung = array();
		$kampung['id_kampung'] = $row['id_kampung'];
		array_push($r["kampung"], $kampung);
	}
	echo json_encode($r);
}
else{
	$r['message'] = "nade";
	echo json_encode($r);
}
//end ambil id kampung

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
	$id_anggota = $_POST['id_anggota'];
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
try {
	move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
	$result = mysql_query("insert into toko (nama_toko, alamat_toko, telp, deskripsi_toko, gambar, id_kampung, id_anggota, lat, lng)
		values ('$nama_toko', '$alamat_toko', '$telp', '$deskripsi_toko', '$file_url', '$id_kampung', '$id_anggota', '$lat', '$lng')");
	if ($result) {
		$response["success"] = 1;
		$response["message"] = "successfully created";
		echo json_encode($response);
	}
	else{
		$response["success"] = 0;
		$response["message"] = "oops!";
		echo json_encode($response);
	}
} catch (Exception $e) {
	$response['error'] = true;
	$response['message'] = $e->getMessage();
}

}

 ?>