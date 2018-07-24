<?php

require_once '../db_config.php';
$upload_path = '../kampung/gambar/';
$server_ip = '192.168.43.192';


$response = array();
if (isset($_POST['nama_kampung']) && isset($_POST['alamat_kampung']) && isset($_FILES['image']['name'])
	&& isset($_POST['lat']) && isset($_POST['lng'])) {
	$nama_kampung = $_POST['nama_kampung'];
	$alamat_kampung = $_POST['alamat_kampung'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name.'.'.$extension;
	$file_path = $upload_path.$name.'.'.$extension;
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];
	

	$i = 1;
	while (file_exists($file_path)) {
		$newname = $_POST['name'].$i;
		$file_url = $newname.'.'.$extension;
		$file_path = $upload_path.$newname.'.'.$extension;
		$i++;
	}

require_once '../db_connect.php';
$db = new DB_CONNECT();
try {
	move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
	$result = mysql_query("insert into kampung (nama_kampung, alamat_kampung, gambar, lat, lng)
		values ('$nama_kampung', '$alamat_kampung', '$file_url', '$lat', '$lng')");
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