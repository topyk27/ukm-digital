<?php
require_once '../db_config.php';
$upload_path = '../barang/gambar/';
$server_ip = '192.168.43.192';
//$upload_url = 'http://'.$server_ip.'/ukm_digital/barang/gambar/';

$response = array();
if (isset($_POST['nama_barang']) && isset($_POST['harga']) && isset($_POST['stok']) && isset($_POST['deskripsi_barang'])
	&& isset($_FILES['image']['name']) && isset($_POST['id_jenis_barang']) && isset($_POST['id_anggota'])
	&& isset($_POST['ukuran']) && isset($_POST['merk']) && isset($_POST['khusus'])) {
	
	require_once '../db_connect.php';
	$db = new DB_CONNECT();
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

	$id_anggota = $_POST['id_anggota'];
	$s ="select id_toko from toko where id_anggota = '$id_anggota'";
	$h = mysql_query($s) or die(mysql_error());
	if (mysql_num_rows($h) > 0) {
		$r1['toko'] = array();
		while ($ro = mysql_fetch_array($h)) {
			$toko = array();
			$toko['id_toko'] = $ro['id_toko'];
			array_push($r1['toko'], $toko);
		}
	}

	$nama_barang = $_POST['nama_barang'];
	$harga = $_POST['harga'];
	$stok = $_POST['stok'];
	$deskripsi_barang = $_POST['deskripsi_barang'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name.'.'.$extension;
	$file_path = $upload_path.$name.'.'.$extension;
	// $id_jenis_barang = $_POST['id_jenis_barang'];
	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	$id_toko = $toko['id_toko'];
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
try {
	move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
	$result = mysql_query("insert into barang (nama_barang, ukuran, merk, khusus, harga, stok, deskripsi_barang, gambar, id_jenis_barang, id_toko)
		values ('$nama_barang', '$ukuran', '$merk', '$khusus', '$harga', '$stok', '$deskripsi_barang', '$file_url', '$id_jenis_barang', '$id_toko')");
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

if (isset($_POST['nama_barang']) && isset($_POST['harga']) && isset($_POST['stok']) && isset($_POST['deskripsi_barang'])
	&& isset($_FILES['image']['name']) && isset($_POST['id_jenis_barang']) && isset($_POST['id_toko'])
	&& isset($_POST['ukuran']) && isset($_POST['merk']) && isset($_POST['khusus'])) {
	require_once '../db_connect.php';
	$db = new DB_CONNECT();
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
	$nama_barang = $_POST['nama_barang'];
	$harga = $_POST['harga'];
	$stok = $_POST['stok'];
	$deskripsi_barang = $_POST['deskripsi_barang'];
	$name = $_POST['name'];
	$fileinfo = pathinfo($_FILES['image']['name']);
	$extension = $fileinfo['extension'];
	$file_url = $name.'.'.$extension;
	$file_path = $upload_path.$name.'.'.$extension;
	// $id_jenis_barang = $_POST['id_jenis_barang'];
	$id_jenis_barang = $jenis_barang['id_jenis_barang'];
	$id_toko = $_POST['id_toko'];
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
try {
	move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
	$result = mysql_query("insert into barang (nama_barang, ukuran, merk, khusus, harga, stok, deskripsi_barang, gambar, id_jenis_barang, id_toko)
		values ('$nama_barang', '$ukuran', '$merk', '$khusus', '$harga', '$stok', '$deskripsi_barang', '$file_url', '$id_jenis_barang', '$id_toko')");
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