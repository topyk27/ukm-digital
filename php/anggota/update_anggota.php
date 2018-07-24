update_anggota.php
<?php 

require_once '../db_config.php';

$upload_path = '../anggota/gambar/';
$server_ip = '192.168.43.192';
$sv = 'http://'.$server_ip.'/ukm_digital/anggota/gambar/';


$response = array();
if (isset($_POST['id_anggota']) && isset($_POST['nama']) && isset($_POST['alamat']) && isset($_POST['no_hp'])
	&& isset($_POST['email']) && isset($_POST['name']) && isset($_FILES['image']['name'])
	&& isset($_POST['username']) && isset($_POST['password'])) {
	$id_anggota = $_POST['id_anggota'];
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
	$s = "select gambar from anggota where id_anggota = '$id_anggota'";
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
		$result = mysql_query("update anggota set
			nama = '$nama',
			alamat = '$alamat',
			no_hp = '$no_hp',
			email = '$email',
			gambar = '$file_url',
			username = '$username',
			password = '$password'
			where id_anggota = '$id_anggota' ");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "successfully created";
			echo json_encode($response);
			unlink('../anggota/gambar/'.$gam["gambar"]);
        
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

if (isset($_POST['id_anggota']) && isset($_POST['nama']) && isset($_POST['alamat']) && isset($_POST['no_hp'])
	&& isset($_POST['email']) && !isset($_POST['name']) && !isset($_FILES['image']['name'])
	&& isset($_POST['username']) && isset($_POST['password'])) {
	$id_anggota = $_POST['id_anggota'];
	$nama = $_POST['nama'];
	$alamat = $_POST['alamat'];
	$no_hp = $_POST['no_hp'];
	$email = $_POST['email'];
	$username = $_POST['username'];
	$password = $_POST['password'];
	
	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("update anggota set
		nama = '$nama',
		alamat = '$alamat',
		no_hp = '$no_hp',
		email = '$email',
		username = '$username',
		password = '$password'
		where id_anggota = '$id_anggota' ");

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