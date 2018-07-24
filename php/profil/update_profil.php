<?php 

require_once '../db_config.php';

$upload_path = '../profil/gambar/';
$server_ip = '192.168.43.192';
$sv = 'http://'.$server_ip.'/ukm_digital/profil/gambar/';

$response = array();

if (isset($_POST['id']) && isset($_POST['nama']) && isset($_POST['deskripsi'])
	&& isset($_POST['name']) && isset($_FILES['image']['name']) && isset($_POST['link'])) {
	# code...

	$id = $_POST['id'];
	$nama = $_POST['nama'];
	$deskripsi = $_POST['deskripsi'];
	$link = $_POST['link'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name . '.' . $extension;
	$file_path = $upload_path . $name . '.' . $extension;

	$i = 1;
	while (file_exists($file_path)) {
		# code...
		$newname = $_POST['name'].$i;
		$file_url = $newname . '.' . $extension;
		$file_path = $upload_path . $newname . '.' . $extension;
		$i++;
	}

	require_once '../db_connect.php';
	$db = new DB_CONNECT();

	$s = "SELECT gambar from profil where id = '$id'";
	$hasil = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($hasil) > 0) {
		# code...
		$r['gam'] = array();
		while ($row = mysql_fetch_array($hasil)) {
			# code...
			$gam = array();
			$gam['gambar'] = $row['gambar'];
			array_push($r['gam'], $gam);
		}
		$r['success'] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
    	$r["message"] = "tidak ada data gambar";
    	echo json_encode($r);
	}

	try {
		move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
		$result = mysql_query("UPDATE profil set
			nama = '$nama',
			deskripsi = '$deskripsi',
			gambar = '$file_url',
			link = '$link'
			where id = '$id' ");

		if ($result) {
			# code...
			$response["success"] = 1;
			$response["message"] = "successfully created";
			echo json_encode($response);
			unlink('../profil/gambar/'.$gam["gambar"]);
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

if (isset($_POST['id']) && isset($_POST['nama']) && isset($_POST['deskripsi'])
	&& !isset($_POST['name']) && !isset($_FILES['image']['name']) && isset($_POST['link'])) {
	# code...
	$id = $_POST['id'];
	$nama = $_POST['nama'];
	$deskripsi = $_POST['deskripsi'];
	$link = $_POST['link'];

	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("UPDATE profil set
		nama = '$nama',
		deskripsi = '$deskripsi',
		link = '$link'
		where id = '$id' ");

	if ($result) {
		# code...
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