<?php 
$response = array();

require_once '../db_connect.php';
// $db = new DB_CONNECT();
// $sv = "http://192.168.43.192/ukm_digital/anggota/gambar/";

//cari dengan nama
if (isset($_POST['nama']) && !isset($_POST['alamat']) && !isset($_POST['toko'])) {
	# code...
	$nama = $_POST['nama'];
	$sql = "SELECT * from anggota where nama like '%$nama%' ";
	read_anggota($sql);
}
//cari dengan alamat
if (!isset($_POST['nama']) && isset($_POST['alamat']) && !isset($_POST['toko'])) {
	# code...
	$alamat = $_POST['alamat'];
	$sql = "SELECT * from anggota where alamat like '%$alamat%' ";
	read_anggota($sql);
}
//cari dengan nama dan alamat
if (isset($_POST['nama']) && isset($_POST['alamat']) && !isset($_POST['toko'])) {
	# code...
	$nama = $_POST['nama'];
	$alamat = $_POST['alamat'];
	$sql = "SELECT * from anggota where nama like '%$nama%' && alamat like '%$alamat%' ";
	read_anggota($sql);
}
//cari yang punya toko
if (!isset($_POST['nama']) && !isset($_POST['alamat']) && isset($_POST['toko'])) {
	# code...
	$toko = $_POST['toko'];
	//anggota yang gak punya toko
	if ($toko == "0") {
		# code...
		$sql = "SELECT * from anggota where id_anggota NOT IN (SELECT id_anggota from toko)";
	}
	//anggota yang punya toko
	else{
		$sql = "SELECT * from anggota where id_anggota IN (SELECT id_anggota from toko)";
	}
	read_anggota($sql);
}
//cari yang punya toko pake nama
if (isset($_POST['nama']) && !isset($_POST['alamat']) && isset($_POST['toko'])) {
	# code...
	$nama = $_POST['nama'];
	$toko = $_POST['toko'];
	//anggota yang gak punya toko
	if ($toko == "0") {
		# code...
		$sql = "SELECT * from anggota where nama like '%$nama%' && id_anggota NOT IN (SELECT id_anggota from toko) ";
	}
	else{
		$sql = "SELECT * from anggota where nama like '%$nama%' && id_anggota IN (SELECT id_anggota from toko) ";
	}
	read_anggota($sql);
}
//cari yang punya toko pake alamat
if (!isset($_POST['nama']) && isset($_POST['alamat']) && isset($_POST['toko'])) {
	# code...
	$alamat = $_POST['alamat'];
	$toko = $_POST['toko'];
	//anggota yang gak punya toko
	if ($toko == "0") {
		# code...
		$sql = "SELECT * from anggota where alamat like '%$alamat%' && id_anggota NOT IN (SELECT id_anggota from toko) ";
	}
	else{
		$sql = "SELECT * from anggota where alamat like '%$alamat%' && id_anggota IN (SELECT id_anggota from toko) ";
	}
	read_anggota($sql);
}
//cari dengan nama alamat toko
if (isset($_POST['nama']) && isset($_POST['alamat']) && isset($_POST['toko'])) {
	# code...
	$nama = $_POST['nama'];
	$alamat = $_POST['alamat'];
	$toko = $_POST['toko'];
	//anggota yang gak punya toko
	if ($toko == "0") {
		# code...
		$sql = "SELECT * from anggota where nama like '%$nama%' && alamat like '%$alamat%'
		&& id_anggota NOT IN (SELECT id_anggota from toko)";
	}
	else{
		$sql = "SELECT * from anggota where nama like '%$nama%' && alamat like '%$alamat%'
		&& id_anggota IN (SELECT id_anggota from toko)";
	}
	read_anggota($sql);
}


function read_anggota($sql){
	$db = new DB_CONNECT();
	$sv = $db::$svr . "/ukm_digital/anggota/gambar/";
$result = mysql_query($sql) or die(mysql_error());
if (mysql_num_rows($result) > 0) {
		# code...
		$response['anggota'] = array();
		while ($row = mysql_fetch_array($result)) {
			# code...
			$anggota = array();
			$anggota['id_anggota'] = $row['id_anggota'];
			$anggota['nama'] = $row['nama'];
			$anggota['alamat'] = $row['alamat'];
			$anggota['no_hp'] = $row['no_hp'];
			$anggota['email'] = $row['email'];
			$anggota['gambar'] = $sv.$row['gambar'];
			$anggota['username'] = $row['username'];
			$anggota['password'] = $row['password'];
			array_push($response['anggota'], $anggota);
		}
		$response['success'] = 1;
		echo json_encode($response);
	}
	else{
		$response['success'] = 0;
		echo json_encode($response);
	}
}
 ?>