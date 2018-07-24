<?php 

require_once "../db_connect.php";
$db = new DB_CONNECT();

$sv = $db::$svr . "/ukm_digital/admin/gambar/";
$email = $_GET["email"];
$password = $_GET["password"];
$query = "select * from admin where username='$email' and password='$password' limit 1";
$hasil = mysql_query($query) or die(mysql_error());
if (mysql_num_rows($hasil) > 0) {
	$respone = array();
	$respone["login"] = array();
	while ($data = mysql_fetch_array($hasil)) {
		$h['id_anggota'] = $data['id_admin'];
		$h['nama'] = $data['nama'];
		$h['email'] = $data['email'];
		$h['gambar'] = $sv.$data['gambar'];
		$h['username'] = $data['username'];
		$h['password'] = $data ['password'];

		array_push($respone["login"], $h);

	}
	$respone["success"] = "1";
	echo json_encode($respone);
}
else
{
	$respone["success"] = "0";
	$respone["message"] = "Tidak ada data";
	echo json_encode($respone);
}

 ?>