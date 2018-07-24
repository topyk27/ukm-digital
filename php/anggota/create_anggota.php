<?php 

require_once '../db_config.php';
$upload_path = '../anggota/gambar/';
$server_ip = '192.168.43.192';


$response = array();
if (isset($_POST['nama']) && isset($_POST['alamat']) && isset($_POST['no_hp']) && isset($_POST['email'])
	&& isset($_FILES['image']['name']) && isset($_POST['username']) && isset($_POST['password'])) {
	$nama = $_POST['nama'];
	$alamat = $_POST['alamat'];
	$no_hp = $_POST['no_hp'];
	$email = $_POST['email'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name.'.'.$extension;
	$file_path = $upload_path.$name.'.'.$extension;
	$username = $_POST['username'];
	$password = $_POST['password'];

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
	$result = mysql_query("insert into anggota (nama, alamat, no_hp, email, gambar, username, password)
		values ('$nama', '$alamat', '$no_hp', '$email', '$file_url', '$username', '$password')");
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