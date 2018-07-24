<?php 


require_once '../db_config.php';

$upload_path = '../kampung/gambar/';
$server_ip = '192.168.43.192';
$sv = 'http://'.$server_ip.'/ukm_digital/kampung/gambar/';


$response = array();
if (isset($_POST['id_kampung']) && isset($_POST['nama_kampung']) && isset($_POST['alamat_kampung'])
	&& isset($_POST['name']) && isset($_FILES['image']['name']) && isset($_POST['lat']) && isset($_POST['lng'])) {
	$id_kampung = $_POST['id_kampung'];
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
	$s = "select gambar from kampung where id_kampung = '$id_kampung'";
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
		$result = mysql_query("update kampung set
			nama_kampung = '$nama_kampung',
			alamat_kampung = '$alamat_kampung',
			gambar = '$file_url',
			lat = '$lat',
			lng = '$lng'
			where id_kampung = '$id_kampung' ");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "successfully created";
			echo json_encode($response);
			unlink('../kampung/gambar/'.$gam["gambar"]);
        
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

if (isset($_POST['id_kampung']) && isset($_POST['nama_kampung']) && isset($_POST['alamat_kampung'])
	&& !isset($_POST['name']) && !isset($_FILES['image']['name']) && isset($_POST['lat']) && isset($_POST['lng'])) {
	$id_kampung = $_POST['id_kampung'];
	$nama_kampung = $_POST['nama_kampung'];
	$alamat_kampung = $_POST['alamat_kampung'];
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];
	
	require_once '../db_connect.php';
	$db = new DB_CONNECT();
	$result = mysql_query("update kampung set
		nama_kampung = '$nama_kampung',
		alamat_kampung = '$alamat_kampung',
		lat = '$lat',
		lng = '$lng'
		where id_kampung = '$id_kampung' ");

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