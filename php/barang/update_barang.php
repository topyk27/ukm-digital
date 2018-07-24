<?php 

require_once '../db_config.php';
require_once '../resize.php';

$upload_path = '../barang/gambar/';
$server_ip = '192.168.43.192';
$sv = 'http://'.$server_ip.'/ukm_digital/barang/gambar/';
//$upload_url = 'http://'.$server_ip.'/ukm_digital/barang'.$upload_path;

$response = array();
$r = array();
require_once '../db_connect.php';
	$db = new DB_CONNECT();
if (isset($_POST['id_barang']) && isset($_POST['nama_barang']) && isset($_POST['harga']) && isset($_POST['stok'])
	&& isset($_POST['deskripsi_barang']) && isset($_POST['name']) && isset($_FILES['image']['name'])
	&& isset($_POST['id_jenis_barang'])
	&& isset($_POST['ukuran']) && isset($_POST['merk']) && isset($_POST['khusus'])) {

	// require_once '../db_connect.php';
	// $db = new DB_CONNECT();
	$nama_jenis = $_POST['id_jenis_barang'];
	$sql = "select id_jenis_barang from jenis_barang where jenis_barang = '$nama_jenis'";
	$res = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($res) > 0) {
		$r["jenis_barang"] = array();
		while ($row = mysql_fetch_array($res)) {
			$jenis_barang = array();
			$jenis_barang["id_jenis_barang"] = $row["id_jenis_barang"];
			array_push($r["jenis_barang"], $jenis_barang);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "nade id jenis barang";
		echo json_encode($r);
	}
	$id_barang = $_POST['id_barang'];
	$nama_barang = $_POST['nama_barang'];
	$harga = $_POST['harga'];
	$stok = $_POST['stok'];
	$deskripsi_barang = $_POST['deskripsi_barang'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name.'.'.$extension;
	//new
	
	//end new
	$file_path = $upload_path.$name.'.'.$extension;
	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	$ukuran = $_POST['ukuran'];
	$merk = $_POST['merk'];
	$khusus = $_POST['khusus'];

	$i = 1;
	while (file_exists($file_path)) {
		$newname = $_POST['name'].$i;
		$file_url = $newname.'.'.$extension;
		$file_path = $upload_path.$newname.'.'.$extension;
		$i++;
	}

	// require_once '../db_connect.php';
	// $db = new DB_CONNECT();
	$s = "select gambar from barang where id_barang = '$id_barang'";
	$hasil = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($hasil) > 0 ) {
		$re["bar"] = array();
		while ($row = mysql_fetch_array($hasil)) {
			$bar = array();
			$bar["gambar"] = $row["gambar"];
			array_push($re["bar"], $bar);
		}
		$re["success"] = 1;
		echo json_encode($re);
	}
	else{
		$re["success"] = 0;
    	$re["message"] = "tidak ada data gambar";
    	echo json_encode($re);
	}

	try {
		move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
		$result = mysql_query("update barang set
			nama_barang = '$nama_barang',
			ukuran = '$ukuran',
			merk = '$merk',
			khusus = '$khusus',
			harga = '$harga',
			stok = '$stok',
			deskripsi_barang = '$deskripsi_barang',
			gambar = '$file_url',
			id_jenis_barang = '$id_jenis_barang'
			where id_barang = '$id_barang' ");

		if ($result) {
			$response["success"] = 1;
			$response["message"] = "successfully created";
			echo json_encode($response);
			unlink('../barang/gambar/'.$bar["gambar"]);
        
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

if (isset($_POST['id_barang']) && isset($_POST['nama_barang']) && isset($_POST['harga']) && isset($_POST['stok'])
	&& isset($_POST['deskripsi_barang']) && !isset($_POST['name']) && !isset($_FILES['image']['name'])
	&& isset($_POST['id_jenis_barang'])
	&& isset($_POST['ukuran']) && isset($_POST['merk']) && isset($_POST['khusus'])) {

// echo "aku tampan";
// require_once '../db_connect.php';
// 	$db = new DB_CONNECT();
	$nama_jenis = $_POST['id_jenis_barang'];
	$sql = "select id_jenis_barang from jenis_barang where jenis_barang = '$nama_jenis'";
	$res = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($res) > 0) {
		$r["jenis_barang"] = array();
		while ($row = mysql_fetch_array($res)) {
			$jenis_barang = array();
			$jenis_barang["id_jenis_barang"] = $row["id_jenis_barang"];
			array_push($r["jenis_barang"], $jenis_barang);
		}
		$r["success"] = 1;
		echo json_encode($r);
	}
	else{
		$r["success"] = 0;
		$r["message"] = "nade id jenis barang";
		echo json_encode($r);
	}

	$id_barang = $_POST['id_barang'];
	$nama_barang = $_POST['nama_barang'];
	$harga = $_POST['harga'];
	$stok = $_POST['stok'];
	$deskripsi_barang = $_POST['deskripsi_barang'];
	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	$ukuran = $_POST['ukuran'];
	$merk = $_POST['merk'];
	$khusus = $_POST['khusus'];
	

	// require_once '../db_connect.php';
	// $db = new DB_CONNECT();
	$result = mysql_query("update barang set
		nama_barang = '$nama_barang',
		ukuran = '$ukuran',
		merk = '$merk',
		khusus = '$khusus',
		harga = '$harga',
		stok = '$stok',
		deskripsi_barang = '$deskripsi_barang',
		id_jenis_barang = '$id_jenis_barang'
		where id_barang = '$id_barang' ") or die(mysql_error());



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