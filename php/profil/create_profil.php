<?php 

require_once '../db_config.php';
$upload_path = '../profil/gambar/';
$server_ip = '192.168.43.192';

$response = array();

if (isset($_POST['nama']) && isset($_POST['deskripsi']) && isset($_POST['link']) && isset($_FILES['image']['name'])) {
	# code...


$nama = $_POST['nama'];
$deskripsi = $_POST['deskripsi'];
$link = $_POST['link'];
$name = $_POST['name'];
$fileinfo = pathinfo($_FILES['image']['name']);
$extension = $fileinfo['extension'];
$file_url = $name.'.'.$extension;
$file_path = $upload_path . $name . '.' . $extension;

$i = 1;
while (file_exists($file_path)) {
	# code...
	$newname = $_POST['name'].$i;
	$file_url = $newname.'.'.$extension;
	$file_path = $upload_path.$newname.'.'.$extension;
	$i++;
}

require_once '../db_connect.php';
$db = new DB_CONNECT();
try {
	move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
	$query = "INSERT into profil (nama, deskripsi, gambar, link)
	values ('$nama', '$deskripsi', '$file_url', '$link')";
	$result = mysql_query($query);

	if ($result) {
		# code...
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