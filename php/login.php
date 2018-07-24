<?php 

require_once "db_connect.php";
$db = new DB_CONNECT();

$sv = $db::$svr . "/ukm_digital/anggota/gambar/";
$sva = $db::$svr . "/ukm_digital/admin/gambar/";
$email = $_GET["email"];
$password = $_GET["password"];
$query = "select * from anggota where username='$email' and password='$password' limit 1";
$hasil = mysql_query($query) or die(mysql_error());
if (mysql_num_rows($hasil) > 0) {
	$respone = array();
	$respone["login"] = array();
	while ($data = mysql_fetch_array($hasil)) {
		$h = array();
		$h['id_anggota'] = $data['id_anggota'];
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
	// $respone["success"] = "0";
	// $respone["message"] = "Tidak ada data";
	// echo json_encode($respone);
	$sql = "SELECT * FROM admin where username = '$email' and password = '$password' limit 1";
	$result = mysql_query($sql) or die(mysql_error());
	if (mysql_num_rows($result) > 0) {
		# code...
		$respone = array();
		$respone['login_admin'] = array();
		while ($d = mysql_fetch_array($result)) {
			# code...
			$hsl = array();
			$hsl['id_admin'] = $d['id_admin'];
			$hsl['nama'] = $d['nama'];
			$hsl['email'] = $d['email'];
			$hsl['gambar'] = $sva.$d['gambar'];
			$hsl['username'] = $d['username'];
			$hsl['password'] = $d['password'];

			array_push($respone['login_admin'], $hsl);

		}
		$respone['success'] = "1";
		echo json_encode($respone);
	}
	else{
		$respone["success"] = "0";
		$respone["message"] = "Tidak ada data";
		echo json_encode($respone);
	}
}

 ?>